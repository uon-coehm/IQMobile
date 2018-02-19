using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Events;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.UoW;
using IQCare.Sync.Core.Model;
using log4net;

namespace IQCare.Sync.Core.Services
{
    public class EncounterService : IEncounterService
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private IUnitOfWork _uow;
        private readonly IPatientService _patientService;


        public IPatientService PatientService
        {
            get { return _patientService; }
        }

        public IEmrService EmrService { get; set; }        
        public event EventHandler<EncounterCreated> EncounterCreatedEvent;

        public EncounterService(IUnitOfWork uow,IPatientService patientService)
        {
            _uow = uow;
            _patientService = patientService;
            _patientService.EncounterService = this;
            _patientService.PatientSavedEvent += _patientService_PatientSavedEvent;
        }

        private void _patientService_PatientSavedEvent(object sender, PatientSaved e)
        {
            Sync(e.Encounters,e.Patient);
        }

        private List<MConcept> GetEncounterMConpcets(int encountertypeId)
        {
            return _uow.MConceptRepository.GetAllByEncounterType(encountertypeId).ToList();
        }

        public IEnumerable<Encounter> GetAll()
        {
            return _uow.EncounterRepository.GetAll();
        }
        public void Sync(Encounter entity)
        {
            throw new NotImplementedException();
        }

        public EncounterType GetEncounterType(int id)
        {
            return _uow.EncounterTypeRepository.FindById(id);
        }

        public List<LookupHTS> GetLookupHts()
        {
            return _uow.LookupHtsRepository.GetAll().ToList();
        }

        public string VerifyConcepts(Encounter encounter)
        {
            List<Observation> delObs = new List<Observation>();
            var sb=new StringBuilder();

            if (null != encounter)
            {

                var mconcepts = _uow.MConceptRepository.GetAllByEncounterType(encounter.EncounterTypeId).ToList();
                foreach (var obs in encounter.Observations)
                {
                    var found = mconcepts.FirstOrDefault(x => x.Id == obs.MConceptId);

                    if (null == found)
                    {
                        sb.AppendLine($"{obs.MConceptId}");
                        delObs.Add(obs);
                    }
                }
                foreach (var toremove in delObs)
                {
                    encounter.Observations.Remove(toremove);

                    Log.Debug($"Removed missing obs {toremove.Id} with concept {toremove.MConceptId}");

                }
            }


            var summary = sb.ToString();
            if (!string.IsNullOrWhiteSpace(summary))
            {
                throw new Exception($"Concepts with id(s) Not found :{summary}");
            }
            return summary;
        }

        public void Sync(IEnumerable<Encounter> encounters, Patient patient)
        {
            foreach (var encounter in encounters)
            {
                try
                {
                    VerifyConcepts(encounter);
                }
                catch (Exception ex)
                {
                    Log.Debug(ex);
                }
                

                if (null != encounter)
                {
                    Encounter existingEncounter = null;
                    try
                    {
                        existingEncounter = _uow.EncounterRepository.FindBySyncId(encounter.UuId);
                    }
                    catch (Exception ex)
                    {
                        Log.Debug($"Error Searching for encounter {encounter}");
                        Log.Debug(ex);
                    }

                    if (null != existingEncounter)
                    {
                        var encounterNew = Encounter.CreateFrom(encounter);
                        try
                        {
                            _uow.EncounterRepository.Delete(existingEncounter.UuId);
                            _uow.Commit();
                        }
                        catch (Exception ex)
                        {
                            Log.Debug($"Error updating encounter {encounter}");
                            Log.Debug(ex);
                        }
                        encounterNew.PatientId = patient.Id;
                        encounterNew.Patient = null;
                        encounterNew.EncounterTypeId = encounter.EncounterTypeId;
                        encounterNew.EncounterType = null;
                        try
                        {
                            _uow.EncounterRepository.Save(encounterNew);
                            _uow.Commit();
                        }
                        catch (Exception ex)
                        {
                            Log.Debug(ex);
                        }
                        OnEncounterCreatedEvent(
                            new EncounterCreated(
                                patient,
                                EmrService.GetVisitType(GetEncounterType(encounterNew.EncounterTypeId).IqcareId.Value),
                                encounterNew,
                                EmrService.GetLocation(),
                                GetEncounterMConpcets(encounterNew.EncounterTypeId),
                                GetLookupHts()
                                )
                            );
                    }
                    else
                    {
                        encounter.PatientId = patient.Id;
                        encounter.Patient = null;
                        encounter.EncounterTypeId = encounter.EncounterTypeId;
                        encounter.EncounterType = null;
                        foreach (var o in encounter.Observations)
                        {
                            o.MConcept = null;
                        }
                        try
                        {
                            _uow.EncounterRepository.Save(encounter);
                            _uow.Commit();
                        }
                        catch (Exception ex)
                        {
                            Log.Debug($"Error saving NEW encounter {encounter}");
                            Log.Debug(ex);

                        }
                        OnEncounterCreatedEvent(
                            new EncounterCreated(
                                patient,
                                EmrService.GetVisitType(GetEncounterType(encounter.EncounterTypeId).IqcareId.Value),
                                encounter, EmrService.GetLocation(),
                                GetEncounterMConpcets(encounter.EncounterTypeId),
                                GetLookupHts()
                                )
                            );
                    }
                }
            }
        }

        public void Delete(Guid uuid)
        {
            _uow.EncounterRepository.Delete(uuid);
        }

        
        public void SaveChanges()
        {
            _uow.Commit();
        }


        protected virtual void OnEncounterCreatedEvent(EncounterCreated e)
        {
            EncounterCreatedEvent?.Invoke(this, e);
        }
        
    }
}
