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
    /*
SELECT TOP 1000 [id]
      ,[type]
      ,[testresultcode]
      ,[iqcode]
      ,[fieldname]
      ,[tablename]
      ,[uuid]
FROM[iqcare_sync].[dbo].[lookuphts]
*/
    // ReSharper disable once InconsistentNaming
    public class LookupHTS
    {
        public int Id { get; set; }
        public string Type { get; set; }
        public int Testresultcode { get; set; }
        public int Iqcode { get; set; }
        public string Fieldname { get; set; }
        public string Tablename { get; set; }
        public Guid UuId { get; set; }

        public override string ToString()
        {
            return $"{Type}|{Testresultcode}|{Iqcode}";
        }
    }
}
