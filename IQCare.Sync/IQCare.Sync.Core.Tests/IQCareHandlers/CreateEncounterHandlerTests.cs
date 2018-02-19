using IQCare.Sync.Core.Handlers.IQCare;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Model;
using IQCare.Sync.Shared.Tests.TestData;
using NUnit.Framework;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Diagnostics;
using System.Linq;
using IQCare.Sync.Core.Events;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Interfaces.UoW;
using IQCare.Sync.Core.Services;
using IQCare.Sync.Infrastructure;
using IQCare.Sync.Infrastructure.Repository;

namespace IQCare.Sync.Core.Tests.IQCareHandlers
{
    [TestFixture]
    public class CreateEncounterHandlerTests
    {
        private CreateEmrEncounterHandler _createEmrEncounterHandler;
        private Factory _factory;
        private List<Encounter> _testEncounters;
        private EncounterCreated _encounterCreated;
        private IUnitOfWork _uow;        
        private IEmrRepository _emrRepository;

        [SetUp]
        public void Setup()
        {
            _factory=new Factory();
            _uow=new UnitOfWork(new SyncContext());
            _testEncounters = _factory.GenerateTestEncountersByType(1);
            var _encounter=_testEncounters.First();
            var _patient = _testEncounters.First().Patient;
            var _emrRepository = new EmrRepository();
            var _visitType = _emrRepository.GetVisitTypeByFeature(_encounter.EncounterType.IqcareId.Value);
            var location = _emrRepository.GetLocation(1024);
            var concepts = _uow.MConceptRepository.GetAllByEncounterType(_encounter.EncounterTypeId).ToList();
            var htslookups = _uow.LookupHtsRepository.GetAll().ToList();
            _encounterCreated = new EncounterCreated(_patient,_visitType, _encounter,location,concepts,htslookups);
            _createEmrEncounterHandler = new CreateEmrEncounterHandler();                        
        }

        [Test]
        public void should_handle()
        {
            
            Assert.NotNull(_encounterCreated.Patient);
            Assert.NotNull(_encounterCreated.Encounter);
            Assert.NotNull(_encounterCreated.VisitType);
            Assert.IsNotEmpty(_encounterCreated.MConcepts);

            _createEmrEncounterHandler.Handle(_encounterCreated);

            Assert.False(string.IsNullOrWhiteSpace(_createEmrEncounterHandler.GetNotification()));
            Debug.Print(_createEmrEncounterHandler.GetNotification());
            Assert.False(string.IsNullOrWhiteSpace(_createEmrEncounterHandler.GetSqlActionsBatch()));
            Debug.Print($"{_createEmrEncounterHandler.GetSqlActionsBatch()}");
        }

        [TearDown]
        public void TearDown()
        {
            _factory.CleanUpTestEncountersByType();
        }
    }
}