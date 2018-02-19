using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using log4net;

namespace IQCare.Sync.Wapi.Tests.Controllers
{
   public class BaseControllerTest
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        public BaseControllerTest()
        {
            Log.Debug("IQCare.Sync.Wapi.Tests init");
        }
    }
}
