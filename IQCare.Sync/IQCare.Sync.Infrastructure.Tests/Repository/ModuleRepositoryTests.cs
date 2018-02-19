using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using IQCare.Sync.Core.Interfaces.UoW;
using IQCare.Sync.Core.Model;
using IQCare.Sync.Infrastructure.Repository;
using IQCare.Sync.Shared.Tests.TestData;
using NUnit.Framework;

namespace IQCare.Sync.Infrastructure.Tests.Repository
{
    [TestFixture]
    public class ModuleRepositoryTests
    {
        private readonly Factory _factory = new Factory();
        private IUnitOfWork _uow;
        private List< Module> _testModules;

        [SetUp]
        public void Setup()
        {
            _testModules = _factory.GenerateTestModulesNoConcepts();
            _uow = new UnitOfWork(new SyncContext());
        }

        [Test]
        public void should_GetAll()
        {
            var modules = _uow.ModuleRepository.GetAll();
            Assert.That(modules, Is.Not.Empty);
            foreach (var module in modules)
            {
                if (_testModules.Contains(module))
                {
                    Debug.Print(module.ToString());
                    foreach (var encounterType in module.EncounterTypes)
                    {
                        Debug.Print($"   >.{encounterType.ToString()}");
                    }
                }
            }
        }
        
        [TearDown]
        public void TearDown()
        {
            _factory.CleanUpTestModulesNoConcepts();
        }
    }
}