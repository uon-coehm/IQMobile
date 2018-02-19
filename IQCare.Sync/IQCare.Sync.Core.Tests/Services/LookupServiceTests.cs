

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
    public class LookupServiceTests
    {
        private readonly Factory _factory = new Factory();
        private IUnitOfWork _uow;
        private List<Lookup> _testLookups;
        private ILookupService _lookupService;       

        [SetUp]
        public void Setup()
        {
            _testLookups= _factory.GenerateTestLookups();
            _uow = new UnitOfWork(new SyncContext());
            _lookupService = new LookupService(_uow);
        }

        [Test]
        public void should_GetAll()
        {           
            Assert.That(_testLookups, Is.Not.Empty);
            List<int> ids = _testLookups.Select(x => x.Id).ToList();
            var lookups = _lookupService.GetAll().Where(x => ids.Contains(x.Id));
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
