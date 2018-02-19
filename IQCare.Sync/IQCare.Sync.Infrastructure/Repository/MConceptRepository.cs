using System;
using System.Collections.Generic;
using System.Linq;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Infrastructure.Repository
{
    public class MConceptRepository : Repository<MConcept>, IMConceptRepository
    {
        public MConceptRepository(SyncContext context) : base(context)
        {
        }

        public IEnumerable<MConcept> GetAllByEncounterType(int encounterTypeId)
        {
            return GetAll().Where(x => x.EncounterTypeId == encounterTypeId);
        }
    }
}