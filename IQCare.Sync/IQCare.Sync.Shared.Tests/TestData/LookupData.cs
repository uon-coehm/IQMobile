using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Shared.Tests.TestData
{

    public class LookupData
    {

        public static List<Lookup> GetTestLookups()
        {
            var lookups = new List<Lookup>();

            var lookupKe = new Lookup();
            lookupKe.Name = "Kenya";
            lookupKe.Codeid = -1;
            lookupKe.Rank = 1;
            lookupKe.IqcareId = -1;
            lookups.Add(lookupKe);

            var lookupTz = new Lookup();
            lookupTz.Name = "Tanzania";
            lookupTz.Codeid = -1;
            lookupTz.Rank = 2;
            lookupKe.IqcareId = -2;
            lookups.Add(lookupTz);


            return lookups;
        }
        
    }
}
