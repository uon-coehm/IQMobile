

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using IQCare.Sync.Core.Interfaces;
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
    public class ConceptControllerTest : BaseControllerTest
    {
        private readonly Factory _factory = new Factory();
        private IUnitOfWork _uow;
        private List<Module> _testModules;
        private IConceptService _conceptService;

        [SetUp]
        public void Setup()
        {
            _testModules = _factory.GenerateTestModules();
            _uow = new UnitOfWork(new SyncContext());
            _conceptService = new ConceptService(_uow);
        }

        [Test]
        public void should_Get()
        {
            // Arrange
            var encounterType = _testModules.First().EncounterTypes.First();
            Assert.That(encounterType, Is.Not.Null);
            var testConcepts = encounterType.Concepts;
            Assert.That(testConcepts, Is.Not.Empty);
            var controller = new ConceptController(_conceptService);

            // Act
            var concepts = controller.Get(encounterType.Id);

            // Assert
            Assert.That(concepts,Is.Not.Empty);
            foreach (var concept in concepts)
            {
                Assert.That(testConcepts.Contains(concept));
                Debug.Print(concept.ToString());
            }
        }

        [TearDown]
        public void TearDown()
        {
            _factory.CleanUpTestModules();
        }
    }
}
