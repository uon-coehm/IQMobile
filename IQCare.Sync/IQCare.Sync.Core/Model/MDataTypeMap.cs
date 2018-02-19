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
    public class MDataTypeMap : SyncEntity
    {
        [JsonProperty("iqType")]
        public string IqType { get; set; }
        [JsonProperty("dataType")]
        public string DataType { get; set; }
     
        public override string ToString()
        {
            return $"{IqType}|{DataType}";
        }
    }
}
