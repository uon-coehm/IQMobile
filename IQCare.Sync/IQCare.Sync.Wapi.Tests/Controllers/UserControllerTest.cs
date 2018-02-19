

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
    public class UserControllerTest: BaseControllerTest
    {

        private readonly Factory _factory = new Factory();
        private IUnitOfWork _uow;
        private List<User> _testUsers;
        private IUserService _userService;
        private List<Guid> uuids;

        [SetUp]
        public void Setup()
        {
            _testUsers = _factory.GenerateTestUsers();
            _uow = new UnitOfWork(new SyncContext());
            _userService = new UserService(_uow);
            uuids = new List<Guid>();
        }

        [Test] public void should_Get()
        {
            // Arrange
            Assert.That(_testUsers, Is.Not.Empty);
            List<int> ids = _testUsers.Select(x => x.Id).ToList();
            UserController controller = new UserController(_userService);

            // Act
            var users = controller.Get().Where(x => ids.Contains(x.Id));

            // Assert
            Assert.That(users,Is.Not.Empty);
            foreach (var user in users)
            {
                Debug.Print(user.ToString());
            }

        }

        [TearDown]
        public void TearDown()
        {
            _factory.CleanUpTestUsers();
        }
    }
}
