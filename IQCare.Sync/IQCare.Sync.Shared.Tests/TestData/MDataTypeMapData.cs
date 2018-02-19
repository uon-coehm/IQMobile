using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Interfaces.UoW;
using IQCare.Sync.Core.Model;
using IQCare.Sync.Infrastructure;
using IQCare.Sync.Infrastructure.Repository;

namespace IQCare.Sync.Shared.Tests.TestData
{

    public class MDataTypeMapData
    {
        private static IUnitOfWork _uow = new UnitOfWork(new SyncContext());
       
        public static List<MDataTypeMap> GetMDataTypeMaps()
        {
           var  list=_uow.MDataTypeMapRepository.GetAll().ToList();
          
            return list;
        }
       
    }
}
