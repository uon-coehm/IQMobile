using System;
using System.Collections.Generic;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Interfaces
{
    public interface ILookupService
    {
        IEnumerable<Lookup> GetAll();
        IEnumerable<MDataTypeMap> GetAllDataType();
    }
}