using System;
using System.CodeDom;
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
    public class UserRepositoryTests
    {
        private readonly Factory _factory=new Factory();
        private IUnitOfWork _uow;
        private List<User> _testUsers;

        [SetUp]
        public void Setup()
        {
            _testUsers= _factory.GenerateTestUsers();
            _uow = new UnitOfWork(new SyncContext());
        }

        [Test]
        public void should_GetAll()
        {
            var users = _uow.UserRepository.GetAll();
            Assert.That(users, Is.Not.Empty);
            foreach (var user in users)
            {
                if (_testUsers.Contains(user))
                {
                    Debug.Print(user.ToString());
                }
            }
        }

        [TearDown]
        public void TearDown()
        {
            _factory.CleanUpTestUsers();
        }
    }
}