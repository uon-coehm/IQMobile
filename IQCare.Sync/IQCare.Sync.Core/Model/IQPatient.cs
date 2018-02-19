using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IQCare.Sync.Core.Model
{
    // ReSharper disable once InconsistentNaming

    [Table("HTCPatientView")]
    public class IQPatient
    {
        [Key]
        public int Id { get; set; }
        public string FirstName { get; set; }
        public string MiddleName { get; set; }
        public string LastName { get; set; }
        public int Sex { get; set; }
        public DateTime Dob { get; set; }
        public int? DobPrecision { get; set; }
        public string ClientCode { get; set; }
        public int LocationId { get; set; }
        public DateTime? RegistrationDate { get; set; }
        public DateTime? CreateDate { get; set; }
        public DateTime? UpdateDate { get; set; }
        public int DeleteFlag { get; set; }
        public Guid? SyncId { get; set; }



        public override string ToString()
        {
            return $"{FirstName} {MiddleName} {LastName}";
        }
        public string ToStringDetail()
        {
            return $"{ClientCode}|{FirstName} {MiddleName} {LastName}|{SyncId??new Guid()}";
        }
    }
}

/*
Ptn_Pk
CAST(decryptbykey(FirstName) AS varchar(50))
CAST(decryptbykey(LastName) AS varchar(50))
CAST(decryptbykey(MiddleName) AS varchar(50))
LocationID
RegistrationDate
DOB
Sex
DobPrecision
Client_Code
UserID
CreateDate
UpdateDate
DeleteFlag
*/
