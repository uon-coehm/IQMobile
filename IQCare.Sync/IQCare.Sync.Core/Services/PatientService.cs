using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using IQCare.Sync.Core.Events;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Interfaces.UoW;
using IQCare.Sync.Core.Model;
using log4net;
using StructureMap.Attributes;

namespace IQCare.Sync.Core.Services
{
    public class PatientService : IPatientService
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private IUnitOfWork _uow;
       
        public IEncounterService EncounterService { get; set; }
        public IEmrService EmrService { get; set; }
        
        public event EventHandler<PatientCreated> PatientCreatedEvent;
        public event EventHandler<PatientSaved> PatientSavedEvent;


        public PatientService(IUnitOfWork uow, IEmrRepository emrRepository, ICreateEmrPatientHandler createEmrPatientHandler, ICreateEmrEncounterHandler createEmrEncounterHandler)
        {
            _uow = uow;
            EncounterService = new EncounterService(uow,this);
            EmrService = new IQCareEmrService(this, emrRepository, createEmrPatientHandler, createEmrEncounterHandler);
            EncounterService.EmrService = EmrService;
        }

        public IEnumerable<Patient> GetAll()
        {
            return _uow.PatientRepository.GetAll();
        }

        public void Sync(Patient entity)
        {
            Sync(entity,false);
        }

        public void Sync(Patient patient, bool excludeEncounters)
        {
            Patient existingPatient = null;

            if (null == patient)
            {
                return;
            }

            var encounters = patient.Encounters.ToList();

            try
            {
                existingPatient = _uow.PatientRepository.FindBySyncId(patient.UuId);
            }
            catch (Exception ex)
            {
                Log.Debug($"Error Searching for patient {patient}");
                Log.Debug(ex);
            }


            if (null != existingPatient)
            {
                existingPatient.UpdateFrom(patient);
                //existingPatient.Encounters = null;
                try
                {
                    _uow.PatientRepository.Update(existingPatient);
                    _uow.Commit();
                }
                catch (Exception ex)
                {
                    Log.Debug($"Error updating patient {patient}");
                    Log.Debug(ex);
                }
                
                patient = existingPatient;
                patient.Encounters = null;
                Log.Debug($"UPDATING patient demographics {patient}");

                OnPatientCreatedEvent(new PatientCreated(existingPatient, EmrService.GetLocation()));
            }
            else
            {
                patient.Encounters = null;
                try
                {
                    _uow.PatientRepository.Save(patient);
                    _uow.Commit();
                }
                catch (Exception ex)
                {
                    Log.Debug($"Error saving NEW patient {patient}");
                    Log.Debug(ex);
                }
                Log.Debug($"CREATING patient demographics {patient}");
                OnPatientCreatedEvent(new PatientCreated(patient, EmrService.GetLocation()));
            }

            if (excludeEncounters)
            {
                Log.Debug($"Excluding encounters...");
                return;
            }

            Log.Debug($"PROCESSING encounters {patient}");
            OnPatientSavedEvent(new PatientSaved(patient,encounters));
        }

        private Task SyncTask(Patient patient, bool excludeEncounters,CancellationToken cancellationToken)
        {
            return Task.Run(() =>
            {
                cancellationToken.ThrowIfCancellationRequested();
                Sync(patient, excludeEncounters);
            },
              cancellationToken);
        }
        public async Task SyncAsync(Patient patient, bool excludeEncounters, CancellationTokenSource cancellationTokenSource)
        {
            await SyncTask(patient, excludeEncounters, cancellationTokenSource.Token);
        }

        public void Delete(Guid uuid)
        {
            _uow.PatientRepository.Delete(uuid);
        }

        public void SaveChanges()
        {
            _uow.Commit();
        }

        protected virtual void OnPatientCreatedEvent(PatientCreated e)
        {
            PatientCreatedEvent?.Invoke(this, e);
        }

        protected virtual void OnPatientSavedEvent(PatientSaved e)
        {
            PatientSavedEvent?.Invoke(this, e);
        }
    }
}
