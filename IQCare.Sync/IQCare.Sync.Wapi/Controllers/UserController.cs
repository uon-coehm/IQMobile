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
    public class UserController : ApiController
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private readonly IUserService _userService;

        public UserController(IUserService userService)
        {
            _userService = userService;
        }

        // GET: api/User
        public IEnumerable<User> Get()
        {
            Log.Debug("get called!");
            var users = _userService.GetAll();
            return users.ToList();
        }
    }
}
