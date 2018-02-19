using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Events
{
    public class PatientCreated :EventArgs,  ISyncEvent
    {
        public DateTime DateOccurred { get;  }
        public Patient Patient { get; set; }
        public IQLocation Location { get; set; }
        public IQModule Module{ get; set; }
        public IQVisitType VisitType { get; set; }

        public PatientCreated(Patient patient,IQLocation location)
        {
            Patient = patient;
            Location = location;
            DateOccurred = DateTime.Now;

            //TODO: Remove hard codede ModuleID,VisitType,LocationID values

            Module = new IQModule() {ModuleID = 300};
            VisitType = new IQVisitType() {VisitTypeID = 10001};
        }
    }
}
