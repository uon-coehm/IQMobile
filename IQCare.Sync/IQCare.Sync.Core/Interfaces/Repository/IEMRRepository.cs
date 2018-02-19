using System;
using System.CodeDom;
using System.Collections.Generic;
using System.Threading.Tasks;
using IQCare.Sync.Core.Handlers;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Interfaces.Repository
{
    public interface IEmrRepository: IDisposable
    {
        IQModule GetModule(int moduleId);
        IQFeature GetFeature(int featureId);
        IQVisitType GetVisitType(int visitTypeId);
        IQVisitType GetVisitTypeByFeature(int featureId);
        IQLocation GetLocation(int locationId);
        IQPatient GetPatient(int id);
        IQPatient GetPatient(Guid syncid);
        void Initialize();
        void CreatePatient(List<SqlAction> createPatientActions,Guid syncid);
        void CreateEncounter(List<SqlAction> createEncounterActions, Guid syncid);
        int ExecuteQuery(string sql);
        string ExecuteQueryStringResult(string sql);
        void ExecuteCommand(string sql);
        void ExecuteCommand(string sql, bool tx);
    }
}