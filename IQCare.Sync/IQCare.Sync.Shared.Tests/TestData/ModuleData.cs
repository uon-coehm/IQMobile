using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Shared.Tests.TestData
{
    public class ModuleData
    {
        public static List<Module> GetTestModulesOnly()
        {
            var modules = new List<Module>();
            var module = GetModule();

            foreach (var e in GetTestEncounterTypesOnly())
            {
                module.AddEncounterType(e);
            }

            modules.Add(module);

            return modules;
        }
        public static List<EncounterType> GetTestEncounterTypesOnly()
        {
            var encounterTypes = new List<EncounterType>();
            var encounterType = GetEncounterType();

            encounterTypes.Add(encounterType);

            return encounterTypes;
        }

        public static List<Module> GetTestModules()
        {
            var modules = new List<Module>();
            var module = GetModule();

            foreach (var e  in GetTestEncounterTypes())
            {
                module.AddEncounterType(e);
            }
            modules.Add(module);

            return modules;
        }
        public static List<EncounterType> GetTestEncounterTypes()
        {
            var encounterTypes = new List<EncounterType>();
            var encounterType = GetEncounterType();

            foreach (var c in ConceptData.GetTestConcepts())
            {
                    encounterType.AddConcept(c);
            }
            encounterTypes.Add(encounterType);

            return encounterTypes;
        }

        private static Module GetModule()
        {
            var module = new Module();

            module.Name = "TestModule";
            module.Display = "TestModule";
            module.Displayshort = "TM";

            return module;
        }
        private static EncounterType GetEncounterType()
        {
            var encounterType = new EncounterType();

            encounterType.Name = "TM Form";
            encounterType.Display = "TM Form";
            encounterType.Displayshort = "TMFRM";

            return encounterType;
        }



    }
}
