using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Shared;
using Newtonsoft.Json;

namespace IQCare.Sync.Core.Model
{
    public class Encounter : SyncEntity
    {
        [JsonProperty("startdate")]
        public DateTime Startdate { get; set; }
        [NotMapped]
        [JsonIgnore]
        public string StartdateString
        {
            get { return DateString(Startdate); }
        }

        [JsonProperty("starttime")]
        public DateTime Starttime { get; set; }
        [NotMapped]
        [JsonIgnore]
        public string StarttimeString
        {
            get { return DateString(Starttime); }
        }

        [JsonProperty("geoloctaion")]
        public String Geoloctaion { get; set; }

        [JsonProperty("completed")]
        public bool Completed { get; set; }

        [Column("patient_id")]
        [ForeignKey("Patient")]
        [JsonIgnore]
        public int PatientId { get; set; }

        [NotMapped]
        public int PatientPk { get; set; }


        [JsonProperty("patient")]
        public virtual Patient Patient { get; set; }

        [Column("encounterType_id")]
        [ForeignKey("EncounterType")]
        [JsonIgnore]
        public int EncounterTypeId { get; set; }
        [NotMapped]        
        public int EncounterTypePk { get; set; }


        [JsonProperty("encounterType")]
        public virtual EncounterType EncounterType { get; set; }

        [JsonProperty("observations")]
        public virtual ICollection<Observation> Observations { get; set; }

     

        public Encounter()
        {
            Observations = new List<Observation>();
        }

        public void AddObservation(Observation observation)
        {
            observation.Encounter = this;
            Observations.Add(observation);
        }
        public void AddObservations(List<Observation> observations)
        {
            foreach (var o in observations)
            {
                AddObservation(o);
            }
        }

        public override void UpdateFrom(SyncEntity other)
        {
            Encounter encounter = (Encounter) other;
            base.UpdateFrom(encounter);
           
            Startdate = encounter.Startdate;
            Starttime = encounter.Starttime;
            Geoloctaion = encounter.Geoloctaion;
            Completed = encounter.Completed;
            PatientId = encounter.PatientId;
            Patient = null;
            EncounterTypeId = encounter.EncounterTypeId;
            EncounterType = null;
            Observations=new List<Observation>();
            foreach (var observation in encounter.Observations)
            {                             
                observation.Encounter = null;
                observation.MConcept = null;
                Observations.Add(observation);
            }
            
        }

        public static Encounter CreateFrom(Encounter other)
        {
            var encounter = new Encounter();

            encounter.Startdate = other.Startdate;
            encounter.Starttime = other.Starttime;
            encounter.Geoloctaion = other.Geoloctaion;
            encounter.Completed = other.Completed;
            encounter.PatientId = other.PatientId;
            encounter.EncounterTypeId = other.EncounterTypeId;
            encounter.UuId = other.UuId;
            foreach (var observation in other.Observations)
            {
                encounter.AddObservation(Observation.CreateFrom(observation));
            }

            return encounter;
        }

        public override string ToString()
        {
            return $"{base.Id}|{EncounterType}|{Startdate.ToString("dd MMM yyyy")}|{Patient}";
        }
    }

}
