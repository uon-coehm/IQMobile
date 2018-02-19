

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
    public class UserServiceTests
    {
        private readonly Factory _factory = new Factory();
        private IUnitOfWork _uow;
        private List<User> _testUsers;
        private IUserService _userService;
        private List<Guid> uuids;

        [SetUp]
        public void Setup()
        {
            _testUsers= _factory.GenerateTestUsers();
            _uow = new UnitOfWork(new SyncContext());
            _userService = new UserService(_uow);
            uuids = new List<Guid>();
        }

        [Test]
        public void should_GetAll()
        {           
            Assert.That(_testUsers, Is.Not.Empty);
            List<int> ids = _testUsers.Select(x => x.Id).ToList();

            var users = _userService.GetAll().Where(x => ids.Contains(x.Id));
            Assert.That(users, Is.Not.Empty);
            foreach (var user in users)
            {
                Debug.Print($"{user.Username} | {user.Password}");
            }

            Debug.Print("------------------------------");
            foreach (var user in _userService.GetAll())
            {
                Debug.Print($"{user.Username} | {user.Password}");
            }
        }

        [TearDown]
        public void TearDown()
        {
            _factory.CleanUpTestUsers();
         
            foreach (var id in uuids)
            {
                _userService.Delete(id);
            }
            _userService.SaveChanges();
        }
    }
}
