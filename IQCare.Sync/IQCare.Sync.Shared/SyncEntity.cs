using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Data.SqlTypes;
using System.IO;
using System.Linq;
using System.Resources;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace IQCare.Sync.Shared
{
    //[Serializable]
    public abstract class SyncEntity
    {
        private readonly string dateFormat = "dd MMM yyyy";

        [JsonProperty("uuid")]
        public Guid UuId { get; set; }

        [JsonProperty("id")]
        public int Id { get; set; }

        [JsonProperty("userid")]
        public int? UserId { get; set; }

        [JsonProperty("iqcareid")]
        public int? IqcareId { get; set; }

        [JsonProperty("syncstate")]
        public int? SyncState { get; set; }

        [JsonProperty("updatedate")]
        public DateTime? UpdateDate { get; set; }

        protected SyncEntity()
        {
            UuId = Guid.NewGuid();
            UpdateDate = DateTime.Now;
            SyncState = 1;
            UserId = -1;
            IqcareId = -1;
        }

        public virtual void UpdateFrom(SyncEntity other)
        {
            UpdateDate = DateTime.Now;
            SyncState = 1;
            UserId = other.UserId;
        }

        public virtual string DateString(DateTime dateTime)
        {
            return dateTime.ToString(dateFormat);
        }

        public override bool Equals(object obj)
        {
            SyncEntity entity = obj as SyncEntity;
            if (entity == null)
                return false;
            else
                return UuId.Equals(entity.UuId);

        }

        public static DateTime GetValidDate(Object value)
        {
            var date = (DateTime) value;
            if (date < SqlDateTime.MinValue.Value)
            {
                return SqlDateTime.MinValue.Value;
            }
            else if (date > SqlDateTime.MaxValue.Value)
            {
                return SqlDateTime.MaxValue.Value;
            }
            return new DateTime(1900, 1, 1);
        }
    }
}
