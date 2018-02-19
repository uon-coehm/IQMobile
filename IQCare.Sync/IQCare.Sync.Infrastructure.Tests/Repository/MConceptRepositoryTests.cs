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
    public class MConceptRepositoryTests
    {
        private readonly Factory _factory = new Factory();
        private IUnitOfWork _uow;
        private List<Module> _testModules;

        [SetUp]
        public void Setup()
        {
            _testModules = _factory.GenerateTestModules();
            _uow = new UnitOfWork(new SyncContext());
        }


        [Test]
        public void should_GetAll()
        {
            var et = _testModules.First().EncounterTypes.First();
            var allConcepts = _uow.MConceptRepository.GetAll().Where(x => x.EncounterTypeId == et.Id);
            Assert.That(allConcepts, Is.Not.Empty);

            foreach (var c in allConcepts)
            {
                Debug.Print($"{c.EncounterType} >>.{c}");
            }
        }
        [Test]
        public void should_GetAll_with_iqconcepts()
        {
            var et = _testModules.First().EncounterTypes.First();
            var mIqConcept = _uow.MConceptRepository.GetAll().FirstOrDefault(x => x.IQConcepts.Count>0);
            Assert.That(mIqConcept, Is.Not.Null);
            Assert.That(mIqConcept.IQConcepts, Is.Not.Empty);
            Debug.Print($"{mIqConcept}");
            foreach (var c in mIqConcept.IQConcepts)
            {
                Debug.Print($">>.{c}");
            }
        }

        [Test]
        public void should_GetAllByEncouterType()
        {
            var et = _testModules.First().EncounterTypes.First();
            var allConcepts = _uow.MConceptRepository.GetAllByEncounterType(et.Id);
            Assert.That(allConcepts, Is.Not.Empty);

            foreach (var c in allConcepts)
            {
                Debug.Print($"{c.EncounterType} >>.{c}");
            }
        }

        [TearDown]
        public void TearDown()
        {
            _factory.CleanUpTestModules();
        }
    }
}