using IQCare.Sync.Shared;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using log4net;
using Newtonsoft.Json;

namespace IQCare.Sync.Core.Model
{
    public class User : SyncEntity
    {
        private string _password;

        [JsonProperty("username")]
        public string Username { get; set; }

        [JsonProperty("password")]
        public string Password
        {
            get { return GetDecryptedPassword(); }
            set { _password = value; }
        }

        [JsonProperty("counsellorcode")]
        public string CounsellorCode { get; set; }


        public string GetDecryptedPassword()
        {
            if (Id > -1)
            {
                try
                {
                    _password = Utility.Decrypt(_password);
                }
                catch 
                {
                    //Debug.Print($"{ex.Message} |{Username} {_password}");
                }
            }
            return _password;
        }

        public override string ToString()
        {
            return $"{Username} {CounsellorCode}";
        }

        public string ToStringDetail()
        {
            return $"{Username}|{CounsellorCode}|{UuId}|{IqcareId}|{Id}";
        }

        public override void UpdateFrom(SyncEntity other)
        {
            base.UpdateFrom(other);
            CounsellorCode = ((User) other).CounsellorCode;
        }
    }
}

