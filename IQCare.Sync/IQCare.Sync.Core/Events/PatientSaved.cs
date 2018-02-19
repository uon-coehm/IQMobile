using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Events
{
    public class PatientSaved :EventArgs,  ISyncEvent
    {
        public DateTime DateOccurred { get;  }
        public Patient Patient { get; set; }
        public List<Encounter> Encounters { get; set; }

        public PatientSaved(Patient patient)
        {
            DateOccurred = DateTime.Now;
            Patient = patient;
        }
        public PatientSaved(Patient patient, List<Encounter> encounters):this(patient)
        {
            Encounters = encounters;
        }
    }
}
