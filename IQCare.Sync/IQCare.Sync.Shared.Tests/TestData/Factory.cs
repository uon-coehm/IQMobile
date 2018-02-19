using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Interfaces.Repository;
using IQCare.Sync.Core.Interfaces.UoW;
using IQCare.Sync.Core.Model;
using IQCare.Sync.Infrastructure;
using IQCare.Sync.Infrastructure.Repository;

namespace IQCare.Sync.Shared.Tests.TestData
{
    public class Factory
    {
        private IUnitOfWork _uow;

        private List<User> _users;
        private List<Lookup> _lookups;
        private List<Patient> _patients;
        private List<Patient> _patients2;
        private List<Patient> _patients3;
        private List<Module> _modules;
        private List<Module> _modulesNoConcepts;
        private List<Encounter> _encounters;
        private List<Encounter> _encounters2;
        private List<MDataTypeMap> _dataTypeMaps;
        private List<MConcept> _concepts;
        private IEmrRepository _emrRepository;

        public IUnitOfWork UoW
        {
            get { return _uow; }
            set { _uow = value; }
        }

        public Factory()
        {
            _uow = new UnitOfWork(new SyncContext());
            _emrRepository=new EmrRepository();
        }
       
        public List<User> GenerateTestUsers()
        {
            _users = new List<User>();
            foreach (var user in UserData.GetTestUsers())
            {
                _uow.UserRepository.Save(user);
                _uow.Commit();
                _users.Add(user);
            }
            return _users;
        }

        public void CleanUpTestUsers()
        {
            foreach (var user in _users)
            {
                _uow.UserRepository.Delete(user.UuId);
            }
            _uow.Commit();
        }

        public List<Lookup> GenerateTestLookups()
        {
            _lookups = new List<Lookup>();
            foreach (var lookup in LookupData.GetTestLookups())
            {
                _uow.LookupRepository.Save(lookup);
                _uow.Commit();
                _lookups.Add(lookup);
            }
            return _lookups;
        }

        public void CleanUpTestLookups()
        {
            foreach (var lookup in _lookups)
            {
                _uow.LookupRepository.Delete(lookup.UuId);
            }
            _uow.Commit();
        }

        public List<Patient> GenerateTestPatients()
        {
            _patients = new List<Patient>();
            foreach (var patient in PatientData.GetTestPatients())
            {
                _uow.PatientRepository.Save(patient);
                _uow.Commit();
                _patients.Add(patient);
            }
            return _patients;
        }

        public Patient GenerateTestHtcPatient()
        {
            var patient = PatientData.GetPatient();
            _uow.PatientRepository.Save(patient);
            _uow.Commit();
            return patient;
        }

        public List<Patient> GenerateTestPatientsWithEncounters()
        {
            _patients2 = new List<Patient>();

            var patient = PatientData.GetPatientForEncounter();
            patient.AddEncounters(GenerateTestEncountersNoPatient());

            _uow.PatientRepository.Save(patient);
            _uow.Commit();

            _patients2.Add(patient);

            return _patients2;
        }

        public Patient GenerateTestPatientNewWithEncounter()
        {
            var patient = PatientData.GetPatientForEncounterNew();
            patient.AddEncounters(GenerateTestEncountersNoPatient());
            _patients2.Add(patient);
            return patient;
        }


        public void CleanUpTestPatients()
        {
            foreach (var patient in _patients)
            {
                _uow.PatientRepository.Delete(patient.UuId);
                _uow.Commit();
            }
            
        }
        public void CleanUpTestHTCPatients()
        {
            foreach (var patient in _patients3)
            {
                _uow.PatientRepository.Delete(patient.UuId);
                _uow.Commit();
            }

        }
        public void CleanUpTestPatientsWithEncounters()
        {
            foreach (var patient in _patients2)
            {
                _uow.PatientRepository.Delete(patient.UuId);
                _uow.Commit();
            }
            
            CleanUpTestModules();
        }

        public List<Module> GenerateTestModulesNoConcepts()
        {
            _modulesNoConcepts = new List<Module>();
            foreach (var module in ModuleData.GetTestModulesOnly())
            {
                _uow.ModuleRepository.Save(module);
                _uow.Commit();
                _modulesNoConcepts.Add(module);
            }
            return _modulesNoConcepts;
        }

        public void CleanUpTestModulesNoConcepts()
        {
            foreach (var module in _modulesNoConcepts)
            {
                _uow.ModuleRepository.Delete(module.UuId);
            }
            _uow.Commit();
        }

        public List<Module> GenerateTestModules()
        {
            _modules = new List<Module>();
            foreach (var module in ModuleData.GetTestModules())
            {
                _uow.ModuleRepository.Save(module);
                _uow.Commit();
                _modules.Add(module);
            }
            return _modules;
        }

        public void CleanUpTestModules()
        {
            foreach (var module in _modules)
            {
                _uow.ModuleRepository.Delete(module.UuId);
                _uow.Commit();
            }
            
        }

        public List<MConcept> GenerateTestConcepts()
        {
            GenerateTestModulesNoConcepts();
            var et = _modulesNoConcepts.First().EncounterTypes.First();
            _concepts = new List<MConcept>();
            foreach (var concept in ConceptData.GetTestConcepts())
            {
                    concept.EncounterType = et;
                _uow.MConceptRepository.Save(concept);
                _uow.Commit();
                _concepts.Add(concept);
            }
            return _concepts;
        }

        public List<Encounter> GenerateTestEncounters()
        {
            GenerateTestPatients();
            GenerateTestModules();
            
            _encounters = new List<Encounter>();

            //EncounterType
            var encounterType = _modules.First().EncounterTypes.First();

            //Concepts
            var conceptNumeric = encounterType.Concepts.FirstOrDefault(x => x.DataTypeMapId==3);
            var conceptLookup = encounterType.Concepts.FirstOrDefault(x => x.DataTypeMapId==5);

            //Observations
            var observationNumeric = EncounterData.GetTestObservationsNumeric();
            observationNumeric.MConcept = conceptNumeric;
            var observationLookup = EncounterData.GetTestObservationsLookup();
            observationLookup.MConcept = conceptLookup;

            //Encounter
            var encounter = EncounterData.GetTestEncounter();
            encounter.EncounterType = encounterType;
            encounter.EncounterTypeId = encounterType.Id;
            encounter.Patient = _patients.First();
            encounter.AddObservation(observationNumeric);
            encounter.AddObservation(observationLookup);

            _uow.EncounterRepository.Save(encounter);
            _uow.Commit();

            _encounters.Add(encounter);

            return _encounters;
        }

        public List<Encounter> GenerateTestEncountersByType(int encounterTypeId)
        {
            _patients3=new List<Patient>();
            var patient = GenerateTestHtcPatient();
            _patients3.Add(patient);
            

            _encounters2 = new List<Encounter>();

            //EncounterType
            var encounterType = _uow.EncounterTypeRepository.FindById(encounterTypeId);

            //Concepts
            /*           
            
            6. Seen As?(5)
                Individual (4030)
                Couple (4031)
                Group  (4032)
            30.If not tested,Why not (5)
                Changed mind (4124)
                Want to test later	(4125)            
            24.Homosexual Partners last 12m (3)
            11.Marps (2)
                Prisoner (4051)
                Fisherperson(4048)
            15.Disability (2)	
                N/A (No Disability) (4063)
                Blind (4065)
                Other	(4068)
            16.Specify other Disability (6)            
            48.Remarks (7)

            */

            var conceptLookup = encounterType.Concepts.FirstOrDefault(x => x.Id == 30);
            var conceptLookup2 = encounterType.Concepts.FirstOrDefault(x => x.Id == 6);

            var conceptNumeric = encounterType.Concepts.FirstOrDefault(x => x.Id == 24);

            var conceptMulti = encounterType.Concepts.FirstOrDefault(x => x.Id == 15);
            var conceptMulti2 = encounterType.Concepts.FirstOrDefault(x => x.Id == 11);

            var conceptText = encounterType.Concepts.FirstOrDefault(x => x.Id == 16);
            var conceptText2 = encounterType.Concepts.FirstOrDefault(x => x.Id == 48);

            //Observations
            var observations = new List<Observation>();
            observations.Add(EncounterData.GetTestObservation(4124, conceptLookup));
            observations.Add(EncounterData.GetTestObservation(4030, conceptLookup2));

            observations.Add(EncounterData.GetTestObservation(4, conceptNumeric));

            observations.Add(EncounterData.GetTestObservation(new[] {4063, 4065, 4068}, conceptMulti));
            observations.Add(EncounterData.GetTestObservation(new[] {4051, 4048}, conceptMulti2));

            observations.Add(EncounterData.GetTestObservation("color blind", conceptText));
            observations.Add(EncounterData.GetTestObservation("No remark", conceptText2));

            //Encounter
            var encounter = EncounterData.GetTestEncounter();
            encounter.EncounterType = encounterType;
            encounter.EncounterTypeId = encounterType.Id;
            encounter.Patient = patient;
            encounter.AddObservations(observations);            

            _uow.EncounterRepository.Save(encounter);
            _uow.Commit();

            _encounters2.Add(encounter);

            return _encounters2;
        }

        public Encounter GenerateTestEncounter(Patient patient,Module module)
        {
            //EncounterType
            var encounterType = module.EncounterTypes.First();

            //Concepts
            var conceptNumeric = encounterType.Concepts.FirstOrDefault(x => x.DataTypeMapId == 3);
            var conceptLookup = encounterType.Concepts.FirstOrDefault(x => x.DataTypeMapId == 5);

            //Observations
            var observationNumeric = EncounterData.GetTestObservationsNumericNew();
            observationNumeric.MConceptId = conceptNumeric.Id;
            var observationLookup = EncounterData.GetTestObservationsLookupNew();
            observationLookup.MConceptId = conceptLookup.Id;

            //Encounter
            var encounter = EncounterData.GetTestEncounterNew();
            encounter.EncounterType = encounterType;
            encounter.Patient = patient;
            encounter.AddObservation(observationNumeric);
            encounter.AddObservation(observationLookup);

            return encounter;
        }

        public List<Encounter> GenerateTestEncountersNoPatient()
        {
            GenerateTestModules();

            _encounters = new List<Encounter>();

            //EncounterType
            var encounterType = _modules.First().EncounterTypes.First();

            //Concepts
            var conceptNumeric = encounterType.Concepts.FirstOrDefault(x => x.DataTypeMapId == 3);
            var conceptLookup = encounterType.Concepts.FirstOrDefault(x => x.DataTypeMapId == 5);

            //Observations
            var observationNumeric = EncounterData.GetTestObservationsNumeric();
            observationNumeric.MConceptId = conceptNumeric.Id;
            var observationLookup = EncounterData.GetTestObservationsLookup();
            observationLookup.MConceptId = conceptLookup.Id;

            //Encounter
            var encounter = EncounterData.GetTestEncounter();
            encounter.EncounterTypeId = encounterType.Id;           
            encounter.AddObservation(observationNumeric);
            encounter.AddObservation(observationLookup);

            _encounters.Add(encounter);

            return _encounters;
        }
        public void CleanUpTestEncounters()
        {
            foreach (var encounter in _encounters)
            {
                _uow.EncounterRepository.Delete(encounter.UuId);
                _uow.Commit();
            }
            
            CleanUpTestPatients();
            CleanUpTestModules();
        }

        public void CleanUpTestEncountersByType()
        {
            foreach (var encounter in _encounters2)
            {
                _uow.EncounterRepository.Delete(encounter.UuId);
                _uow.Commit();
            }

            CleanUpTestHTCPatients();
        }

        public List<string> GetIQTables()
        {
            var tables = new List<string>
            {
                "[dtl_PatientContacts]",
                "[DTL_PATIENTHOUSEHOLDINFO]",
                "[DTL_RURALRESIDENCE]",
                "[DTL_URBANRESIDENCE]",
                "[DTL_PATIENTHIVPREVCAREENROLLMENT]",
                "[DTL_PATIENTGUARANTOR]",
                "[DTL_PATIENTDEPOSITS]",
                "[DTL_PATIENTINTERVIEWER]"
            };
            return tables;
        }

      
        public void CleanIQCareTestPatients(int testIqPatientId)
        {
            string sql =
                $@"

                    delete from iqcare.[dbo].dtl_fb_DisabilityHTC   where Ptn_pk= {testIqPatientId};
                    delete from iqcare.[dbo].dtl_fb_HTCReferredTO  where Ptn_pk= {testIqPatientId};
                    delete from iqcare.[dbo].dtl_fb_InformationSourceHTC   where Ptn_pk= {testIqPatientId};
                    delete from iqcare.[dbo].dtl_fb_MARPsHTC   where Ptn_pk= {testIqPatientId};
                    delete from iqcare.[dbo].dtl_fb_ServicesGivenHTC  where Ptn_pk= {testIqPatientId};
                    delete from iqcare.[dbo].dtl_fb_VisitReasonHTC where Ptn_pk= {testIqPatientId};
                    delete from iqcare.[dbo].[DTL_FBCUSTOMFIELD_KNH_HTC_Form]  where Ptn_pk= {testIqPatientId};

                    delete from  dtl_PatientContacts where Ptn_Pk= {testIqPatientId};
                    delete from  [DTL_PATIENTHOUSEHOLDINFO] where Ptn_Pk= {testIqPatientId};	
                    delete from  [DTL_RURALRESIDENCE] where Ptn_Pk= {testIqPatientId};	
                    delete from  [DTL_URBANRESIDENCE]  where Ptn_Pk= {testIqPatientId};	
                    delete from  [DTL_PATIENTHIVPREVCAREENROLLMENT]  where Ptn_Pk= {testIqPatientId};			
                    delete from  [DTL_PATIENTGUARANTOR]  where Ptn_Pk= {testIqPatientId};	
                    delete from  [DTL_PATIENTDEPOSITS]  where Ptn_Pk= {testIqPatientId};			
                    delete from  [DTL_PATIENTINTERVIEWER]  where Ptn_Pk= {testIqPatientId};	
                    delete from  [DTL_FBCUSTOMFIELD_Patient_Registration] 	 where Ptn_Pk= {testIqPatientId};	
                    delete from  ord_Visit where Ptn_Pk= {testIqPatientId};
                    delete from mst_Patient where Ptn_Pk= {testIqPatientId};

                ";
            if (testIqPatientId > -1)
            {
                _emrRepository.ExecuteCommand(sql);
            }
        }

        public void CleanIQCareTestPatients()
        {
            string sql =
                $@"

            delete from iqcare.[dbo].dtl_fb_DisabilityHTC   where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
            delete from iqcare.[dbo].dtl_fb_HTCReferredTO  where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
            delete from iqcare.[dbo].dtl_fb_InformationSourceHTC   where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
            delete from iqcare.[dbo].dtl_fb_MARPsHTC   where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
            delete from iqcare.[dbo].dtl_fb_ServicesGivenHTC  where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
            delete from iqcare.[dbo].dtl_fb_VisitReasonHTC where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
            delete from iqcare.[dbo].[DTL_FBCUSTOMFIELD_KNH_HTC_Form]  where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
            delete from  iqcare.dbo.dtl_PatientContacts where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
            delete from  iqcare.dbo.[DTL_PATIENTHOUSEHOLDINFO] where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
            delete from  iqcare.dbo.[DTL_RURALRESIDENCE] where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
            delete from  iqcare.dbo.[DTL_URBANRESIDENCE]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
            delete from  iqcare.dbo.[DTL_PATIENTHIVPREVCAREENROLLMENT]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));			
            delete from  iqcare.dbo.[DTL_PATIENTGUARANTOR]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
            delete from  iqcare.dbo.[DTL_PATIENTDEPOSITS]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));			
            delete from  iqcare.dbo.[DTL_PATIENTINTERVIEWER]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
            delete from  iqcare.dbo.[DTL_FBCUSTOMFIELD_Patient_Registration] 	 where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
            delete from  iqcare.dbo.ord_Visit where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
            delete from  iqcare.dbo.mst_Patient where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
            delete from  iqcare.dbo.lnk_patientprogramstart where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));

                ";

            _emrRepository.ExecuteCommand(sql);
        }




    }
}
