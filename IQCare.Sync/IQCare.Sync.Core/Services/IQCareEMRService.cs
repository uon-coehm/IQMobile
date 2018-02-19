using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Events;
using IQCare.Sync.Core.Handlers;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Model;
using log4net;
using StructureMap;
using StructureMap.Attributes;

namespace IQCare.Sync.Core.Services
{
    // ReSharper disable once InconsistentNaming
    public class IQCareEmrService : IEmrService
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        //TODO: load Location from settings
        private readonly int locationId = 755;
        private IPatientService _patientService;
        private IEncounterService _encounterService;

        public IPatientService PatientService
        {
            get { return _patientService; }
        }

        public IEncounterService EncounterService
        {
            get { return _encounterService; }
        }

        public IEmrRepository EmrRepository { get; set; }
        public ICreateEmrPatientHandler CreateEmrPatientHandler { get; set; }
        public ICreateEmrEncounterHandler CreateEmrEncounterHandler { get; set; }

        public IQCareEmrService(IPatientService patientService, IEmrRepository emrRepository,
            ICreateEmrPatientHandler createEmrPatientHandler, ICreateEmrEncounterHandler createEmrEncounterHandler)
        {
            _patientService = patientService;
            _patientService.EmrService = this;
            _patientService.EncounterService.EmrService = this;
            _encounterService = patientService.EncounterService;

            EmrRepository = emrRepository;

            CreateEmrPatientHandler = createEmrPatientHandler;
            CreateEmrEncounterHandler = createEmrEncounterHandler;

            _patientService.PatientCreatedEvent += PatientService_PatientCreatedEvent;
            _encounterService.EncounterCreatedEvent += EncounterService_EncounterCreatedEvent;

        }

        public IQLocation GetLocation()
        {
            return EmrRepository.GetLocation(locationId);
        }

        public IQVisitType GetVisitType(int featureId)
        {
            return EmrRepository.GetVisitTypeByFeature(featureId);
        }

        protected virtual void PatientService_PatientCreatedEvent(object sender, PatientCreated e)
        {
            CreatePatient(e);
        }

        protected virtual void EncounterService_EncounterCreatedEvent(object sender, EncounterCreated e)
        {
            CreateEncounter(e);
        }


        public void CreatePatient(PatientCreated args)
        {
            CreateEmrPatientHandler.Handle(args);
            EmrRepository.CreatePatient(CreateEmrPatientHandler.GetSqlActions(), args.Patient.UuId);
        }

        public void CreateEncounter(EncounterCreated args)
        {
            CreateEmrEncounterHandler.Handle(args);
            EmrRepository.CreateEncounter(CreateEmrEncounterHandler.GetSqlActions(), args.Patient.UuId);
        }
    }
}
