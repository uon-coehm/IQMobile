using System;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Interfaces.Repository
{
    public interface IPatientRepository : IRepository<Patient>
    {
        void UpdatePartnerLink(Guid patientUuid,Guid? partnerUuid);
    }
}