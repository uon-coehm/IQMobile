

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
    public class ModuleControllerTest : BaseControllerTest
    {

        private readonly Factory _factory = new Factory();
        private IUnitOfWork _uow;
        private List<Module> _testModules;
        private IModuleService _moduleService;
        private List<Guid> uuids;

        [SetUp]
        public void Setup()
        {
            _testModules = _factory.GenerateTestModules();
            _uow = new UnitOfWork(new SyncContext());
            _moduleService = new ModuleService(_uow);
            uuids = new List<Guid>();
        }

        [Test]
        public void should_Get()
        {
            // Arrange
            Assert.That(_testModules, Is.Not.Empty);
            List<int> ids = _testModules.Select(x => x.Id).ToList();
            var controller = new ModuleController(_moduleService);

            // Act
            var modules = controller.Get().Where(x => ids.Contains(x.Id));

            // Assert
            Assert.That(modules,Is.Not.Empty);
            foreach (var module in modules)
            {
                Debug.Print(module.ToString());
            }
        }

        [TearDown]
        public void TearDown()
        {
            _factory.CleanUpTestModules();
        }
    }
}
