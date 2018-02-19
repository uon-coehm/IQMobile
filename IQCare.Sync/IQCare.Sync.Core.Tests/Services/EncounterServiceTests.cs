

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using IQCare.Sync.Core.Handlers.IQCare;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Interfaces.UoW;
using IQCare.Sync.Core.Model;
using IQCare.Sync.Core.Services;
using IQCare.Sync.Infrastructure;
using IQCare.Sync.Infrastructure.Repository;
using IQCare.Sync.Shared.Tests.TestData;
using NUnit.Framework;

namespace IQCare.Sync.Core.Tests.Services
{
    [TestFixture]
    public class EncounterServiceTests
    {
        private readonly Factory _factory = new Factory();
        private IUnitOfWork _uow;
        private IEmrRepository _emrRepository;
        private ICreateEmrPatientHandler _createEmrPatientHandler;
        private ICreateEmrEncounterHandler _createEmrEncounterHandler;

        private List<Encounter> _testEncounters=new List<Encounter>();
        private IEncounterService _encounterService;
        private PatientService _patientService;
        private List<Guid> uuids = new List<Guid>();
        private Patient _patient;
        private IQPatient _iqPatient;

        [SetUp]
        public void Setup()
        {
            _testEncounters = _factory.GenerateTestEncountersByType(1);
            _patient = _testEncounters.First().Patient;
            _uow = new UnitOfWork(new SyncContext());
            _emrRepository = new EmrRepository();

            _createEmrPatientHandler = new CreateEmrPatientHandler();
            _createEmrEncounterHandler = new CreateEmrEncounterHandler();

            _patientService = new PatientService(_uow, _emrRepository, _createEmrPatientHandler, _createEmrEncounterHandler);
            _encounterService = _patientService.EncounterService;

            _patientService.Sync(_patient, true);
            _iqPatient=  new EmrRepository().GetPatient(_patient.UuId);
            uuids = new List<Guid>();
        }

        [Test]
        public void should_GetAll()
        {
            Assert.That(_testEncounters, Is.Not.Empty);
            List<int> ids = _testEncounters.Select(x => x.Id).ToList();

            var encounters = _encounterService.GetAll().Where(x => ids.Contains(x.Id));
            Assert.That(encounters, Is.Not.Empty);
            foreach (var encounter in encounters)
            {
                Debug.Print(encounter.ToString());
                Assert.That(encounter.Observations, Is.Not.Empty);
                foreach (var obs in encounter.Observations)
                {
                    Debug.Print($"   >.{obs.ToString()}");
                }
            }
        }

        [Test]
        public void should_verify_concepts()
        {
            var encounter = _testEncounters.First();

            var summary = _encounterService.VerifyConcepts(encounter);
            Assert.IsNullOrEmpty(summary);

            encounter.Observations.First().MConceptId = -1;


            var ex = Assert.Throws<Exception>(() => _encounterService.VerifyConcepts(encounter));
            Assert.That(ex.Message, Is.StringContaining("Concepts with id(s) Not found"));
            Debug.Print(ex.Message);
        }

        [Test]
        public void should_sync_new_Encounter()
        {
            var encounters = new List<Encounter>();
            var patient = _testEncounters.First().Patient;
            var encounter = _testEncounters.First();
            encounters.Add(encounter);

            _encounterService.Sync(encounters,patient);

            _uow = new UnitOfWork(new SyncContext());
            _patientService = new PatientService(_uow, _emrRepository, _createEmrPatientHandler, _createEmrEncounterHandler);
            _encounterService = _patientService.EncounterService;
            var savedEncounter = _encounterService.GetAll().First(x => x.UuId == encounter.UuId);
            Assert.That(savedEncounter, Is.Not.Null);
            Debug.Print(savedEncounter.ToString());
            foreach (var obs in savedEncounter.Observations)
            {
                Debug.Print($"   >.{obs.ToString()}");
            }
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [ord_Visit] where Ptn_Pk in ({_iqPatient.Id}) AND VisitType=219"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({_iqPatient.Id})"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [dtl_fb_MARPsHTC] where Ptn_Pk in ({_iqPatient.Id})"), Is.EqualTo(2));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [dtl_fb_DisabilityHTC] where Ptn_Pk in ({_iqPatient.Id})"), Is.EqualTo(3));
            Assert.That(_emrRepository.ExecuteQueryStringResult($"select remarks  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({_iqPatient.Id})"), Is.StringContaining("No remark"));
        }

        [Test]
        public void should_sync_existing_Encounter()
        {
            var encounters = new List<Encounter>();
            var patient = _testEncounters.First().Patient;
            var encounter = _testEncounters.First();
            encounters.Add(encounter);

            _encounterService.Sync(encounters, patient);

            _uow = new UnitOfWork(new SyncContext());
            _patientService = new PatientService(_uow, _emrRepository, _createEmrPatientHandler, _createEmrEncounterHandler);
            _encounterService = _patientService.EncounterService;
            var savedEncounter = _encounterService.GetAll().First(x => x.UuId == encounter.UuId);
            Assert.That(savedEncounter, Is.Not.Null);
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [ord_Visit] where Ptn_Pk in ({_iqPatient.Id}) AND VisitType=219"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({_iqPatient.Id})"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQueryStringResult($"select remarks  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({_iqPatient.Id})"), Is.StringContaining("No remark"));

            foreach (var o in savedEncounter.Observations)
            {
                if (o.MConceptId == 16)
                {
                    o.ValueText = "color blind left eye";
                }
                if (o.MConceptId == 48)
                {
                    o.ValueText = "Screen for TB2";
                }
            }
            var updateEncounters = new List<Encounter>(); updateEncounters.Add(savedEncounter);
            _encounterService.Sync(updateEncounters, patient);
            
            var emrRepository = new EmrRepository();
            Assert.That(emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [ord_Visit] where Ptn_Pk in ({_iqPatient.Id}) AND VisitType=219"), Is.EqualTo(1));
            Assert.That(emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({_iqPatient.Id})"), Is.EqualTo(1));
            Assert.That(emrRepository.ExecuteQueryStringResult($"select remarks  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({_iqPatient.Id})"), Is.StringContaining("Screen for TB2"));

        }

        [TearDown]
        public void TearDown()
        {
            _factory.UoW = new UnitOfWork(new SyncContext());
            _factory.CleanUpTestEncountersByType();
            _factory.CleanIQCareTestPatients(_iqPatient.Id);
        }
    }
}
