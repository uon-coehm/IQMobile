

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
using IQCare.Sync.Wapi.Controllers;
using NUnit.Framework;

namespace IQCare.Sync.Wapi.Tests.Controllers
{

    [TestFixture]
    public class PatientControllerTest : BaseControllerTest
    {
        private readonly Factory _factory = new Factory();
        private IUnitOfWork _uow;
        private IEmrRepository _emrRepository;
        private ICreateEmrPatientHandler _createEmrPatientHandler;
        private ICreateEmrEncounterHandler _createEmrEncounterHandler;

        private List<Patient> _testPatients;
        private IPatientService _patientService;
        private IEncounterService _encounterService;
        private List<Guid> uuids;
        private List<Module> _testModules;
        private List<Patient> _testPatientWithEncounters;
        
        private int testIQPatientId;
        private IEmrService _emrService;
        private Patient _patientWithEncounter;

        [SetUp]
        public void Setup()
        {
            uuids = new List<Guid>();
            testIQPatientId = -1;
            

            _testPatients = _factory.GenerateTestPatients();
            _patientWithEncounter = _factory.GenerateTestEncountersByType(1).First().Patient;
            _uow = new UnitOfWork(new SyncContext());
            _emrRepository = new EmrRepository();
            _createEmrPatientHandler = new CreateEmrPatientHandler();
            _createEmrEncounterHandler=new CreateEmrEncounterHandler();

            _patientService = new PatientService(_uow,_emrRepository,_createEmrPatientHandler,_createEmrEncounterHandler);
            _encounterService = _patientService.EncounterService;


            _emrService = _patientService.EmrService;
            
        }

        [Test]
        public void should_Get()
        {          
            // Arrange
            Assert.That(_testPatients, Is.Not.Empty);
            List<int> ids = _testPatients.Select(x => x.Id).ToList();
            var controller = new PatientController(_patientService);

            // Act
            var patients = controller.Get().Where(x => ids.Contains(x.Id));

            // Assert
            Assert.That(patients, Is.Not.Empty);
            foreach (var patient in patients)
            {
                Debug.Print(patient.ToString());
            }
        }
        
        [Test]
        public void should_Post()
        {
            // Arrange                        
            Assert.That(_patientWithEncounter, Is.Not.Null);
            var controller = new PatientController(_patientService);

            // Act
            controller.Post(PateintDTO.Create(_patientWithEncounter));

            // Assert
            _uow = new UnitOfWork(new SyncContext());
            _patientService = new PatientService(_uow, new EmrRepository(), _createEmrPatientHandler, _createEmrEncounterHandler);
            var newPatient= _patientService.GetAll().FirstOrDefault(x => x.UuId == _patientWithEncounter.UuId);
            Assert.NotNull(newPatient);
            Assert.AreEqual(_patientWithEncounter.Lastname,newPatient.Lastname);
            Debug.Print(newPatient.ToString());
            var iqpatient = _emrRepository.GetPatient(newPatient.UuId);
            Assert.NotNull(iqpatient);
            testIQPatientId = iqpatient.Id;
            Assert.That(iqpatient.FirstName, Is.EqualTo(_patientWithEncounter.Firstname));
            Assert.That(iqpatient.MiddleName, Is.EqualTo(_patientWithEncounter.Middlename));
            Assert.That(iqpatient.LastName, Is.EqualTo(_patientWithEncounter.Lastname));
            Assert.That(iqpatient.SyncId, Is.EqualTo(_patientWithEncounter.UuId));
            Debug.Print($"IQCare:{iqpatient.ToStringDetail()}");
        }

       

        [TearDown]
        public void TearDown()
        {
            _factory.UoW = new UnitOfWork(new SyncContext());
            _factory.CleanUpTestPatients();
            _factory.CleanUpTestEncountersByType();
            _factory.CleanIQCareTestPatients(testIQPatientId);
        }
    }
}
