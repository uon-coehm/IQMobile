using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Events
{
    public class EncounterCreated :EventArgs,  ISyncEvent
    {
        public DateTime DateOccurred { get;  }
        public int PatientId { get; set; }
        public Guid PatientSyncId { get; set; }
        public Patient Patient { get; set; }
        public Encounter Encounter { get; set; }
        public IQVisitType VisitType { get; set; }
        public IQLocation Location { get; set; }
        public List<MConcept> MConcepts { get; set; }
        public List<LookupHTS> LookupsHts { get; set; }

        public EncounterCreated(IQVisitType visitType, Encounter encounter, IQLocation location, List<MConcept> mConcepts, List<LookupHTS> lookupshts)
        {
            DateOccurred = DateTime.Now;
            VisitType = visitType;
            Encounter = encounter;
            Location = location;
            MConcepts = mConcepts;
            LookupsHts = lookupshts;
        }
        public EncounterCreated(Patient patient, IQVisitType visitType, Encounter encounter, IQLocation location, List<MConcept> mConcepts, List<LookupHTS> lookupshts) : this(visitType, encounter, location, mConcepts, lookupshts)
        {
            Patient = patient;
        }
        public EncounterCreated(int patientId, IQVisitType visitType, Encounter encounter, IQLocation location, List<MConcept> mConcepts, List<LookupHTS> lookupshts) :this(visitType,encounter,location, mConcepts, lookupshts)
        {
            PatientId = patientId;
        }
        public EncounterCreated(Guid patientSyncId, IQVisitType visitType, Encounter encounter, IQLocation location, List<MConcept> mConcepts, List<LookupHTS> lookupshts) : this(visitType, encounter, location, mConcepts, lookupshts)
        {
            PatientSyncId = patientSyncId;
        }
    
    }
}
