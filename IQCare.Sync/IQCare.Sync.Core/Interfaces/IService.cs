using System;
using System.Collections.Generic;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Interfaces
{
    public interface IService<T>
    {
        IEnumerable<T> GetAll();
        void Sync(T entity);
        void Delete(Guid uuid);
        void SaveChanges();
    }
}