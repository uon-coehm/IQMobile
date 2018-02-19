using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Shared;
using log4net;
using Newtonsoft.Json;

namespace IQCare.Sync.Core.Model
{
    public class PateintDTO
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        [JsonProperty("patient")]
        public Patient Patient { get; set; }

        [JsonProperty("encounters")]
        public List<Encounter> Encounters { get; set; }

        [JsonProperty("observations")]
        public List<Observation> Observations { get; set; }

        public PateintDTO()
        {
            Encounters = new List<Encounter>();
            Observations = new List<Observation>();
        }

        public static PateintDTO Create(Patient patient)
        {
            PateintDTO patientDto = new PateintDTO();
            patientDto.Observations=GenerateObservationDto(patient);
            patientDto.Encounters=GenerateEncounterDto(patient);
            patientDto.Patient=GeneratePatientDto(patient);


            return patientDto;
        }

        public Patient GeneratePatient(string storein)
        {
            var patient = new Patient();
            try
            {
                patient = Patient;
                patient.Id = 0;

                var partner = Patient.Partner;

                if (null != partner)
                {
                    patient.PartnerUuid = partner.UuId;
                    patient.Partner = null;
                }

                patient.Encounters = GenerateEncounters(Encounters);

                if (storein.ToLower().Trim() != "-")
                {
                    Utility.StoreMessage(patient, storein, patient.UuId.ToString());
                }
            }
            catch (Exception ex)
            {
                Log.Debug(ex);
            }
            return patient;
        }
        private List<Encounter> GenerateEncounters( List<Encounter> encounters)
        {
            List<Encounter> es = new List<Encounter>();

            foreach (var e in encounters)
            {
                e.Id = 0;
                e.PatientId = 0;
                e.EncounterTypeId = e.EncounterTypePk;
                e.Observations = GenerateObservations(Observations);
                es.Add(e);
            }
            return es;
        }
        private List<Observation> GenerateObservations( List<Observation> observations)
        {
            List<Observation> obs = new List<Observation>();

            foreach (var o in observations)
            {
                o.Id = 0;
                o.EncounterId = 0;
                o.MConceptId = o.MConceptPk;
                obs.Add(o);
            }
            return obs;
        }

        private static Patient GeneratePatientDto(Patient patient)
        {
            patient.Encounters=null;
            return patient;
        }
        private static List<Encounter> GenerateEncounterDto(Patient patient)
        {
            List<Encounter> encounterList = new List<Encounter>();
            foreach (var e in patient.Encounters)
            {
                e.Patient=null;
                e.PatientPk = patient.Id;
                e.EncounterTypePk = e.EncounterTypeId;
                e.EncounterType=null;
                e.Observations=null;
                encounterList.Add(e);
            }
            return encounterList;
        }
        private static List<Observation> GenerateObservationDto(Patient patient)
        {

            List<Observation> observationList = new List<Observation>();
            foreach (var e in patient.Encounters)
            {

                foreach (var o in e.Observations)
                {
                    o.EncounterPk = o.EncounterId;
                    o.Encounter = null;
                    o.MConceptPk = o.MConceptId;
                    o.MConcept = null;
                    observationList.Add(o);
                }
            }

            return observationList;
        }

        public override string ToString()
        {
            var encounter = this.Patient.Encounters.FirstOrDefault();
            string encounterInfo=encounter?.ToString() ?? "0";
            string obsInfo= encounter?.Observations.Count.ToString() ?? "0";

            return $"{this.Patient} ||Encounters[{encounterInfo}] Observations [{obsInfo}]";
        }
    }
}
