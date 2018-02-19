using System;
using System.Collections.Generic;
using System.Linq;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Infrastructure.Repository
{
    public class LookupRepository : Repository<Lookup>, ILookupRepository
    {
        public LookupRepository(SyncContext context) : base(context)
        {
        }

        public IEnumerable<Lookup> FindByCodeId(int codeid)
        {
            return GetAll().Where(x => x.Codeid == codeid);
        }
    }
}