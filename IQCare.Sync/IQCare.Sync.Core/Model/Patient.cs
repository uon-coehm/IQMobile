using IQCare.Sync.Shared;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace IQCare.Sync.Core.Model
{
    public class Patient:SyncEntity
    {
        [JsonProperty("firstname")]
        public string Firstname  { get; set; }
        [JsonProperty("middlename")]
        public string Middlename{ get; set; }
        [JsonProperty("lastname")]
        public string  Lastname{ get; set; }
        [JsonProperty("sex")]
        public int  Sex { get; set; }
        [JsonProperty("dob")]
        public DateTime  Dob { get; set; }
        [NotMapped]
        [JsonIgnore]
        public string DobString
        {
            get { return DateString(Dob); }
        }
        [JsonProperty("clientcode")]
        public string Clientcode { get; set; }
        [JsonProperty("contactphone")]
        public string Contactphone { get; set; }
        [JsonProperty("kin")]
        public string  Kin { get; set; }
        [JsonProperty("kinphone")]
        public string Kinphone { get; set; }
        [JsonProperty("kinrelation")]
        public int Kinrelation { get; set; }
        [JsonProperty("kinrelationother")]
        public string Kinrelationother { get; set; }
        [JsonProperty("enrollmentdate")]
        public DateTime Enrollmentdate { get; set; }
    
        [NotMapped]
        [JsonIgnore]
        public string EnrollmentdateString
        {
            get { return DateString(Enrollmentdate); }
        }
        [JsonProperty("enrollmenttime")]
        public DateTime Enrollmenttime { get; set; }

        [NotMapped]
        [JsonIgnore]
        public string EnrollmenttimeString
        {
            get { return DateString(Enrollmenttime); }
        }
        [JsonProperty("idtype")]
        public int Idtype { get; set; }

        [JsonProperty("estimateddob")]
        public bool Estimateddob { get; set; }

        [Column("partner_id")]
        [JsonIgnore]
        public int? PartnerId { get; set; }
        [JsonProperty("partner")]
        public virtual Patient Partner { get; set; }
        [Column("partner_uuid")]
        [JsonIgnore]
        public Guid? PartnerUuid { get; set; }


        [JsonProperty("encounters")]
        public virtual ICollection<Encounter> Encounters { get; set; }

        public Patient()
        {
            Encounters=new List<Encounter>();
        }


        public override void UpdateFrom(SyncEntity other)
        {   
            base.UpdateFrom(((Patient)other));
            Partner = ((Patient)other).Partner;
            Estimateddob =((Patient)other).Estimateddob;
            Idtype =((Patient)other).Idtype;
            Enrollmenttime =((Patient)other).Enrollmenttime;
            Enrollmentdate =((Patient)other).Enrollmentdate;
            Kinrelationother = ((Patient) other).Kinrelationother;
            Kinrelation =((Patient)other).Kinrelation;
            Kinphone =((Patient)other).Kinphone;
            Kin =((Patient)other).Kin;
            Contactphone =((Patient)other).Contactphone;
            Clientcode =((Patient)other).Clientcode;
            Dob =((Patient)other).Dob;
            Sex =((Patient)other).Sex;
            Lastname =((Patient)other).Lastname;
            Middlename =((Patient)other).Middlename;
            Firstname =((Patient)other).Firstname;
            PartnerUuid= ((Patient)other).PartnerUuid;
        }

        public void AddEncounter(Encounter encounter)
        {
            encounter.Patient = this;
            this.Encounters.Add(encounter);
        }
        public void AddEncounters(IEnumerable<Encounter> encounters)
        {
            foreach (var encounter in encounters)
            {
                AddEncounter(encounter);
            }
        }

        public int GetDobEstimated()
        {
            return Estimateddob ? 1 : 0;
        }
       
        public override string ToString()
        {
            return $"{Firstname} {Middlename} {Lastname}";
        }
        public string ToStringDetail()
        {
            return $"{Firstname} {Middlename} {Lastname} |{Idtype}|{Clientcode}|{UuId}|{IqcareId}|{Id}";
        }
    }
}

