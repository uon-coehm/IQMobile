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
    public class Module : SyncEntity
    {
        [JsonProperty("name")]
        public string Name { get; set; }
        [JsonProperty("display")]
        public string Display { get; set; }
        [JsonProperty("displayshort")]
        public string Displayshort { get; set; }
        [JsonProperty("icon")]
        public string Icon { get; set; }
        [JsonProperty("encounterTypes")]
        public virtual ICollection<EncounterType> EncounterTypes { get; set; }

        public Module()
        {
            EncounterTypes=new List<EncounterType>();
        }

        public override string ToString()
        {
            return $"{Name}";
        }

        public void AddEncounterType(EncounterType encounterType)
        {
            encounterType.Module = this;
            EncounterTypes.Add(encounterType);
        }
    }
}
