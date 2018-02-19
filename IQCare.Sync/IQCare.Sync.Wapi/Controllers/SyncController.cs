using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace IQCare.Sync.Wapi.Controllers
{
    public class SyncController : ApiController
    {
        // GET: api/Sync
        public string Get()
        {
            return "IQMobile Server is Online!";
        }

    }
}
