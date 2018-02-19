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
    public class EncounterType : SyncEntity
    {
        [JsonProperty("name")]
        public string Name { get; set; }
        [JsonProperty("display")]
        public string Display { get; set; }
        [JsonProperty("displayshort")]
        public string Displayshort { get; set; }
        [Column("module_id")]
        [ForeignKey("Module")]
        [JsonIgnore]
        public int ModuleId { get; set; }
        [JsonProperty("module")]
        public virtual Module Module { get; set; }
        [JsonIgnore]
        public virtual ICollection<MConcept> Concepts { get; set; }

        public EncounterType()
        {
            Concepts=new List<MConcept>();
        }

        public void AddConcept(MConcept concept)
        {
            concept.EncounterType = this;
            Concepts.Add(concept);
        }

        public override string ToString()
        {
            return $"{Name}";
        }
    }

}
