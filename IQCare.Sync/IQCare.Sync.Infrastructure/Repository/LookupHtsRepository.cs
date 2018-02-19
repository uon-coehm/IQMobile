using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Infrastructure.Repository
{
    public class LookupHtsRepository :  ILookupHtsRepository
    {
        internal readonly SyncContext Context;

        public LookupHtsRepository(SyncContext context)
        {
            Context = context;
        }

        public virtual IEnumerable<LookupHTS> GetAll()
        {
            return Context.LookupsHts;
        }
    }
}