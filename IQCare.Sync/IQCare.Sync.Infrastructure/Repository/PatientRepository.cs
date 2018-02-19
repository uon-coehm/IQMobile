using System;
using System.Linq;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Infrastructure.Repository
{
    public class PatientRepository : Repository<Patient>, IPatientRepository
    {
        public PatientRepository(SyncContext context) : base(context)
        {
        }

        public void UpdatePartnerLink(Guid patientUuid, Guid? partnerUuid)
        {
            //TODO :update partner linkages
            if (partnerUuid.HasValue)
            {
                var partner = FindBySyncId(partnerUuid.Value);
                if (null != partner)
                {
                    
                }
                return;                
            }
            


        }
    }
}