using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Reflection;
using System.Threading;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Model;
using log4net;

namespace IQCare.Sync.Wapi.Controllers
{
    public class PatientController : ApiController
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly IPatientService _patientService;
        private readonly string _iqmStore = $"{HttpRuntime.AppDomainAppPath}{@"\"}{Properties.Settings.Default.StoreLocation}{@"\"}";
        


        public PatientController(IPatientService patientService)
        {
            _patientService = patientService;
        }

        // GET: api/Patient
        public IEnumerable<Patient> Get()
        {
            Log.Debug("get called!");
            var patients = _patientService.GetAll();
            return patients.ToList();
        }

        // POST: api/Patient
        public void Post(PateintDTO patientDto)
        {
            Log.Debug($"posting... {patientDto}");
            var patient = patientDto.GeneratePatient(_iqmStore);
            _patientService.Sync(patient, false);//.Wait();
            //_patientService.SyncAsync(patient, false, new CancellationTokenSource());//.Wait();
            Log.Debug($"patient {patientDto} posted !");
        }
    }
}
