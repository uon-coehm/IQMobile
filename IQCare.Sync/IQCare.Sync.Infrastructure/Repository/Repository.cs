using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Core;
using System.Linq;
using System.Linq.Expressions;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Shared;

namespace IQCare.Sync.Infrastructure.Repository
{
    public abstract class Repository<T> : IRepository<T> where T : SyncEntity
    {
        internal readonly SyncContext Context;
        internal IDbSet<T> Dbset;

        public Repository(SyncContext context)
        {
            Context = context;
            Dbset = Context.Set<T>();
        }

        public T FindById(int id)
        {
            return Dbset.Find(id);
        }

        public T FindBySyncId(Guid uuid)
        {
            return GetAll().FirstOrDefault(x => x.UuId == uuid);
        }

        public virtual IEnumerable<T> GetAll()
        {
            return Dbset;
        }

        public void Save(T entity)
        {
            Dbset.Add(entity);
        }

        public void Save(List<T> entities)
        {
            foreach (var entity in entities)
            {
                Save(entity);    
            }
        }

        public void Update(T entity)
        {
            if (entity == null)
                throw new ArgumentNullException("entity");

            Dbset.Attach(entity);
            Context.Entry(entity).State = EntityState.Modified;
        }

        public void Update(List<T> entities)
        {
            foreach (T entity in entities)
            {
                Update(entity);
            }
        }

        public void Delete(Guid uuid)
        {
            var entity = FindBySyncId(uuid);
            if (entity == null)
                throw new ObjectNotFoundException("entity");
            Dbset.Remove(entity);
        }

        public void Dispose()
        {
            if (Context != null)
            {
                Context.Dispose();
            }
        }
    }
}
