using System.Collections.Generic;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Interfaces.Repository
{
    public interface ILookupHtsRepository 
    {
        IEnumerable<LookupHTS> GetAll();
    }
}