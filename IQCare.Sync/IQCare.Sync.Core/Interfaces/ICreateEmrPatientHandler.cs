using System.Collections.Generic;
using IQCare.Sync.Core.Events;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Interfaces
{
    public interface ICreateEmrPatientHandler : IHandle<PatientCreated>
    {
        Patient Patient { get; }
        IQLocation Location { get; }
        IQModule Module { get; }
        IQVisitType VisitType { get;  }
        List<SqlAction> GetSqlActions();
        string GetSqlActionsBatch();
    }
}