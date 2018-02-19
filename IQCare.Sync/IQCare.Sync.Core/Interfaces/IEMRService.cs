using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using IQCare.Sync.Core.Events;
using IQCare.Sync.Core.Handlers;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Interfaces
{
    public interface IEmrService
    {
        IPatientService PatientService { get; }
        IEncounterService EncounterService { get; }

        IEmrRepository EmrRepository { get; set; }
        ICreateEmrPatientHandler CreateEmrPatientHandler { get; set; }
        ICreateEmrEncounterHandler CreateEmrEncounterHandler { get; set; }

        IQLocation GetLocation();      
        IQVisitType GetVisitType(int featureId);
        void CreatePatient(PatientCreated args);
        void CreateEncounter(EncounterCreated args);
    }
}