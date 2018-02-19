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
using NUnit.Framework;

namespace IQCare.Sync.Core.Tests.Model
{
    [TestFixture]
    public class UserTests
    {
        private User user;

        [SetUp]
        public void Setup()
        {
            user = TestUser.GetUser();
        }

        [Test]
        public void should_Get_decrypted_password()
        {
           Assert.AreEqual("c0nste11a",user.Password);
            Debug.Print(user.Password);
        }

        [TearDown]
        public void TearDown()
        {
        }

        private class TestUser
        {
            public static User GetUser()
            {
                var user = new User();
                user.Username = "John";
                user.Password = "Zu7BrcApEvdWiVLpjGpuhw==";
                user.Id = 1;
                return user;
            }
        }


    }
}
