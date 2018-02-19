using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using IQCare.Sync.Core.Events;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Interfaces
{
    public interface IEncounterService : IService<Encounter>
    {
        IPatientService PatientService { get;  }
        IEmrService EmrService { get; set; }
        EncounterType GetEncounterType(int id);
        List<LookupHTS> GetLookupHts();
        string VerifyConcepts(Encounter encounter);
        void Sync(IEnumerable<Encounter> encounters,Patient patient);
        
        event EventHandler<EncounterCreated> EncounterCreatedEvent;
    }
}