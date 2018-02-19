using System;
using System.Linq;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Infrastructure.Repository
{
    public class MDataTypeMapRepository : Repository<MDataTypeMap>, IMDataTypeMapRepository
    {
        public MDataTypeMapRepository(SyncContext context) : base(context)
        {
        }
    }
}