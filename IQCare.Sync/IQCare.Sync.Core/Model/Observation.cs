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
    public class Observation : SyncEntity
    {
        [JsonProperty("obsDate")]
        public DateTime ObsDate { get; set; }
       
        [JsonProperty("valueText")]
        public string ValueText { get; set; }

        [JsonProperty("valueMultipleChoice")]
        public string ValueMultipleChoice { get; set; }

        [JsonProperty("valueNumeric")]
        public int? ValueNumeric { get; set; }

        [JsonProperty("valueDecimal")]
        public decimal? ValueDecimal { get; set; }

        [JsonProperty("valueLookup")]
        public int? ValueLookup { get; set; }

        [JsonProperty("valueDate")]
        public DateTime? ValueDate { get; set; }

        [JsonProperty("valueDateTime")]
        public DateTime? ValueDateTime { get; set; }

        [JsonProperty("valueBoolean")]
        public bool? ValueBoolean { get; set; }

        [NotMapped]
        public int MConceptPk { get; set; }

        [Column("concept")]
        [ForeignKey("MConcept")]
        [JsonIgnore]
        public int MConceptId { get; set; }
        [JsonProperty("mConcept")]
        public virtual MConcept MConcept { get; set; }

        [NotMapped]
        public int EncounterPk { get; set; }
        [Column("encounter")]
        [ForeignKey("Encounter")]
        [JsonIgnore]
        public int EncounterId { get; set; }
        [JsonProperty("encounter")]
        public virtual Encounter Encounter { get; set; }


    
        //[JsonProperty("recordId")]
        //public int RecordId { get; set; }
        public string ObsvalueString()
        {
            /*
            TEXT, TEXTMULTI,NUMERIC, NUMERICDECIMAL,
            SELECT, MULTISELECT,DATETIME, DATE, TIME,YESNO           
            */

            string obs = "";

            if (MConcept.DataTypeMap.DataType == "TEXT")
            {
                obs = ValueText;
            }
            if (MConcept.DataTypeMap.DataType == "TEXTMULTI")
            {
                obs = ValueText;
            }
            if (MConcept.DataTypeMap.DataType == "NUMERIC")
            {
                obs = ValueNumeric.ToString();
            }
            if (MConcept.DataTypeMap.DataType == "NUMERICDECIMAL")
            {
                obs = ValueDecimal.ToString();
            }
            if (MConcept.DataTypeMap.DataType == "DATE")
            {
                //obs = ValueDate?.ToString("dd MMM yyyy");
                obs = ValueText;
            }
            if (MConcept.DataTypeMap.DataType == "DATETIME")
            {
                //obs = ValueDateTime?.ToString("dd MMM yyyy HH:mm:ss");
                obs = ValueText;
            }
            if (MConcept.DataTypeMap.DataType == "SELECT")
            {
                obs = ValueNumeric.ToString();
            }
            if (MConcept.DataTypeMap.DataType == "MULTISELECT")
            {
                obs = ValueMultipleChoice;
            }
            if (MConcept.DataTypeMap.DataType == "YESNO")
            {
                obs = "0";
                if (ValueBoolean.HasValue && ValueBoolean.Value)
                {
                    obs = "1";
                }
            }
            return obs;
        }
        public string ObsvalueSqlString()
        {
            /*
            TEXT, TEXTMULTI,NUMERIC, NUMERICDECIMAL,
            SELECT, MULTISELECT,DATETIME, DATE, TIME,YESNO           
            */

            string obs = ObsvalueString();
            if (!(
                MConcept.DataTypeMap.DataType == "NUMERIC" ||
                MConcept.DataTypeMap.DataType == "NUMERICDECIMAL" ||
                MConcept.DataTypeMap.DataType == "SELECT" ||
                MConcept.DataTypeMap.DataType == "YESNO"))
            {
                return $"'{obs}'";
            }
            return obs;
        }

        public static Observation CreateFrom(Observation observation)
        {
            var obs = new Observation();

            obs.ObsDate = observation.ObsDate;
            obs.MConceptId = observation.MConceptId;
            obs.ValueText = observation.ValueText;
            obs.ValueMultipleChoice = observation.ValueMultipleChoice;
            obs.ValueNumeric = observation.ValueNumeric;
            obs.ValueDecimal = observation.ValueDecimal;
            obs.ValueLookup = observation.ValueLookup;
            obs.ValueDate = observation.ValueDate;
            obs.ValueDateTime = observation.ValueDateTime;
            obs.ValueBoolean = observation.ValueBoolean;
            obs.UuId = observation.UuId;

            return obs;
        }
        

        public string[] GetMultipleChoices()
        {
            return ValueMultipleChoice.Split(',');
        }
        public override string ToString()
        {   
            return $"{Encounter}|{MConcept}|{ObsvalueString()}";
        }
    }
}
