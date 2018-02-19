using System;
using System.Collections.Generic;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Interfaces
{
    public interface IModuleService 
    {
        IEnumerable<Module> GetAll();
    }
}