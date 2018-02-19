using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using IQCare.Sync.Core.Events;
using IQCare.Sync.Core.Model;
using IQCare.Sync.Core.Services;

namespace IQCare.Sync.Core.Interfaces
{
    public interface IPatientService : IService<Patient>
    {
        IEncounterService EncounterService { get; set; }
        IEmrService EmrService { get; set; }
        event EventHandler<PatientCreated> PatientCreatedEvent;
        event EventHandler<PatientSaved> PatientSavedEvent;
        void Sync(Patient patient, bool excludeEncounters);
        Task SyncAsync(Patient patient, bool excludeEncounters, CancellationTokenSource cancellationTokenSource);
    }
}