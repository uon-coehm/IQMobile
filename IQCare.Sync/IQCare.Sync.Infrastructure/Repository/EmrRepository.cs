using System;
using System.CodeDom;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Model;
using IQCare.Sync.Shared;
using log4net;

namespace IQCare.Sync.Infrastructure.Repository
{
    // ReSharper disable once InconsistentNaming


    public class EmrRepository : IEmrRepository
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private readonly EMRContext _emrContext;
        private readonly IQLocation _currentLocation;

        private IQLocation CurrentLocation
        {
            get { return _currentLocation; }
        }

        public EmrRepository()
        {
            _emrContext = new EMRContext();
        }


        public IQModule GetModule(int moduleId)
        {
            var module = _emrContext.Modules.Find(moduleId);

            if (module.Features.Count == 0)
            {
                var features = _emrContext.Features
                    .Where(x => x.ModuleId == moduleId);

                module.AddFeatures(features);
            }
            return module;
        }

        public IQFeature GetFeature(int featureId)
        {
            var feature = _emrContext.Features.Find(featureId);
            if (feature.VisitTypes.Count == 0)
            {
                var visittypes = _emrContext.VisitTypes
                    .Where(x => x.FeatureId == featureId);

                feature.AddVisitTypes(visittypes);
            }
            return feature;
        }

        public IQVisitType GetVisitType(int visitTypeId)
        {
            var visitType = _emrContext.VisitTypes.Find(visitTypeId);
            if (visitType.Feature != null && visitType.FeatureId.HasValue)
            {
                var feature = GetFeature(visitType.FeatureId.Value);
                if (feature.Module != null && feature.ModuleId > 0)
                {
                    var module = GetModule(feature.ModuleId);
                    feature.Module = module;

                }
                visitType.Feature = feature;
            }
            return visitType;
        }

        public IQVisitType GetVisitTypeByFeature(int featureId)
        {
            return GetFeature(featureId).VisitTypes.FirstOrDefault();
        }

        public IQLocation GetLocation(int locationId)
        {
            var location = _emrContext.Locations.Find(locationId);
            return location;
        }

        public IQPatient GetPatient(int id)
        {
            var patients =
                _emrContext.Patients.SqlQuery(
                    $@"
                Open symmetric key Key_CTC decryption by password=N'{Utility.DbSecurity
                        }';
                SELECT  * FROM [HTCPatientView]
                WHERE Id={id}").ToList();

            return patients.FirstOrDefault();
        }

        public IQPatient GetPatient(Guid syncid)
        {
            var patients =
                _emrContext.Patients.SqlQuery(
                    $@"
                Open symmetric key Key_CTC decryption by password=N'{Utility.DbSecurity
                        }';
                SELECT  * FROM [HTCPatientView]
                WHERE syncid='{syncid}'")
                    .ToList();

            return patients.FirstOrDefault();
        }

        public void Initialize()
        {
            /*
         Declare @SymKey varchar(8000)                                    
         Set @SymKey = 'Open symmetric key Key_CTC decryption by password=N'+@password
         Exec(@SymKey)
            */
        }

        public void CreatePatient(List<SqlAction> createPatientActions, Guid syncid)
        {
            
            StringBuilder sqlBuilder = new StringBuilder();
            foreach (var a in createPatientActions.OrderBy(x => x.Rank))
            {
                sqlBuilder.AppendLine(a.Action);
            }
            Log.Debug("creating patient");
            ExecuteCommand(sqlBuilder.ToString(),false);
        }

        public void CreateEncounter(List<SqlAction> createEncounterActions, Guid syncid)
        {
            StringBuilder sqlBuilder = new StringBuilder();
            foreach (var a in createEncounterActions.OrderBy(x => x.Rank))
            {
                sqlBuilder.AppendLine(a.Action);
            }
            Log.Debug("creating patient encounters");
            ExecuteCommand(sqlBuilder.ToString(),false);
        }

        public int ExecuteQuery(string sql)
        {
            var result = _emrContext.Database.SqlQuery<int>(sql).Single();
            return result;
        }

        public string ExecuteQueryStringResult(string sql)
        {
            var result = _emrContext.Database.SqlQuery<string>(sql).Single();
            return result;
        }

        public void ExecuteCommand(string sql)
        {
            try
            {
                _emrContext.Database.ExecuteSqlCommand(TransactionalBehavior.EnsureTransaction, sql);
            }
            catch (Exception ex)
            {
                Log.Debug(new String('-', 10));
                Log.Debug("Error executing sql");
                Log.Debug(ex);
                Log.Debug(sql);
                Log.Debug(new String('-', 10));
            }

        }

        public void ExecuteCommand(string sql, bool tx)
        {
            if (tx)
            {
                ExecuteCommand(sql);
            }
            else
            {
                try
                {
                    _emrContext.Database.ExecuteSqlCommand(TransactionalBehavior.DoNotEnsureTransaction, sql);
                }
                catch (Exception ex) 
                {
                    Log.Debug(new String('-', 10));
                    Log.Debug("Error executing sql");
                    Log.Debug(ex);
                    Log.Debug(sql);
                    Log.Debug(new String('-', 10));
                }                
            }
        }

        public void Dispose()
        {
            if (null != _emrContext)
            {
                _emrContext.SaveChanges();
                _emrContext.Dispose();
            }
        }
    }
}
