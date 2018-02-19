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
    public class LookupRepositoryTests
    {
        private readonly Factory _factory = new Factory();
        private IUnitOfWork _uow;
        private List<Lookup> _testLookups;
        [SetUp]
        public void Setup()
        {
            _testLookups = _factory.GenerateTestLookups();
            _uow = new UnitOfWork(new SyncContext());
        }

        [Test]
        public void should_GetAll()
        {
            var lookups = _uow.LookupRepository.GetAll();
            Assert.That(lookups, Is.Not.Empty);
            foreach (var lookup in lookups)
            {
                if (_testLookups.Contains(lookup))
                {
                    Debug.Print(lookup.ToString());
                }
            }
        }

        [Test]
        public void should_GetAllByCodeId()
        {
            var lookups = _uow.LookupRepository.FindByCodeId(-1);
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