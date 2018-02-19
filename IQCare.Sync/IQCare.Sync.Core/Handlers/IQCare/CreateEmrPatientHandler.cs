using System.Collections.Generic;
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
    public class CreateEmrPatientHandler : ICreateEmrPatientHandler
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private string _notification;
        private Patient _patient;
        private IQLocation _location;
        private List<SqlAction> _sqlActions;
        private string _sqlActionsBatch;
        private IQModule _module;
        private IQVisitType _visittype;


        public Patient Patient
        {
            get { return _patient; }
        }

        public IQLocation Location
        {
            get { return _location; }
        }

        public IQModule Module
        {
            get { return _module; }
        }

        public IQVisitType VisitType
        {
            get { return _visittype; }
        }

        public void Handle(PatientCreated args)
        {

            //TODO: Remove hard codede ModuleID,VisitType,LocationID values
            
            _sqlActions = new List<SqlAction>();
            _patient = args.Patient;
            _location = args.Location;
            _module = args.Module;
            _visittype = args.VisitType;

            _notification = $"Creating Patient[{args.Patient}] Encounters[{args.Patient.Encounters?.Count ?? 0}]";
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
            _sqlActions.Add(new SqlAction(rank, Utility.GetSqlDecrptyion()));rank++;
            _sqlActions.Add(InsertPatient(rank)); rank++;
            _sqlActions.Add(InsertVisit(rank)); rank++;
            _sqlActions.Add(InsertContacts(rank)); rank++;
            _sqlActions.Add(InsertDefualts(rank)); rank++;
            _sqlActions.Add(InsertRegistration(rank)); rank++;
            _sqlActions.Add(UpdateReference(rank));
            _sqlActions.Add(InsertEnrollment(rank));

            StringBuilder sqlBuilder = new StringBuilder(" ");
            foreach (var action in _sqlActions.OrderBy(x => x.Rank))
            {
                sqlBuilder.AppendLine(action.Action);
            }

            _sqlActionsBatch = sqlBuilder.ToString();
        }

        private SqlAction InsertPatient(decimal rank)
        {
            //TODO: ALTER TABLE [dbo].[mst_Patient] ADD [SyncId] [uniqueidentifier] NULL

            string sql = $@"
                DECLARE @ptnpk int
                DECLARE @visitipk int

                UPDATE 
	                [mst_Patient] 
                SET 
	                [Status]='0',
                    [FirstName]=encryptbykey(key_guid('Key_CTC'), '{Patient.Firstname}'),
                    [MiddleName]=encryptbykey(key_guid('Key_CTC'), '{Patient.Middlename}'),
                    [LastName]=encryptbykey(key_guid('Key_CTC'), '{Patient.Lastname}'),    

                    [LocationID]=  (select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),
                    [RegistrationDate]= '{Patient.EnrollmentdateString}',
                    [Sex]= '{Patient.Sex}', 
                    [DOB]= '{Patient.DobString}',
                    [DobPrecision]= '{Patient.GetDobEstimated()}', 

                    [CountryId]=(select TOP 1 CountryID from mst_Facility WHERE DeleteFlag = 0),
                    [PosId]=(select TOP 1 PosID from mst_Facility WHERE DeleteFlag = 0),
                    [SatelliteId]=(select TOP 1 SatelliteID from mst_Facility WHERE DeleteFlag = 0), 
                    [UserID]='{Patient.UserId}', 
                    [UpdateDate]=GETDATE(),
                    [MaritalStatus]='{Patient.Kinrelation}',
                    [HTSOccupation]='{Patient.Kinphone}',
                    [HTSPhysicalAddress]='{Patient.Kin}',

                    [Phone]= encryptbykey(key_guid('Key_CTC'), '{Patient.Contactphone}'),
                    [Client_Code]= '{Patient.Clientcode}'

                WHERE 
	                SyncId='{Patient.UuId}'

                IF @@ROWCOUNT=0

                    INSERT INTO 
                        mst_Patient(
                            Status, FirstName, MiddleName, LastName, 
                            LocationID, RegistrationDate, Sex, DOB, DobPrecision,
                            CountryId, PosId, SatelliteId, UserID, CreateDate,
                            Phone,Client_Code,SyncId,MaritalStatus, HTSOccupation, HTSPhysicalAddress)
                    VALUES(
                        '0', encryptbykey(key_guid('Key_CTC'), '{Patient.Firstname}'), encryptbykey(key_guid('Key_CTC'), '{Patient.Middlename}'), encryptbykey(key_guid('Key_CTC'), '{Patient.Lastname}'), 
                        (select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0), '{Patient.EnrollmentdateString}', '{Patient.Sex}', '{Patient.DobString}', '{Patient.GetDobEstimated()}', 
                        (select TOP 1 CountryID from mst_Facility WHERE DeleteFlag = 0),
                        (select TOP 1 PosID from mst_Facility WHERE DeleteFlag = 0), 
                        (select TOP 1 SatelliteID from mst_Facility WHERE DeleteFlag = 0), '{Patient.UserId}', GETDATE(),
                        encryptbykey(key_guid('Key_CTC'), '{Patient.Contactphone}'),'{Patient.Clientcode}','{Patient.UuId}','{Patient.Kinrelation}','{Patient.Kinphone}','{Patient.Kin}');
                
                SET @ptnpk=(SELECT Ptn_Pk  FROM mst_Patient WHERE SyncId ='{Patient.UuId}');";


            var action = new SqlAction(rank, sql);
            return action;
        }
        private SqlAction InsertVisit(decimal rank)
        {
            //TODO: ALTER TABLE [dbo].[ord_Visit] ADD [IQMobileVisitType] [int] NULL

            string sql = $@"

                UPDATE 
	                [ord_Visit] 
                SET 
	                [Ptn_Pk]=@ptnpk,
                    [LocationID]=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),
                    [VisitDate]='{Patient.EnrollmentdateString}',
                    [VisitType]= 12,
                    [UserID]='{Patient.UserId}',
                    [UpdateDate]=GETDATE(),
                    [IQMobileVisitType]=12
                WHERE 
	                Ptn_pk=@ptnpk AND LocationId=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0) AND IQMobileVisitType=12                
                IF @@ROWCOUNT=0
                    INSERT INTO 
                        ord_Visit(
                            Ptn_Pk, LocationID, VisitDate, VisitType, UserID, CreateDate,IQMobileVisitType)
                    VALUES(
                        @ptnpk,(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0), '{Patient.EnrollmentdateString}', 12, '{Patient.UserId}', GETDATE(),12);
                
                SET @visitipk=(SELECT TOP 1 [Visit_Id] FROM [ord_Visit] WHERE Ptn_Pk=@ptnpk AND IQMobileVisitType=12 ORDER BY CreateDate desc);";

            var action = new SqlAction(rank, sql);
            return action;
        }
        private SqlAction InsertContacts(decimal rank)
        {
            string sql = $@"
                
                UPDATE 
	                [dtl_PatientContacts] 
                SET 
	                [Ptn_Pk]=@ptnpk,
                    [VisitId]=@visitipk,                    
                    [LocationID]=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),
                    [UserID]='{Patient.UserId}',
                    [NextofKinName]='{Patient.Kin}',
                    [NextofKinRelationship]='{Patient.Kinrelationother}',
                    [NextofKinTelNo]='{Patient.Kinphone}',
                    [UpdateDate]=GETDATE()
                WHERE 
	                Ptn_pk=@ptnpk AND VisitId=@visitipk
                IF @@ROWCOUNT=0
                    INSERT INTO 
                        dtl_PatientContacts(
                            ptn_pk, VisitId, LocationID, UserID, CreateDate, NextofKinName, NextofKinRelationship, NextofKinTelNo)
                    VALUES(@ptnpk,@visitipk, 
                       (select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0), {Patient.UserId}, GETDATE(), '{Patient.Kin}', '{Patient.Kinrelationother}', '{Patient.Kinphone}');";

            var action = new SqlAction(rank, sql);
            return action;
        }
        private SqlAction InsertDefualts(decimal rank)
        {
            string sql = $@"

                UPDATE 
	                [DTL_PATIENTHOUSEHOLDINFO] 
                SET 
	                [Ptn_Pk]=@ptnpk,
                    [Visit_Pk]=@visitipk,                    
                    [LocationID]=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),
                    [UserID]='{Patient.UserId}',
                    [UpdateDate]=GETDATE()
                WHERE 
	                Ptn_pk=@ptnpk AND Visit_Pk=@visitipk
                IF @@ROWCOUNT=0

                    Insert into 
	                    [DTL_PATIENTHOUSEHOLDINFO](
		                    Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate)
	                    Values(
		                    @ptnpk,@visitipk, (select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0), {Patient.UserId}, GetDate());


                UPDATE 
	                [DTL_RURALRESIDENCE] 
                SET 
	                [Ptn_Pk]=@ptnpk,
                    [Visit_Pk]=@visitipk,                    
                    [LocationID]=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),
                    [UserID]='{Patient.UserId}',
                    [RuralDistrict]='0',
                    [RuralDivision]='0',
                    [UpdateDate]=GETDATE()
                WHERE 
	                Ptn_pk=@ptnpk AND Visit_Pk=@visitipk
                IF @@ROWCOUNT=0                    
                    Insert into 
	                    [DTL_RURALRESIDENCE](
		                    Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate,[RuralDistrict],[RuralDivision])
	                    Values(
		                    @ptnpk,@visitipk,(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0), {Patient.UserId}, GetDate(),'0','0');
                
               
                UPDATE 
	                [DTL_URBANRESIDENCE] 
                SET 
	                [Ptn_Pk]=@ptnpk,
                    [Visit_Pk]=@visitipk,                    
                    [LocationID]=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),
                    [UserID]='{Patient.UserId}',
                    [UrbanTown]='0',                    
                    [UpdateDate]=GETDATE()
                WHERE 
	                Ptn_pk=@ptnpk AND Visit_Pk=@visitipk
                IF @@ROWCOUNT=0
                    Insert into 
	                    [DTL_URBANRESIDENCE](
		                    Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate,[UrbanTown])
	                    Values(
		                    @ptnpk,@visitipk,(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0), {Patient.UserId}, GetDate(),'0');
                

                UPDATE 
	                [DTL_PATIENTHIVPREVCAREENROLLMENT] 
                SET 
	                [Ptn_Pk]=@ptnpk,
                    [Visit_Pk]=@visitipk,                    
                    [LocationID]=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),
                    [UserID]='{Patient.UserId}',
                    [UpdateDate]=GETDATE()
                WHERE 
	                Ptn_pk=@ptnpk AND Visit_Pk=@visitipk
                IF @@ROWCOUNT=0
                    Insert into 
	                    [DTL_PATIENTHIVPREVCAREENROLLMENT](
		                    Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate)
	                    Values(
		                    @ptnpk,@visitipk, (select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0), {Patient.UserId}, GetDate());
		

                UPDATE 
	                [DTL_PATIENTGUARANTOR] 
                SET 
	                [Ptn_Pk]=@ptnpk,
                    [Visit_Pk]=@visitipk,                    
                    [LocationID]=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),
                    [UserID]='{Patient.UserId}',
                    [Guarantor1Occupation]='0',
                    [Guarantor2Occupation]='0',
                    [UpdateDate]=GETDATE()
                WHERE 
	                Ptn_pk=@ptnpk AND Visit_Pk=@visitipk
                IF @@ROWCOUNT=0
                    Insert into 
	                    [DTL_PATIENTGUARANTOR](
		                    Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate,[Guarantor1Occupation],[Guarantor2Occupation])
	                    Values(
		                    @ptnpk,@visitipk, (select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0), {Patient.UserId}, GetDate(),'0','0');


                UPDATE 
	                [DTL_PATIENTDEPOSITS] 
                SET 
	                [Ptn_Pk]=@ptnpk,
                    [Visit_Pk]=@visitipk,                    
                    [LocationID]=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),
                    [UserID]='{Patient.UserId}',
                    [UpdateDate]=GETDATE()
                WHERE 
	                Ptn_pk=@ptnpk AND Visit_Pk=@visitipk
                IF @@ROWCOUNT=0
                    Insert into 
	                    [DTL_PATIENTDEPOSITS](
		                    Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate)
	                    Values(
		                    @ptnpk,@visitipk,(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0), {Patient.UserId}, GetDate());
		

                UPDATE 
	                [DTL_PATIENTINTERVIEWER] 
                SET 
	                [Ptn_Pk]=@ptnpk,
                    [Visit_Pk]=@visitipk,                    
                    [LocationID]=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),
                    [UserID]='{Patient.UserId}',
                    [UpdateDate]=GETDATE()
                WHERE 
	                Ptn_pk=@ptnpk AND Visit_Pk=@visitipk
                IF @@ROWCOUNT=0
                    Insert into 
	                    [DTL_PATIENTINTERVIEWER](
		                    Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate)
	                    Values(
		                    @ptnpk,@visitipk,(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0), {Patient.UserId}, GetDate());";

            var action = new SqlAction(rank, sql);
            return action;
        }
        private SqlAction InsertRegistration(decimal rank)
        {
            string sql = $@"
                if exists(
	                select name from sysobjects where name = 'DTL_FBCUSTOMFIELD_Patient_Registration') 
                begin 
                    
                    UPDATE 
	                    [DTL_FBCUSTOMFIELD_Patient_Registration] 
                    SET 
	                    [Ptn_Pk]=@ptnpk,
                        [Visit_Pk]=@visitipk,                    
                        [LocationID]=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),
                        [UserID]='{Patient.UserId}',
                        [UpdateDate]=GETDATE()
                    WHERE 
	                    Ptn_pk=@ptnpk AND Visit_Pk=@visitipk
                    IF @@ROWCOUNT=0
	                    Insert into 
		                    [DTL_FBCUSTOMFIELD_Patient_Registration](
			                    Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate)
		                    Values(
			                    @ptnpk,@visitipk,(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),{Patient.UserId}, GetDate()) 
                end ;";
            var action = new SqlAction(rank, sql);
            return action;
        }
        private SqlAction UpdateReference(decimal rank)
        {
            string sql = $@"
                update 
	                mst_patient set IQNumber = 'IQ-'+convert(varchar,Replicate('0',20-len(x.[ptnIdentifier]))) +x.[ptnIdentifier]  
                from (
	                select 
		                UPPER(substring(convert(varchar(50),decryptbykey(firstname)),1,1))+UPPER(substring(convert(varchar(50),decryptbykey(lastname)),1,1))+convert(varchar,dob,112)+convert(varchar,locationid)+Convert(varchar(10),ptn_pk) [ptnIdentifier] 
	                from 
		                mst_patient where ptn_pk = @ptnpk)x 
	                where ptn_pk= @ptnpk;";

            var action = new SqlAction(rank, sql);
            return action;
        }
        private SqlAction InsertEnrollment(decimal rank)
        {       
            string sql = $@"

                UPDATE 
	                [ord_Visit] 
                SET 
	                [Ptn_Pk]=@ptnpk,
                    [LocationID]=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),
                    [VisitDate]='{Patient.EnrollmentdateString}',
                    [VisitType]= {VisitType.VisitTypeID},
                    [DataQuality]=0,
                    [UserID]='{Patient.UserId}',
                    [UpdateDate]=GETDATE(),
                    [IQMobileVisitType]={VisitType.VisitTypeID}
                WHERE 
	                Ptn_pk=@ptnpk AND LocationId=(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0) AND IQMobileVisitType={VisitType.VisitTypeID}                
                IF @@ROWCOUNT=0
                    Insert into 
	                    ord_visit(
		                    Ptn_Pk,LocationID,VisitDate,VisitType,DataQuality,DeleteFlag,UserID,CreateDate,IQMobileVisitType)
	                    values (
		                    @ptnpk,(select TOP 1 FacilityID from mst_Facility WHERE DeleteFlag = 0),'{Patient.EnrollmentdateString}',{VisitType.VisitTypeID},0,0,{Patient.UserId}, Getdate(),{VisitType.VisitTypeID});



                UPDATE 
	                [lnk_patientprogramstart] 
                SET 
	                [Ptn_Pk]=@ptnpk,
                    [ModuleID]={Module.ModuleID},
                    [StartDate]='{Patient.EnrollmentdateString}',
                    [UserID]='{Patient.UserId}',
                    [UpdateDate]=GETDATE()
                WHERE 
	                Ptn_pk=@ptnpk AND ModuleID={Module.ModuleID}            
                IF @@ROWCOUNT=0                  
                    Insert into 
	                    lnk_patientprogramstart(
		                    Ptn_Pk,ModuleID,StartDate,UserID,CreateDate)
	                    values (
		                    @ptnpk, {Module.ModuleID},'{Patient.EnrollmentdateString}',{Patient.UserId}, Getdate());";


            var action = new SqlAction(rank, sql);
            return action;
        }
    }
}
