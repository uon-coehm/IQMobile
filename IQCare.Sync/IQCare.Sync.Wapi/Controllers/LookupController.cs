using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Reflection;
using System.Web.Http;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Model;
using log4net;

namespace IQCare.Sync.Wapi.Controllers
{
    public class LookupController : ApiController
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly ILookupService _lookupService;

        public LookupController(ILookupService lookupService)
        {
            _lookupService = lookupService;
        }


        // GET: api/Lookup
        public IEnumerable<Lookup> Get()
        {
            Log.Debug("get called!");
            var lookups = _lookupService.GetAll()
                .OrderBy(x => x.Codeid)
                .ThenBy(x => x.Rank);
                
            return lookups.ToList();
        }


        
    }
}
