

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
    public class LookupControllerTest : BaseControllerTest
    {

        private readonly Factory _factory = new Factory();
        private IUnitOfWork _uow;
        private List<Lookup> _testLookups;
        private ILookupService _LookupService;
        private List<Guid> uuids;

        [SetUp]
        public void Setup()
        {
            _testLookups = _factory.GenerateTestLookups();
            _uow = new UnitOfWork(new SyncContext());
            _LookupService = new LookupService(_uow);
            uuids = new List<Guid>();
        }

        [Test]
        public void should_Get()
        {
            // Arrange
            Assert.That(_testLookups, Is.Not.Empty);
            List<int> ids = _testLookups.Select(x => x.Id).ToList();
            var controller = new LookupController(_LookupService);

            // Act
            var lookups = controller.Get().Where(x => ids.Contains(x.Id));

            // Assert
            Assert.That(lookups, Is.Not.Empty);
            foreach (var lookup in lookups)
            {
                Debug.Print(lookup.ToString());
            }
        }

        [TearDown]
        public void TearDown()
        {
            _factory.CleanUpTestLookups();
        }
    }
}
