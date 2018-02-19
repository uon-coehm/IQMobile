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
    public class PatientRepositoryTests
    {
        private readonly Factory _factory=new Factory();
        private IUnitOfWork _uow;
        private List<Patient> _testPatients;

        [SetUp]
        public void Setup()
        {
            _testPatients= _factory.GenerateTestPatients();
            _uow = new UnitOfWork(new SyncContext());
        }

        [Test]
        public void should_GetAll()
        {
            var patients = _uow.PatientRepository.GetAll();
            Assert.That(patients, Is.Not.Empty);
            foreach (var patient in patients)
            {
                if (_testPatients.Contains(patient))
                {
                    Debug.Print(patient.ToString());
                }
            }
        }

        [TearDown]
        public void TearDown()
        {
            _factory.CleanUpTestPatients();
        }
    }
}