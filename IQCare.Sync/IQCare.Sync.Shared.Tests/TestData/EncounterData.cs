using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Shared.Tests.TestData
{

    public class EncounterData
    {
        public static List<Encounter> GetTestEncounters()
        {
            var encounters = new List<Encounter>();

            var encounter = new Encounter();

            encounter.Startdate = DateTime.Today;
            encounter.Starttime = DateTime.Now;
            encounter.Geoloctaion = "NAKS";
            encounters.Add(encounter);

            return encounters;
        }
       

        public static Encounter GetTestEncounter()
        {
            var encounter = new Encounter();

            encounter.Startdate = DateTime.Today;
            encounter.Starttime = DateTime.Now;
            encounter.Geoloctaion = "NAKS";

            return encounter;
        }
        public static Encounter GetTestEncounterNew()
        {
            var encounter = new Encounter();

            encounter.Startdate = DateTime.Today.AddDays(-1);
            encounter.Starttime = DateTime.Now.AddDays(-1);
            encounter.Geoloctaion = "KSM";

            return encounter;
        }
        public static Observation GetTestObservationsText()
        {
            var observation = new Observation();
            observation.ObsDate = DateTime.Today;
            observation.ValueText = "Kimani";
            return observation;
        }
        public static Observation GetTestObservationsText(string obsval)
        {
            var observation = new Observation();
            observation.ObsDate = DateTime.Today;
            observation.ValueText = obsval;
            return observation;
        }
        public static Observation  GetTestObservationsNumeric()
        {
            var observation = new Observation();
            observation.ObsDate = DateTime.Today;
            observation.ValueNumeric = 33;
            return observation;
        }
        public static Observation GetTestObservationsNumeric(int obsval)
        {
            var observation = new Observation();
            observation.ObsDate = DateTime.Today;
            observation.ValueNumeric = obsval;
            return observation;
        }
        public static Observation GetTestObservationsNumericNew()
        {
            var observation = new Observation();
            observation.ObsDate = DateTime.Today;
            observation.ValueNumeric = 40;
            return observation;
        }
        public static Observation GetTestObservationsLookup()
        {
            var observation2 = new Observation();
            observation2.ObsDate = DateTime.Today;
            observation2.ValueNumeric = -1;
            return observation2;
        }
        public static Observation GetTestObservationsLookup(int obsval)
        {
            var observation2 = new Observation();
            observation2.ObsDate = DateTime.Today;
            observation2.ValueNumeric = obsval;
            return observation2;
        }
        public static Observation GetTestObservation(int obsval,MConcept concept)
        {
            var observation2 = new Observation();
            observation2.MConceptId = concept.Id;
            observation2.ObsDate = DateTime.Today;
            observation2.ValueNumeric = obsval;
            return observation2;
        }
        public static Observation GetTestObservation(string obsval, MConcept concept)
        {
            var observation2 = new Observation();
            observation2.MConceptId = concept.Id;
            observation2.ObsDate = DateTime.Today;
            observation2.ValueText = obsval;
            return observation2;
        }
        public static Observation GetTestObservation(int[] obsvals, MConcept concept)
        {
            var observation2 = new Observation();
            observation2.ObsDate = DateTime.Today;
            observation2.ValueMultipleChoice = String.Join(",", obsvals);
            observation2.MConceptId = concept.Id;
            return observation2;
        }
        public static Observation GetTestObservationsLookupNew()
        {
            var observation2 = new Observation();
            observation2.ObsDate = DateTime.Today;
            observation2.ValueNumeric = -2;
            return observation2;
        }
    }
}

/*

observation.ValueMultipleChoice
observation.ValueMultipleChoice
observation.ValueNumeric
observation.ValueDecimal
observation.ValueLookup
observation.ValueDate
observation.ValueDateTime

Date obsDate;   
MConcept mConcept;
String valueText;
String valueMultipleChoice;
int valueNumeric;
double valueDecimal;
int valueLookup;
Date valueDate;
Date valueDateTime;
Boolean valueBoolean;
Encounter encounter; 
*/
