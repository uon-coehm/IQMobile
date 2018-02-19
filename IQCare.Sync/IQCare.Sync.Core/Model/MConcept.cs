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
    public class MConcept : SyncEntity
    {
        [JsonProperty("display")]
        public string Display { get; set; }
        [JsonProperty("description")]
        public string Description { get; set; }
        [JsonProperty("rank")]
        public decimal Rank { get; set; }
        [JsonProperty("lookupcodeid")]
        public int Lookupcodeid { get; set; }
        [JsonProperty("required")]
        public bool Required { get; set; }
        [JsonProperty("parentIqcareId")]
        public int ParentIqcareId { get; set; }
        [JsonProperty("parentcondition")]
        public string Parentcondition { get; set; }
        [JsonProperty("patientgender")]
        public int Patientgender { get; set; }
        [JsonProperty("autcompute")]
        public bool Autcompute { get; set; }
        [JsonProperty("autcomputelogic")]
        public string Autcomputelogic { get; set; }
        [JsonProperty("autcomputeparentchildren")]
        public string Autcomputeparentchildren { get; set; }
        [JsonProperty("skipto")]
        public string Skipto { get; set; }
        [JsonProperty("skiptocondition")]
        public string Skiptocondition { get; set; }
        [JsonProperty("parentconditionchildren")]
        public string Parentconditionchildren { get; set; }
        [JsonProperty("skiptoparent")]
        public string Skiptoparent { get; set; }
        [JsonProperty("skiptoparentcondition")]
        public string Skiptoparentcondition { get; set; }

        [Column("parent_id")]
        [JsonIgnore]
        public int? ParentId { get; set; }
        [JsonProperty("parent")]
        public virtual MConcept Parent { get; set; }


        [NotMapped]       
        public int DataTypeMapPk { get; set; }


        [Column("dataTypeMap_id")]
        [ForeignKey("DataTypeMap")]
        [JsonIgnore]
        public int DataTypeMapId { get; set; }
        [JsonProperty("dataTypeMap")]
        public virtual MDataTypeMap DataTypeMap { get; set; }

        [Column("encounterType_id")]
        [ForeignKey("EncounterType")]
        [JsonIgnore]
        public int EncounterTypeId { get; set; }
        [JsonProperty("encounterType")]
        public virtual  EncounterType EncounterType { get; set; }

        [JsonIgnore]
        public virtual ICollection<MConcept> ChildrenConcepts { get; set; }

        [JsonIgnore]
        public virtual ICollection<IQConcept> IQConcepts { get; set; }

        public MConcept()
        {
            ChildrenConcepts=new List<MConcept>();
            IQConcepts=new List<IQConcept>();
        }

        public bool IsMultiInsert()
        {
            var concept = IQConcepts.FirstOrDefault();
            if (null != concept)
            {
                return concept.IsMultiInsert();
            }
            return false;
        }

        public bool HasLookup()
        {
            return Lookupcodeid > 0;
        }
        public override string ToString()
        {
            return $"{Display} | {Description} |{DataTypeMap?.DataType}";
        }
    }
}
