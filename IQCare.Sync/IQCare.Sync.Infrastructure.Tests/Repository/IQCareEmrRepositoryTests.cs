using System;
using System.CodeDom;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.ComTypes;
using IQCare.Sync.Core.Events;
using IQCare.Sync.Core.Handlers.IQCare;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Interfaces.UoW;
using IQCare.Sync.Core.Model;
using IQCare.Sync.Core.Services;
using IQCare.Sync.Infrastructure.Repository;
using IQCare.Sync.Shared;
using IQCare.Sync.Shared.Tests.TestData;
using NUnit.Framework;

namespace IQCare.Sync.Infrastructure.Tests.Repository
{
    [TestFixture]
    // ReSharper disable once InconsistentNaming
    public class IQCareEmrRepositoryTests
    {
        private readonly int locationId = 1024;
        private IUnitOfWork _uow;
        private IEmrRepository _emrRepository;
        private ICreateEmrPatientHandler _createEmrPatientHandler;
        private ICreateEmrEncounterHandler _createEmrEncounterHandler;
        private Factory _factory;
        
        private List<Patient> _testPatients = new List<Patient>();
        private List<Guid> uuids = new List<Guid>();
        private IPatientService _patientService;
        private IEncounterService _encounterService;
        private int testIQPatientId;
        private List<Encounter> _testEncounters;
        private EncounterCreated _encounterCreated;

        [SetUp]
        public void Setup()
        {
            _factory=new Factory();
            testIQPatientId = -1;
            _testPatients = _factory.GenerateTestPatients();
            _testEncounters = _factory.GenerateTestEncountersByType(1);
            _uow = new UnitOfWork(new SyncContext());
            _emrRepository = new EmrRepository();
            _createEmrPatientHandler = new CreateEmrPatientHandler();
            _createEmrEncounterHandler = new CreateEmrEncounterHandler();

            _patientService = new PatientService(_uow,_emrRepository,_createEmrPatientHandler,_createEmrEncounterHandler);
            _encounterService = _patientService.EncounterService;
            
            
            var _visitType = _emrRepository.GetVisitTypeByFeature(_testEncounters.First().EncounterType.IqcareId.Value);
            var location = _emrRepository.GetLocation(1024);
            var concepts = _uow.MConceptRepository.GetAllByEncounterType(_testEncounters.First().EncounterTypeId).ToList();
            var htslookups = _uow.LookupHtsRepository.GetAll().ToList();
            _encounterCreated = new EncounterCreated(_testEncounters.First().Patient, _visitType, _testEncounters.First(), location, concepts,htslookups);
        }

        [Test]
        public void should_GetModule()
        {
            var module = _emrRepository.GetModule(5);
            Assert.NotNull(module);
            Debug.Print($"{module}");
            foreach (var feature in module.Features)
            {
                Debug.Print($" >.{feature}");
                foreach (var visitType in feature.VisitTypes)
                {
                    Debug.Print($" >>.{visitType}");
                }
            }
        }
        [Test]
        public void should_GetFeautre()
        {
            var feature = _emrRepository.GetFeature(1120);
            Assert.NotNull(feature);
            Debug.Print($" >.{feature}");
            foreach (var visitType in feature.VisitTypes)
            {
                Debug.Print($" >>.{visitType}");
            }
        }
        [Test]
        public void should_GetVisitType()
        {
            var visitType = _emrRepository.GetVisitType(219);
            Assert.NotNull(visitType);
            
            Debug.Print($"{visitType.ToStringDetail()}");
            
            visitType = _emrRepository.GetVisitType(12);
            Assert.NotNull(visitType);
            Debug.Print($"{visitType.ToStringDetail()}");
        }

        [Test]
        public void should_GetLocation()
        {
            var location = _emrRepository.GetLocation(1024);
            Assert.NotNull(location);
            Debug.Print($"{location}");
        }
        [Test]
        public void should_GetPatient()
        {
            var module = _emrRepository.GetPatient(5);
            Assert.NotNull(module);
            Debug.Print($"{module.ToStringDetail()}");
        }


        [Test]
        public void should_decrypt_session()
        {
            string sql = $@" 
                {Utility.GetSqlDecrptyion()}
                select top 1 (convert(varchar(50),decryptbykey(firstname))) from mst_Patient";
            string patientFirstName = _emrRepository.ExecuteQueryStringResult(sql);
            Assert.False(string.IsNullOrWhiteSpace(patientFirstName));
            Debug.Print(patientFirstName);
        }

        [Test]
        public void should_executeSqlQuery()
        {
            string sql = "select count(*) as totalmods from mst_module";
            int noOfmodules = Convert.ToInt32(_emrRepository.ExecuteQuery(sql));
            Assert.True(noOfmodules > 0);
            Debug.Print(noOfmodules.ToString());
        }

        [Test]
        public void should_Create_Patient()
        {
            testIQPatientId = -1;
            var patient = _testPatients.First();
            Assert.NotNull(patient);
            IQLocation location = _emrRepository.GetLocation(locationId);
            Assert.NotNull(location);
            _createEmrPatientHandler.Handle(new PatientCreated(patient,location));
            Debug.Print(_createEmrPatientHandler.GetNotification());
            Assert.That(_createEmrPatientHandler.GetSqlActions(),Is.Not.Empty);
            //Debug.Print(_createEmrPatientHandler.GetSqlActionsBatch());

            _emrRepository.CreatePatient(_createEmrPatientHandler.GetSqlActions(), patient.UuId);

            var iqpatient = _emrRepository.GetPatient(patient.UuId);
            Assert.NotNull(iqpatient);
            Assert.That(iqpatient.ClientCode,Is.EqualTo(patient.Clientcode));
            testIQPatientId = iqpatient.Id;

            Assert.True(testIQPatientId > -1);
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [mst_Patient] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [DTL_FBCUSTOMFIELD_Patient_Registration] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQueryStringResult($"SELECT IQNumber FROM mst_Patient WHERE Ptn_Pk = {testIQPatientId}"), Is.StringContaining("IQ-"));

            foreach (var t in _factory.GetIQTables())
            {
                Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  {t} where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(1));
            }

            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [ord_Visit] where Ptn_Pk in ({testIQPatientId}) AND VisitType=12"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [ord_Visit] where Ptn_Pk in ({testIQPatientId}) AND VisitType=115"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [lnk_patientprogramstart] where Ptn_Pk in ({testIQPatientId}) AND ModuleID=5"), Is.EqualTo(1));
           
            Debug.Print($"From Android:{patient}!");
            Debug.Print(new string('-', 15));
            Debug.Print($"New Patient:{iqpatient.ToStringDetail()}");
        }
        [Test]
        public void should_Update_Patient()
        {
            testIQPatientId = -1;
            var patient = _testPatients.First();
            Assert.NotNull(patient);
            IQLocation location = _emrRepository.GetLocation(locationId);
            Assert.NotNull(location);
            _createEmrPatientHandler.Handle(new PatientCreated(patient, location));
            _emrRepository.CreatePatient(_createEmrPatientHandler.GetSqlActions(), patient.UuId);

            var iqpatient = _emrRepository.GetPatient(patient.UuId);
            Assert.NotNull(iqpatient);
            Debug.Print($"From Android:{patient}!");
            Assert.That(iqpatient.ClientCode, Is.EqualTo(patient.Clientcode));
            testIQPatientId = iqpatient.Id;

            /////
            patient.Lastname = "Kimani";
            patient.Clientcode = "KKK";
            _uow = new UnitOfWork(new SyncContext());
            _uow.PatientRepository.Update(patient);
            _uow.Commit();

            _uow = new UnitOfWork(new SyncContext());
            var updatedPatient = _uow.PatientRepository.FindById(patient.Id);
            Assert.NotNull(updatedPatient);
            Assert.AreEqual(updatedPatient.UuId,patient.UuId);
            Assert.AreEqual("Kimani", updatedPatient.Lastname);
            Assert.AreEqual("KKK", updatedPatient.Clientcode);

            _createEmrPatientHandler.Handle(new PatientCreated(updatedPatient, location));
            _emrRepository.CreatePatient(_createEmrPatientHandler.GetSqlActions(), updatedPatient.UuId);

            _emrRepository=new EmrRepository();
            var iqpatientUpdated = _emrRepository.GetPatient(updatedPatient.UuId);
            Assert.NotNull(iqpatientUpdated);
            Assert.That(iqpatientUpdated.ClientCode, Is.EqualTo(updatedPatient.Clientcode));
            Assert.That(iqpatientUpdated.LastName, Is.EqualTo(updatedPatient.Lastname));
            Assert.AreEqual(testIQPatientId, iqpatientUpdated.Id);

            Assert.True(testIQPatientId > -1);
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [mst_Patient] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [DTL_FBCUSTOMFIELD_Patient_Registration] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQueryStringResult($"SELECT IQNumber FROM mst_Patient WHERE Ptn_Pk = {testIQPatientId}"), Is.StringContaining("IQ-"));

            foreach (var t in _factory.GetIQTables())
            {
                Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  {t} where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(1));
            }

            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [ord_Visit] where Ptn_Pk in ({testIQPatientId}) AND VisitType=12"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [ord_Visit] where Ptn_Pk in ({testIQPatientId}) AND VisitType=115"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [lnk_patientprogramstart] where Ptn_Pk in ({testIQPatientId}) AND ModuleID=5"), Is.EqualTo(1));

            
            Debug.Print($"Udated to:{updatedPatient}!");
            Debug.Print(new string('-', 15));
            Debug.Print($"New Patient:{iqpatientUpdated.ToStringDetail()}");
        }

        [Test]
        public void should_Create_Encounter()
        {
            testIQPatientId = -1;
            var patient = _testEncounters.First().Patient;
            Assert.NotNull(patient);
            IQLocation location = _emrRepository.GetLocation(locationId);
            Assert.NotNull(location);
            _createEmrPatientHandler.Handle(new PatientCreated(patient, location));            
            Assert.That(_createEmrPatientHandler.GetSqlActions(), Is.Not.Empty);
            _emrRepository.CreatePatient(_createEmrPatientHandler.GetSqlActions(), patient.UuId);
            var iqpatient = _emrRepository.GetPatient(patient.UuId);
            Assert.NotNull(iqpatient);
            Assert.That(iqpatient.ClientCode, Is.EqualTo(patient.Clientcode));
            testIQPatientId = iqpatient.Id;
            Assert.True(testIQPatientId > -1);
            Debug.Print($"New Patient:{iqpatient.ToStringDetail()}");

            _createEmrEncounterHandler.Handle(_encounterCreated);
            Assert.That(_createEmrEncounterHandler.GetSqlActions(), Is.Not.Empty);
            _emrRepository.CreateEncounter(_createEmrEncounterHandler.GetSqlActions(), patient.UuId);

            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [ord_Visit] where Ptn_Pk in ({testIQPatientId}) AND VisitType=219"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [dtl_fb_MARPsHTC] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(2));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [dtl_fb_DisabilityHTC] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(3));
        }

        [Test]
        public void should_Update_Encounter()
        {
            testIQPatientId = -1;
            var patient = _testEncounters.First().Patient;
            Assert.NotNull(patient);
            IQLocation location = _emrRepository.GetLocation(locationId);
            Assert.NotNull(location);
            _createEmrPatientHandler.Handle(new PatientCreated(patient, location));
            Assert.That(_createEmrPatientHandler.GetSqlActions(), Is.Not.Empty);
            _emrRepository.CreatePatient(_createEmrPatientHandler.GetSqlActions(), patient.UuId);
            var iqpatient = _emrRepository.GetPatient(patient.UuId);
            Assert.NotNull(iqpatient);
            Assert.That(iqpatient.ClientCode, Is.EqualTo(patient.Clientcode));
            testIQPatientId = iqpatient.Id;
            Assert.True(testIQPatientId > -1);
            Debug.Print($"New Patient:{iqpatient.ToStringDetail()}");

            _createEmrEncounterHandler.Handle(_encounterCreated);
            Assert.That(_createEmrEncounterHandler.GetSqlActions(), Is.Not.Empty);
            _emrRepository.CreateEncounter(_createEmrEncounterHandler.GetSqlActions(), patient.UuId);

            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [ord_Visit] where Ptn_Pk in ({testIQPatientId}) AND VisitType=219"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [dtl_fb_MARPsHTC] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(2));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [dtl_fb_DisabilityHTC] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(3));


            var e = _testEncounters.First();
            e.Observations.First(x => x.MConceptId == 30).ValueLookup = 4125;
            e.Observations.First(x => x.MConceptId == 24).ValueNumeric = 1;
            e.Observations.First(x => x.MConceptId == 48).ValueText = "Check Later";
            e.Observations.First(x => x.MConceptId == 11).ValueMultipleChoice = "4051";

            _encounterCreated = new EncounterCreated(_testEncounters.First().Patient, _encounterCreated.VisitType,e, _encounterCreated.Location, _encounterCreated.MConcepts, _encounterCreated.LookupsHts);
            _createEmrEncounterHandler.Handle(_encounterCreated);
            _emrRepository.CreateEncounter(_createEmrEncounterHandler.GetSqlActions(), patient.UuId);

            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [ord_Visit] where Ptn_Pk in ({testIQPatientId}) AND VisitType=219"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQuery($"select count(Ptn_Pk)  from  [dtl_fb_MARPsHTC] where Ptn_Pk in ({testIQPatientId})"), Is.EqualTo(1));
            Assert.That(_emrRepository.ExecuteQueryStringResult($"select remarks  from  [DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in ({testIQPatientId})"), Is.StringContaining("Check Later"));

        }

        [TearDown]
        public void TearDown()
        {
            _factory.CleanIQCareTestPatients();
            _factory.CleanUpTestPatients();
            foreach (var id in uuids)
            {
                _patientService.Delete(id);
            }
            _patientService.SaveChanges();

            _factory.CleanUpTestEncountersByType();
            
        }
    }
}