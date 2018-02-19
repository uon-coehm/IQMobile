using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Shared.Tests.TestData
{

    public class PatientData
    {

        public static List<Patient> GetTestPatients()
        {
            var patients = new List<Patient>();

            var patient = new Patient();

            patient.Firstname = "John";
            patient.Middlename = "T";
            patient.Lastname = "TerryXy";
            patient.Sex = 16;
            patient.Idtype = 40002;
            patient.Clientcode = "11";
            patient.Dob = DateTime.Today.AddYears(-22);
            patient.Enrollmentdate = patient.Enrollmenttime = DateTime.Now;
            patients.Add(patient);

            return patients;
        }

        public static Patient GetPatient()
        {
            var patient = new Patient();

            patient.Firstname = "Frank1";
            patient.Middlename = "L";
            patient.Lastname = "Lampard1";
            patient.Sex = 16;
            patient.Idtype = 40001;
            patient.Clientcode = "1";
            patient.Dob = DateTime.Today.AddYears(-51);
            patient.Enrollmentdate = patient.Enrollmenttime = DateTime.Now;
            patient.UuId = new Guid("b7e22864-e9b4-430a-89c9-3601f833e180");

            return patient;
        }
        public static Patient GetPatientForEncounter()
        {
            var patient = new Patient();

            patient.Firstname = "Jamie";
            patient.Middlename = "M";
            patient.Lastname = "Vardy";
            patient.Sex = 16;
            patient.Idtype = 40001;
            patient.Clientcode = "1x0";
            patient.Dob = DateTime.Today.AddYears(-23);
            patient.Enrollmentdate = patient.Enrollmenttime = DateTime.Now;
            return patient;
        }
        public static Patient GetPatientForEncounterNew()
        {
            var patient = new Patient();

            patient.Firstname = "Hugo";
            patient.Middlename = "X";
            patient.Lastname = "Chaves";
            patient.Sex = 16;
            patient.Idtype = 40001;
            patient.Clientcode = "1x1";
            patient.Dob = DateTime.Today.AddYears(-41);
            patient.Enrollmentdate = patient.Enrollmenttime = DateTime.Now;

            return patient;
        }

    }
}
