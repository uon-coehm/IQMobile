using System;
using System.Data.Entity;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Interfaces.UoW;

namespace IQCare.Sync.Infrastructure.Repository
{
    public class UnitOfWork : IUnitOfWork
    {
        private SyncContext _context;
        private IUserRepository _userRepository;
        private IModuleRepository _moduleRepository;
        private IEncounterTypeRepository _encounterTypeRepository;
        private ILookupRepository _lookupRepository;
        private IMDataTypeMapRepository _mDataTypeMapRepository;
        private IMConceptRepository _mConceptRepository;
        private IPatientRepository _patientRepository;
        private IEncounterRepository _encounterRepository;
        private ILookupHtsRepository _lookupHtsRepository;

        public DbContext Context
        {
            get { return _context; }
        }

        public IUserRepository UserRepository
        {
            get { return _userRepository ?? (_userRepository = new UserRepository(_context)); }
        }

        public IModuleRepository ModuleRepository
        {
            get { return _moduleRepository ?? (_moduleRepository = new ModuleRepository(_context)); }
        }

        public IEncounterTypeRepository EncounterTypeRepository
        {
            get{ return _encounterTypeRepository ?? (_encounterTypeRepository = new EncounterTypeRepository(_context));}
        }

        public ILookupRepository LookupRepository
        {
            get { return _lookupRepository ?? (_lookupRepository = new LookupRepository(_context)); }
        }

        public IMDataTypeMapRepository MDataTypeMapRepository
        {
            get { return _mDataTypeMapRepository ?? (_mDataTypeMapRepository = new MDataTypeMapRepository(_context)); }
        }

        public IMConceptRepository MConceptRepository
        {
            get { return _mConceptRepository ?? (_mConceptRepository = new MConceptRepository(_context)); }
        }

        public IPatientRepository PatientRepository
        {
            get { return _patientRepository ?? (_patientRepository = new PatientRepository(_context)); }
        }

        public IEncounterRepository EncounterRepository
        {
            get { return _encounterRepository ?? (_encounterRepository = new EncounterRepository(_context)); }
        }

        public ILookupHtsRepository LookupHtsRepository
        {
            get { return _lookupHtsRepository ?? (_lookupHtsRepository = new LookupHtsRepository(_context)); }
        }

        public UnitOfWork(SyncContext context)
        {
            if (null == context)
            {
                throw new ArgumentNullException("Context was not supplied");
            }
            _context = context;
        }

        public virtual int Commit()
        {
            return _context.SaveChanges();
        }

        public void Refresh(bool savechanges)
        {
            if (savechanges)
            {
                _context.SaveChanges();
            }
            _context=new SyncContext();
        }

        public virtual void CommitSqlCommand()
        {
            _context.SaveChanges();
            _context.Dispose();
            _context = new SyncContext();
        }

        public int ExecuteQuery(string sql)
        {
            return _context.Database.ExecuteSqlCommand(sql);
        }

        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }

        private void Dispose(bool disposing)
        {
            if (disposing)
            {
                _context?.Dispose();
            }
        }
    }
}
