using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Model;
using IQCare.Sync.Infrastructure;
using IQCare.Sync.Infrastructure.Repository;

namespace IQCare.Sync.Shared.Tests.TestData
{

    public class ConceptData
    {
        public ConceptData()
        {

        }

        public static List<MConcept> GetTestConcepts()
        {
            var concepts = new List<MConcept>();

            concepts.Add(GetTestConceptsNumeric());           
            concepts.Add(GetTestConceptsLookup());

            return concepts;
        }
        public static MConcept GetTestConceptsNumeric()
        {
            //var types = MDataTypeMapData.GetMDataTypeMaps();

            var concept1 = new MConcept();
            concept1.Display = "How many years";
            concept1.Description = "How many years";
            concept1.Rank = 1;
            concept1.DataTypeMapId = 3;
            return concept1;
        }
        public static MConcept GetTestConceptsLookup()
        {
            //var types = MDataTypeMapData.GetMDataTypeMaps();

            var concept2 = new MConcept();
            concept2.Display = "Which country";
            concept2.Description = "Which country";
            concept2.Rank = 2;
            concept2.DataTypeMapId = 5; 
            concept2.Lookupcodeid = -1;
           return concept2;
        }

    }
}
