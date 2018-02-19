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
    public class LookupService : ILookupService
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly IUnitOfWork _uow;

        public LookupService(IUnitOfWork uow)
        {
            _uow = uow;
        }

        public IEnumerable<Lookup> GetAll()
        {
            return _uow.LookupRepository.GetAll();
        }
        public IEnumerable<MDataTypeMap> GetAllDataType()
        {
            return _uow.MDataTypeMapRepository.GetAll();
        }
    }
}
