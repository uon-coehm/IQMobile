using System.Collections.Generic;
using IQCare.Sync.Core.Events;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Interfaces
{
    public interface ICreateEmrEncounterHandler : IHandle<EncounterCreated>
    {
        Patient Patient { get; }
        Encounter Encounter { get; }
        IQVisitType VisitType { get; set; }
        IQLocation Location { get; }
        List<SqlAction> GetSqlActions();
        List<MConcept> MConcepts { get; set; }

        List<LookupHTS> LookupsHts { get; set; }

        string GetSqlActionsBatch();
    }
}