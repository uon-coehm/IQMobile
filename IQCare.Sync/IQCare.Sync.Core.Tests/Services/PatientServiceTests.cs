

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
    public class PatientServiceTests
    {
        private readonly Factory _factory = new Factory();

        private IUnitOfWork _uow;
        private IEmrRepository _emrRepository;
        private ICreateEmrPatientHandler _createEmrPatientHandler;
        private ICreateEmrEncounterHandler _createEmrEncounterHandler;

        private IPatientService _patientService;
        private IEncounterService _encounterService;

        private IEmrService _emrService;

        

        private List<Patient> _testPatients = new List<Patient>();
        private List<Patient> _testPatientWithEncounters = new List<Patient>();
        private List<Encounter> _testEncounters = new List<Encounter>();
        private List<Guid> uuids=new List<Guid>();
        
        private int testIQPatientId;

        [SetUp]
        public void Setup()
        {
            testIQPatientId = -1;

            _testPatients = _factory.GenerateTestPatients();
            _testPatientWithEncounters = _factory.GenerateTestPatientsWithEncounters();
            _testEncounters = _factory.GenerateTestEncountersByType(1);

            _uow = new UnitOfWork(new SyncContext());
            _emrRepository = new EmrRepository();

            _createEmrPatientHandler = new CreateEmrPatientHandler();
            _createEmrEncounterHandler = new CreateEmrEncounterHandler();

            _patientService = new PatientService(_uow,_emrRepository,_createEmrPatientHandler,_createEmrEncounterHandler);
            _encounterService = _patientService.EncounterService;
            _emrService = _patientService.EmrService;

            uuids = new List<Guid>();
        }

        [Test]
        public void should_GetAll()
        {
            Assert.That(_testPatients, Is.Not.Empty);
            List<int> ids = _testPatients.Select(x => x.Id).ToList();

            var patients = _patientService.GetAll().Where(x => ids.Contains(x.Id));
            Assert.That(patients, Is.Not.Empty);
            foreach (var patient in patients)
            {
                Debug.Print(patient.ToString());
            }

            Debug.Print("---------------------------------------------");
            foreach (var patient in _patientService.GetAll())
            {
                Debug.Print(patient.ToString());
            }
        }

        [Test]
        public void should_sync_new_patient()
        {
            var patient = new Patient();
            patient.Firstname = "Frank1";
            patient.Middlename = "L";
            patient.Lastname = "Lampard1";
            patient.Sex = 16;
            patient.Idtype = 40001;
            patient.Clientcode = "1";
            patient.Dob = DateTime.Today.AddYears(-51);
            patient.Enrollmentdate = patient.Enrollmenttime = DateTime.Now;           
            _patientService.Sync(patient,true);

            var savedPatient = _patientService.GetAll().FirstOrDefault(x => x.UuId == patient.UuId);

            Assert.That(savedPatient, Is.Not.Null);
            uuids.Add(savedPatient.UuId);
            Assert.That(savedPatient.Lastname, Is.EqualTo("Lampard1"));
            Assert.False(string.IsNullOrWhiteSpace(_createEmrPatientHandler.GetNotification()));
            Debug.Print(savedPatient.ToString());
            Debug.Print(_createEmrPatientHandler.GetNotification());

            
            _emrRepository=new EmrRepository();
            var iqpatient = _emrRepository.GetPatient(savedPatient.UuId);
            Assert.NotNull(iqpatient);
            testIQPatientId = iqpatient.Id;
            Assert.That(iqpatient.FirstName, Is.EqualTo(patient.Firstname));
            Assert.That(iqpatient.MiddleName, Is.EqualTo(patient.Middlename));
            Assert.That(iqpatient.LastName, Is.EqualTo(patient.Lastname));
            Assert.That(iqpatient.SyncId, Is.EqualTo(patient.UuId));
            Debug.Print($"IQCare:{iqpatient.ToStringDetail()}");
        }
        [Test]
        public void should_sync_existing_patient()
        {
            var patient = _patientService.GetAll().FirstOrDefault(x => x.Lastname.ToLower() == "TerryXy".ToLower());
            Assert.That(patient, Is.Not.Null);
            patient.Lastname = "Terry";
            _patientService.Sync(patient,true);            

            var savedPatient = _patientService.GetAll().FirstOrDefault(x => x.Lastname == patient.Lastname);

            Assert.That(savedPatient, Is.Not.Null);
            Assert.That(savedPatient.Firstname, Is.EqualTo(patient.Firstname));

            Assert.False(string.IsNullOrWhiteSpace(_createEmrPatientHandler.GetNotification()));
            Debug.Print(savedPatient.ToString());
            Debug.Print(_createEmrPatientHandler.GetNotification());

            var iqpatient = _emrRepository.GetPatient(savedPatient.UuId);
            Assert.NotNull(iqpatient);
            testIQPatientId = iqpatient.Id;
            Assert.That(iqpatient.FirstName, Is.EqualTo(savedPatient.Firstname));
            Assert.That(iqpatient.MiddleName, Is.EqualTo(savedPatient.Middlename));
            Assert.That(iqpatient.LastName, Is.EqualTo(savedPatient.Lastname));
            Assert.That(iqpatient.SyncId, Is.EqualTo(savedPatient.UuId));
            Debug.Print($"IQCare:{iqpatient.ToStringDetail()}");
        }

        [Test]
        public void should_GetAll_with_Enocunters()
        {
            Assert.That(_testPatientWithEncounters, Is.Not.Empty);
            List<int> ids = _testPatientWithEncounters.Select(x => x.Id).ToList();

            var patients = _patientService.GetAll().Where(x => ids.Contains(x.Id));
            Assert.That(patients, Is.Not.Empty);
            foreach (var patient in patients)
            {
                Debug.Print(patient.ToString());
                foreach (var e in patient.Encounters)
                {
                    Debug.Print($" >.{e.ToString()}");
                    foreach (var o in e.Observations)
                    {
                        Debug.Print($"  >>.{o}");
                    }
                }
            }
        }

        [Test]
        public void should_sync_new_patient_with_encounters()
        {
            testIQPatientId = -1;
            var patient = _testEncounters.First().Patient;
            Assert.NotNull(patient);            
            Assert.That(patient, Is.Not.Null);            
            Assert.That(patient.Encounters, Is.Not.Empty);
           

            _patientService.Sync(patient,false);            
            var savedPatient = _patientService.GetAll().FirstOrDefault(x => x.Lastname == patient.Lastname);

            Assert.That(savedPatient, Is.Not.Null);            
            Debug.Print(savedPatient.ToString());
            Assert.That(savedPatient.Encounters, Is.Not.Empty);
            foreach (var e in savedPatient.Encounters)
            {
                Debug.Print($" >.{e.ToString()}");
                foreach (var o in e.Observations)
                {
                    Debug.Print($"  >>.{o}");
                }
            }

            Assert.False(string.IsNullOrWhiteSpace(_createEmrPatientHandler.GetNotification()));
            Debug.Print(_createEmrPatientHandler.GetNotification());
            var iqpatient = _emrRepository.GetPatient(savedPatient.UuId);
            Assert.NotNull(iqpatient);
            testIQPatientId = iqpatient.Id;
            Debug.Print($"IQCare:{iqpatient.ToStringDetail()}");

            Assert.That(_createEmrEncounterHandler.GetSqlActions(), Is.Not.Empty);
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [ord_Visit] where Ptn_Pk in ({testIQPatientId}) AND VisitType=219"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [dtl_fb_MARPsHTC] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(2));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [dtl_fb_DisabilityHTC] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(3));
            Assert.That(_emrRepository.ExecuteQueryStringResult($"select remarks  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({testIQPatientId})"), Is.StringContaining("No remark"));
        }

        [Test]
        public void should_sync_existing_patient_with_encounters()
        {
            testIQPatientId = -1;
            var patient = _testEncounters.First().Patient;           
            _patientService.Sync(patient, false);
            var savedPatient = _patientService.GetAll().FirstOrDefault(x => x.UuId == patient.UuId);
            Assert.That(savedPatient, Is.Not.Null);
            Assert.That(savedPatient.Encounters, Is.Not.Empty);
            var iqpatient = _emrRepository.GetPatient(savedPatient.UuId);
            Assert.NotNull(iqpatient);
            testIQPatientId = iqpatient.Id;
            Debug.Print($"IQCare before Edit:{iqpatient.ToStringDetail()}");
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [ord_Visit] where Ptn_Pk in ({testIQPatientId}) AND VisitType=219"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQueryStringResult($"select remarks  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({testIQPatientId})"), Is.StringContaining("No remark"));
        
            //var conceptText = encounterType.Concepts.FirstOrDefault(x => x.Id == 16);
            //var conceptText2 = encounterType.Concepts.FirstOrDefault(x => x.Id == 48);

            savedPatient.Lastname = "BonnyK";
            savedPatient.Clientcode = "XC";
            
            foreach (var encounter in savedPatient.Encounters)
            {
                encounter.Geoloctaion = "KISUMU";
                foreach (var o in encounter.Observations)
                {
                    if (o.MConceptId == 16)
                    {
                        o.ValueText = "color blind left eye";
                    }
                    if (o.MConceptId == 48)
                    {
                        o.ValueText ="Screen for TB2";
                    }
                }
            }
            //_uow.PatientRepository.Update(savedPatient);

            //_uow = new UnitOfWork(new SyncContext());
            //var p = _uow.PatientRepository.FindBySyncId(patient.UuId);

            _patientService.Sync(savedPatient, false);
            

            _uow = new UnitOfWork(new SyncContext());
            _patientService = new PatientService(_uow,new EmrRepository(), _createEmrPatientHandler,_createEmrEncounterHandler);
            var savedSyncedPatient = _patientService.GetAll().FirstOrDefault(x => x.UuId == patient.UuId);
            Assert.That(savedSyncedPatient, Is.Not.Null);
            Assert.AreEqual("BonnyK", savedSyncedPatient.Lastname);
            
             var emrRepository = new EmrRepository();
            var iqsyncedpatient = emrRepository.GetPatient(patient.UuId);

            Assert.NotNull(iqsyncedpatient);
            Assert.AreEqual("BonnyK", iqsyncedpatient.LastName);
            Debug.Print($"IQCare After Edit:{iqsyncedpatient.ToStringDetail()}");
            Assert.That(emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [ord_Visit] where Ptn_Pk in ({testIQPatientId}) AND VisitType=219"), Is.EqualTo(1));
            Assert.That(emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(1));
            Assert.That(emrRepository.ExecuteQueryStringResult($"select remarks  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({testIQPatientId})"), Is.StringContaining("Screen for TB2"));
        }

        [TearDown]
        public void TearDown()
        {
            _factory.CleanUpTestPatients();
            foreach (var id in uuids)
            {
                _patientService.Delete(id);
                _patientService.SaveChanges();
            }
            
            _factory.UoW = new UnitOfWork(new SyncContext());
            _factory.CleanUpTestPatientsWithEncounters();
            _factory.CleanUpTestEncountersByType();
            _factory.CleanIQCareTestPatients(testIQPatientId);
        }
    }
}
