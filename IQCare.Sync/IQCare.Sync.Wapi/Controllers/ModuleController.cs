using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Reflection;
using System.Web.Http;
using IQCare.Sync.Core.Interfaces;
using log4net;
using Module = IQCare.Sync.Core.Model.Module;

namespace IQCare.Sync.Wapi.Controllers
{
    public class ModuleController : ApiController
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly IModuleService _moduleService;

        public ModuleController(IModuleService moduleService)
        {
            _moduleService = moduleService;
        }

        // GET: api/Module
        public IEnumerable<Module> Get()
        {
            Log.Debug("get called!");
            var modules = _moduleService.GetAll();
            return modules.ToList();
        }

    }
}
