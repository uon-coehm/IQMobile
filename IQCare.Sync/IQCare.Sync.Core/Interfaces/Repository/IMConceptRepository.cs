using System.Collections.Generic;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Interfaces.Repository
{
    public interface IMConceptRepository : IRepository<MConcept>
    {
        IEnumerable<MConcept> GetAllByEncounterType(int encounterTypeId);
    }
}