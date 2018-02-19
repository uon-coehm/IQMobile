using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Interfaces.UoW;
using IQCare.Sync.Core.Model;
using IQCare.Sync.Infrastructure;
using IQCare.Sync.Infrastructure.Repository;
using IQCare.Sync.Shared.Tests.TestData;
using NUnit.Framework;

namespace IQCare.Sync.Shared.Tests
{
    [TestFixture]
    public class UtiliyTests
    {
        private readonly string _mydocs = Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments)+@"\iqmstore";
        private readonly Guid _filename = new Guid("6f99410f-d02e-48c2-af59-52c8e7d727fd");
        private SqlAction _sqlAction;
        private string _pathOfMsg;

        [SetUp]
        public void Setup()
        {
            _sqlAction = new SqlAction(1, "PRINT 'Hello';", "printhello", "print hello message");
        }

        [Test]
        public void should_StoreMessage()
        {
            var msg = Utility.StoreMessage(_sqlAction, _mydocs, $"{_filename}");
            Debug.Print(msg);
            Assert.That(msg, Is.StringEnding("OK"));
            _pathOfMsg = $"{_mydocs}{@"\"}{_filename}.txt";
            PrintOut(_pathOfMsg);
        }

        [TearDown]
        public void TearDown()
        {
            
            if (File.Exists(_pathOfMsg))
            {
                File.Delete(_pathOfMsg);
            }
        }

        private void PrintOut(string file)
        {
            string line;
            StreamReader sr = new StreamReader(file);
            line = sr.ReadLine();
            while (line != null)
            {
                Debug.Print(line);
                line = sr.ReadLine();
            }
            sr.Close();
        }
    }
}
