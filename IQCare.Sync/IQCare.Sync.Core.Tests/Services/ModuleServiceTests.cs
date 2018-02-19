

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
using NUnit.Framework;

namespace IQCare.Sync.Core.Tests.Services
{
    [TestFixture]
    public class ModuleServiceTests
    {
        private readonly Factory _factory = new Factory();
        private IUnitOfWork _uow;
        private List<Module> _testModules;
        private IModuleService _moduleService;       

        [SetUp]
        public void Setup()
        {
            _testModules= _factory.GenerateTestModules();
            _uow = new UnitOfWork(new SyncContext());
            _moduleService = new ModuleService(_uow);
        }

        [Test]
        public void should_GetAll()
        {           
            Assert.That(_testModules, Is.Not.Empty);
            List<int> ids = _testModules.Select(x => x.Id).ToList();
            var modules = _moduleService.GetAll().Where(x => ids.Contains(x.Id));
            Assert.That(modules, Is.Not.Empty);
            foreach (var module in modules)
            {
                Debug.Print(module.ToString());
                foreach (var encounterType in module.EncounterTypes)
                {
                    Debug.Print($" >.{encounterType}");
                    foreach (var concept in encounterType.Concepts)
                    {
                        Debug.Print($"  >>.{concept}");
                    }
                }
            }
        }

        [TearDown]
        public void TearDown()
        {
            _factory.CleanUpTestModules();
        }
    }
}
