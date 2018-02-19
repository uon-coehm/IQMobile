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
    // ReSharper disable once InconsistentNaming
    public class IQConcept : SyncEntity
    {
        public string Fieldname { get; set; }
        public string Tablename { get; set; }
        public string Iqtype { get; set; }
        public bool Custom { get; set; }
        [Column("MConceptId")]
        public int? MConceptId { get; set; }
        public virtual MConcept MConcept { get; set; }

        public bool IsMultiInsert()
        {
            return HasTable() && Iqtype.Trim().ToLower() == "Multi Select".Trim().ToLower();
        }
        public bool HasTable()
        {
            return !string.IsNullOrWhiteSpace(Tablename) && !string.IsNullOrWhiteSpace(Fieldname);
        }
        public override string ToString()
        {
            return $"{Tablename}.{Fieldname}";
        }
    }
}
