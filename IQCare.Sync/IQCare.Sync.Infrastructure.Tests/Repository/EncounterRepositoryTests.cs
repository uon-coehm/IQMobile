using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.ComTypes;
using IQCare.Sync.Core.Interfaces.UoW;
using IQCare.Sync.Core.Model;
using IQCare.Sync.Infrastructure.Repository;
using IQCare.Sync.Shared.Tests.TestData;
using NUnit.Core;
using NUnit.Framework;

namespace IQCare.Sync.Infrastructure.Tests.Repository
{
    [TestFixture]
    public class EncounterRepositoryTests
    {
        private readonly Factory _factory = new Factory();
        private IUnitOfWork _uow;
        private List<Encounter> _testEncounters;

        [SetUp]
        public void Setup()
        {
            _testEncounters = _factory.GenerateTestEncounters();
            _uow = new UnitOfWork(new SyncContext());
        }

        [Test]
        public void should_GetAll()
        {
            var e = _testEncounters.First();
            Assert.That(e, Is.Not.Null);

            var encounters = _uow.EncounterRepository.GetAll().Where(x => x.Id == e.Id);
            Assert.That(encounters, Is.Not.Empty);
            foreach (var encounter in encounters)
            {
                Debug.Print(encounter.ToString());
            }
        }

        [Test]
        public void should_GetAll_Encounter_Obs()
        {
            var e = _testEncounters.First();
            Assert.That(e, Is.Not.Null);

            var encounters = _uow.EncounterRepository.GetAll().Where(x => x.Id == e.Id);
            Assert.That(encounters, Is.Not.Empty);
            foreach (var encounter in encounters)
            {
                Assert.That(encounter.Observations, Is.Not.Empty);
                foreach (var obs in encounter.Observations)
                {
                    Debug.Print($"   >.{obs.ToString()}");
                }
            }
        }


        [Test]
        public void should_GetAll_Encounter_Patient()
        {
            var e = _testEncounters.First();
            Assert.That(e, Is.Not.Null);

            var encounters = _uow.EncounterRepository.GetAll().Where(x => x.Id == e.Id);
            Assert.That(encounters, Is.Not.Empty);
            foreach (var encounter in encounters)
            {
                Debug.Print(encounter.ToString());
                var patient = encounter.Patient;
                Assert.That(patient, Is.Not.Null);
                Debug.Print($"   >.{patient}");
            }
        }

        [TearDown]
        public void TearDown()
        {
            _factory.CleanUpTestEncounters();
        }
    }
}