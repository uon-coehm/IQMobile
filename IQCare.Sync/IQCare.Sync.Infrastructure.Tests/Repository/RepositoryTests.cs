using System;
using System.Diagnostics;
using IQCare.Sync.Core.Interfaces.UoW;
using IQCare.Sync.Infrastructure.Repository;
using NUnit.Framework;

namespace IQCare.Sync.Infrastructure.Tests.Repository
{
    [TestFixture]
    public class RepositoryTests
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
            var users = _uow.UserRepository.GetAll();
            Assert.That(users, Is.Not.Null);
            foreach (var user in users)
            {                
               Debug.Print(user.ToString());
            }
        }
        
        [TearDown]
        public void TearDown()
        {
        }
    }
}