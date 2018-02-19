using System.Web;
using System.Web.Mvc;

namespace IQCare.Sync.Wapi
{
    public class FilterConfig
    {
        public static void RegisterGlobalFilters(GlobalFilterCollection filters)
        {
            filters.Add(new HandleErrorAttribute());
        }
    }
}
