using System;
using System.Data.Entity;
using IQCare.Sync.Core.Interfaces.Repository;

namespace IQCare.Sync.Core.Interfaces.UoW
{
    public interface IUnitOfWork : IDisposable
    {
        DbContext Context { get; }
        IUserRepository UserRepository { get; }
        IModuleRepository ModuleRepository { get; }
        IEncounterTypeRepository EncounterTypeRepository { get; }
        ILookupRepository LookupRepository { get; }
        IMDataTypeMapRepository MDataTypeMapRepository { get; }
        IMConceptRepository MConceptRepository { get; }
        IPatientRepository PatientRepository { get; }
        IEncounterRepository EncounterRepository { get; }
        ILookupHtsRepository LookupHtsRepository { get; }
        int Commit();
        void Refresh(bool savechanges);
        void CommitSqlCommand();
        int ExecuteQuery(string sql);
    }
}