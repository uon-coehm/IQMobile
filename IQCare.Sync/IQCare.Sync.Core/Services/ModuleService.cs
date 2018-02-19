using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.UoW;
using IQCare.Sync.Core.Model;
using log4net;
using Module = IQCare.Sync.Core.Model.Module;

namespace IQCare.Sync.Core.Services
{
    public class ModuleService : IModuleService
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly IUnitOfWork _uow;

        public ModuleService(IUnitOfWork uow)
        {
            _uow = uow;
        }

        public IEnumerable<Module> GetAll()
        {
            return _uow.ModuleRepository.GetAll();
        }
    }
}
