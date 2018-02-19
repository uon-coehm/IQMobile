using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Infrastructure.Repository
{
  public  class EncounterRepository : Repository<Encounter>, IEncounterRepository
    {
      public EncounterRepository(SyncContext context) : base(context)
      {
      }
    }
}
