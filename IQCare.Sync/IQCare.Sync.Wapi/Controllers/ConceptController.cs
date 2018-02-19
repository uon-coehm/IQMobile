using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Reflection;
using System.Web.Http;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Model;
using log4net;

namespace IQCare.Sync.Wapi.Controllers
{
    public class ConceptController : ApiController
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly IConceptService _conceptService;

        public ConceptController(IConceptService conceptService)
        {
            _conceptService = conceptService;
        }

        // GET: api/Concept/5
        public IEnumerable<MConcept> Get(int id)
        {
            Log.Debug("get called!");
            var concepts = _conceptService.GetAllByEncounterType(id)
                .OrderBy(x => x.Rank);
                

            return concepts.ToList();
        }
    }
}
