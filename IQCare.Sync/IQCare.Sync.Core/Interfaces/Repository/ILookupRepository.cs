using System.Collections.Generic;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Interfaces.Repository
{
    public interface ILookupRepository : IRepository<Lookup>
    {
        IEnumerable<Lookup> FindByCodeId(int codeid);
    }
}