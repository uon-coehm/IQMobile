using System;
using System.Diagnostics;
using System.Linq;
using IQCare.Sync.Core.Interfaces.UoW;
using IQCare.Sync.Infrastructure.Repository;
using NUnit.Framework;

namespace IQCare.Sync.Infrastructure.Tests.Repository
{
    [TestFixture]
    public class MDataTypeMapRepositoryTests
    {
        private IUnitOfWork _uow;
        [SetUp]
        public void Setup()
        {
            _uow = new UnitOfWork(new SyncContext());
        }

        [Test]
        public void should_GetAll()
        {
            var dataTypeMaps = _uow.MDataTypeMapRepository.GetAll();
            Assert.That(dataTypeMaps, Is.Not.Empty);
            foreach (var dataTypeMap in dataTypeMaps)
            {                
               Debug.Print(dataTypeMap.ToString());
            }
        }
        
        [TearDown]
        public void TearDown()
        {
        }
    }
}