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
    public class Lookup : SyncEntity
    {
        [JsonProperty("name")]
        public string Name { get; set; }
        [JsonProperty("codeid")]
        public int Codeid { get; set; }
        [JsonProperty("rank")]
        public decimal Rank { get; set; }
        [JsonProperty("isloner")]
        public bool Isloner { get; set; }
        [JsonProperty("isother")]
        public bool Isother { get; set; }

        public override string ToString()
        {
            return $"{Name}|{Codeid}|{Rank}";
        }
    }
}
