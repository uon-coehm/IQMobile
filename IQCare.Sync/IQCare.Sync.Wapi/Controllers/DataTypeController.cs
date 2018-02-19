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
    public class DataTypeController : ApiController
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly ILookupService _lookupService;

        public DataTypeController(ILookupService lookupService)
        {
            _lookupService = lookupService;
        }


        // GET: api/DataType
        public IEnumerable<MDataTypeMap> Get()
        {
            Log.Debug("get called!");
            var dataTypes = _lookupService.GetAllDataType();
            return dataTypes.ToList();
        }
    }
}
