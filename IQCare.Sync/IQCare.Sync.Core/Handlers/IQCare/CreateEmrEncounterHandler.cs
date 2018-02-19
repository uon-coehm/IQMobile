using System;
using System.Collections.Generic;
using System.Data.Entity.Infrastructure.Interception;
using System.Linq;
using System.Reflection;
using System.Text;
using IQCare.Sync.Core.Events;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Model;
using IQCare.Sync.Shared;
using log4net;

namespace IQCare.Sync.Core.Handlers.IQCare
{
    public class CreateEmrEncounterHandler : ICreateEmrEncounterHandler
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private string _notification;
        private Patient _patient;
        private Encounter _encounter;
        private IQLocation _location;
        private List<SqlAction> _sqlActions;
        private string _sqlActionsBatch;
        private IQVisitType _visitType;
        private List<MConcept> _mConcepts;
        private List<LookupHTS> _lookupHtss;


        public Patient Patient
        {
            get { return _patient; }
        }

        public Encounter Encounter
        {
            get { return _encounter; }
        }

        public IQVisitType VisitType
        {
            get { return _visitType; }
            set { _visitType = value; }
        }

        public IQLocation Location
        {
            get { return _location; }
        }

        public List<MConcept> MConcepts
        {
            get { return _mConcepts; }
            set { _mConcepts = value; }
        }

        public List<LookupHTS> LookupsHts
        {
            get { return _lookupHtss; }
            set { _lookupHtss = value; }
        }

        public void Handle(EncounterCreated args)
        {
            // TODO: Remove hard codede ModuleID, VisitType, LocationID values

            _sqlActions = new List<SqlAction>();
            _patient = args.Patient;
            _location = args.Location;
            _encounter = args.Encounter;
            _visitType = args.VisitType;
            _mConcepts = args.MConcepts;
            _lookupHtss = args.LookupsHts;
            _notification = $"Creating Encounter[{args.Encounter},{args.Patient}] Observations[{args.Patient.Encounters.Count}]";
            Log.Debug(_notification);
            GenerateSqlActions();
        }

        public string GetNotification()
        {
            return _notification;
        }

        public List<SqlAction> GetSqlActions()
        {
            return _sqlActions; 
        }

        public string GetSqlActionsBatch()
        {
            return _sqlActionsBatch;
        }

        public void GenerateSqlActions()
        {
            decimal rank = 0;


            _sqlActions.Add(InsertVisit(rank)); rank++;
            _sqlActions.Add(InsertMultiData(rank)); rank++;
            _sqlActions.Add(InsertObsData(rank));   rank++;            
            _sqlActions.Add(InsertTestData(rank));
            StringBuilder sqlBuilder = new StringBuilder(" ");
            foreach (var action in _sqlActions.OrderBy(x => x.Rank))
            {
                sqlBuilder.AppendLine(action.Action);
            }

            _sqlActionsBatch = sqlBuilder.ToString();
        }

        public SqlAction InsertVisit(decimal rank)
        {
            //TODO: Remove hard codede ModuleID,VisitType,LocationID ,Signature values
            //TODO: HTCTimeOut for Android

            string sql=string.Empty;


            try
            {
                sql = $@"
    
                DECLARE @ptnpk int
                DECLARE @visitipk int
                
                SET @ptnpk=(SELECT Ptn_Pk  FROM mst_Patient WHERE SyncId ='{Patient.UuId}');       

                UPDATE 
	                [ord_Visit] 
                SET 
	                [Ptn_Pk]=@ptnpk,
                    [LocationID]=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),
                    [VisitDate]='{Patient.EnrollmentdateString}',
                    [VisitType]= {VisitType.VisitTypeID},
                    [DataQuality]='1',
                    [UserID]='{Patient.UserId}',
                    [Signature]='0',
                    [UpdateDate]=GETDATE(),
                    [IQMobileVisitType]={VisitType.VisitTypeID}
                WHERE 
	                Ptn_pk=@ptnpk AND LocationId=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0) AND IQMobileVisitType={VisitType.VisitTypeID}
                IF @@ROWCOUNT=0
                    INSERT INTO 
                        ord_Visit(Ptn_Pk, LocationID, VisitDate, VisitType,DataQuality,UserID,Signature,CreateDate,IQMobileVisitType)
                    VALUES(
                        @ptnpk,(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0), '{Patient.EnrollmentdateString}', {VisitType.VisitTypeID},'1', '{Patient.UserId}','0', GETDATE(),{VisitType.VisitTypeID});
                
                SET @visitipk=(SELECT TOP 1 [Visit_Id] FROM [ord_Visit] WHERE Ptn_Pk=@ptnpk AND IQMobileVisitType={VisitType.VisitTypeID} ORDER BY CreateDate desc);

            ";

            }
            catch (Exception ex)
            {
                Log.Debug("Error generating | Encounter Visit");
                Log.Debug(ex);
                Log.Debug(sql);
            }
           
            var action = new SqlAction(rank, sql);
            return action;
        }

        public SqlAction InsertObsData(decimal rank)
        {
            var singleBuilder = new StringBuilder();

            foreach (var o in Encounter.Observations)
            {
                if (IsSingleObs(o.MConceptId))
                {
                    singleBuilder.AppendLine(GetSingleStatement(o));
                }
            }
            var action = new SqlAction(rank, singleBuilder.ToString());
            return action;
        }

        public SqlAction InsertMultiData(decimal rank)
        {
            var multiBuilder=new StringBuilder();

            foreach (var o in Encounter.Observations)
            {
                if (IsMultiObs(o.MConceptId))
                {
                    multiBuilder.AppendLine(GetMultiStatement(o));
                }
            }
            
            var action = new SqlAction(rank, multiBuilder.ToString());
            return action;
        }

        public SqlAction InsertTestData(decimal rank)
        {
           var testObs=new List<Observation>();

            var singleBuilder = new StringBuilder();

            foreach (var o in Encounter.Observations)
            {
                if (IsTest(o.MConceptId))
                {
                    testObs.Add(o);
                }
            }

            if (testObs.Count > 0)
            {
                singleBuilder.AppendLine(GetTestingStatement(testObs));
            }
            var action = new SqlAction(rank, singleBuilder.ToString());
            return action;
        }

        public string GetMultiStatement(Observation observation)
        {
            var mConcept = GetMConcept(observation.MConceptId);
            if (null == mConcept)
            {
                Log.Debug($"No concept {observation.MConceptId} for mulit-statement");
                return string.Empty;
            }
            var iqConcept = mConcept.IQConcepts.FirstOrDefault();

            if (null == iqConcept)
            {
                Log.Debug($"No iq-concept for {observation.MConceptId}");
                return string.Empty;
            }
            StringBuilder multi = new StringBuilder();

            multi.AppendLine($@"
                    DELETE FROM
	                    [{iqConcept.Tablename}] 
                    WHERE 
	                    Ptn_pk=@ptnpk AND Visit_Pk=@visitipk AND LocationId=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0);");

            foreach (var choice in observation.GetMultipleChoices())
            {
                multi.AppendLine($@"
                    Insert into 
                        [{iqConcept.Tablename}] (
                            [ptn_pk], [Visit_Pk], [LocationID],[{iqConcept.Fieldname}],UserId, [CreateDate], DateField1, DateField2, NumericField)
                    values (
                        @ptnpk,@visitipk,(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),{choice},{Patient.UserId},Getdate(),Getdate(), Getdate(), 0);");
            }
            return multi.ToString();
        }

        public string GetSingleStatement(Observation observation)
        {
            var mConcept = GetMConcept(observation.MConceptId);

            if (null == mConcept)
            {
                Log.Debug($"No concept {observation.MConceptId} for single statement");
                return string.Empty;
            }

            var iqConcept = mConcept.IQConcepts.FirstOrDefault();

            if (null == iqConcept)
            {
                Log.Debug($"No iq-concept for {observation.MConceptId}");
                return string.Empty;
            }
            StringBuilder singleBuilder = new StringBuilder();

            singleBuilder.AppendLine(
                $@"                    
                    UPDATE 
	                    {iqConcept.Tablename} 
                    SET 
	                    {iqConcept.Fieldname}={observation.ObsvalueSqlString()} 
                    WHERE 
	                    Ptn_pk=@ptnpk AND Visit_Pk=@visitipk AND LocationId=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0)
                    IF @@ROWCOUNT=0
                        Insert into 
                            [{iqConcept.Tablename}] (
                                [ptn_pk], [Visit_Pk], [LocationID],[{iqConcept.Fieldname}],UserId, CreateDate)
                        values (
                            @ptnpk,@visitipk, (select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),{observation.ObsvalueSqlString()},{Patient.UserId},Getdate());");
            return singleBuilder.ToString();
        }

        public string GetTestingStatement(List<Observation> observations)
        {
            /*
            49	Select Test (Screening)     32	Test No 1 Result
            50	Select Test (Confirmatory)  52	Test No 2 Result
            */

            StringBuilder singleBuilder = new StringBuilder();

            var obsTest1 = observations.FirstOrDefault(x => x.MConceptId == 49);

            if (null != obsTest1)
            {
                var obsTest1Result = observations.FirstOrDefault(x => x.MConceptId == 32);
                var htslookups = LookupsHts.Where(x => x.Type.ToLower().Contains(obsTest1.ObsvalueString().ToLower()));

                if (null != obsTest1Result)
                {
                    var htslookup = htslookups.FirstOrDefault(x => x.Testresultcode == obsTest1Result.ValueNumeric);
                    if (null != htslookup)
                    {
                        singleBuilder.AppendLine(
                            $@"                    
                            UPDATE 
	                            {htslookup.Tablename} 
                            SET 
	                            {htslookup.Fieldname}={htslookup.Iqcode} 
                            WHERE 
	                            Ptn_pk=@ptnpk AND Visit_Pk=@visitipk AND LocationId=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0)
                            IF @@ROWCOUNT=0
                                Insert into 
                                    [{htslookup.Tablename}] (
                                        [ptn_pk], [Visit_Pk], [LocationID],[{htslookup.Fieldname}],UserId, CreateDate)
                                values (
                                    @ptnpk,@visitipk, (select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),{htslookup.Iqcode},{Patient.UserId},Getdate());");
                    }
                }
            }

            var obsTest2 = observations.FirstOrDefault(x => x.MConceptId == 50);

            if (null != obsTest2)
            {
                var obsTest2Result = observations.FirstOrDefault(x => x.MConceptId == 52);
                var htslookups = LookupsHts.Where(x => x.Type.ToLower().Contains(obsTest2.ObsvalueString().ToLower()));

                if (null != obsTest2Result)
                {
                    var htslookup = htslookups.FirstOrDefault(x => x.Testresultcode == obsTest2Result.ValueNumeric);
                    if (null != htslookup)
                    {
                        singleBuilder.AppendLine(
                            $@"                    
                            UPDATE 
	                            {htslookup.Tablename} 
                            SET 
	                            {htslookup.Fieldname}={htslookup.Iqcode} 
                            WHERE 
	                            Ptn_pk=@ptnpk AND Visit_Pk=@visitipk AND LocationId=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0)
                            IF @@ROWCOUNT=0
                                Insert into 
                                    [{htslookup.Tablename}] (
                                        [ptn_pk], [Visit_Pk], [LocationID],[{htslookup.Fieldname}],UserId, CreateDate)
                                values (
                                    @ptnpk,@visitipk, (select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),{htslookup.Iqcode},{Patient.UserId},Getdate());");
                    }
                }
            }

            return singleBuilder.ToString();
        }

        public bool IsSingleObs(int mConceptId)
        {
            var iqc = GetIQConcept(mConceptId);
            if (null == iqc)
            {
                Log.Debug($"iqc-{mConceptId} Not found !");
                return false;
            }
            return iqc.HasTable() && !iqc.IsMultiInsert();
        }

        public bool IsTest(int mConceptId)
        {
            var iqc = GetIQConcept(mConceptId);
            if (null == iqc)
            {
                Log.Debug($"iqc-{mConceptId} Not found !");
                return false;
            }
            return iqc.Custom;
        }

        public bool IsMultiObs(int mConceptId)
        {            
            var iqc = GetIQConcept(mConceptId);
            if (null == iqc)
            {
                Log.Debug($"iqc-{mConceptId} Not found !");
                return false;
            }
            return iqc.HasTable() && iqc.IsMultiInsert();
        }

        private MConcept GetMConcept(int mConceptId)
        {
            return MConcepts.FirstOrDefault(x => x.Id == mConceptId);
        }

        private IQConcept GetIQConcept(int mConceptId)
        {
            var m = GetMConcept(mConceptId);
            if (null != m)
            {
                return m.IQConcepts.FirstOrDefault();
            }
            Log.Debug($"iqc-{mConceptId} Not found !");
            return null;
        }
    }
}