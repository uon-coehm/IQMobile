using System;
using System.Collections.Generic;

namespace IQCare.Sync.Core.Interfaces.Repository
{
    public interface IRepository<T>
    {
        T FindById(int id);
        T FindBySyncId(Guid uuid);
        IEnumerable<T> GetAll();
        void Save(T entity);
        void Save(List<T> entities);
        void Update(T entity);
        void Update(List<T> entities);
        void Delete(Guid uuid);
    }
}