package com.thepalladiumgroup.iqm.core.tests;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IConceptRepository;
import com.thepalladiumgroup.iqm.core.data.IEncounterRepository;
import com.thepalladiumgroup.iqm.core.data.IObservationRepository;
import com.thepalladiumgroup.iqm.core.data.IPatientRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ConceptRepository;
import com.thepalladiumgroup.iqm.core.data.impl.EncounterRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ObsevationRepository;
import com.thepalladiumgroup.iqm.core.data.impl.PatientRepository;
import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.Module;
import com.thepalladiumgroup.iqm.core.model.Observation;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.services.IModuleService;
import com.thepalladiumgroup.iqm.core.services.impl.ModuleService;

import org.joda.time.LocalDate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/05/23]
 */
public class Factory {

    private final IApplicationManager applicationManager;
    private IModuleService moduleService;
    private IConceptRepository conceptRepository;
    private IPatientRepository patientRepository;
    private IEncounterRepository encounterRepository;
    private IObservationRepository observationRepository;

    public Factory(IApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
        moduleService = new ModuleService(applicationManager);
        conceptRepository = new ConceptRepository(applicationManager);
        patientRepository = new PatientRepository(applicationManager);
        encounterRepository = new EncounterRepository(applicationManager);
        observationRepository = new ObsevationRepository(applicationManager);
    }

    public Patient getPatientNew(String clientcode, String mname) throws SQLException {
        Patient newPatient;
        Patient patient = TestData.getTestPatientNoUuid();
        patient.setFirstname("androidFname");
        patient.setMiddlename(mname);
        patient.setLastname("Kanyari");
        patient.setClientcode(clientcode);
        newPatient = patientRepository.save(patient);
        return newPatient;
    }

    public Patient getPatientNewWithEncounters(String clientcode, String mname, boolean completed) throws SQLException {

        Patient newPatient;
        Encounter newEncounter;
        Observation newOobservation;
        LocalDate localDate = new LocalDate();

        List<Encounter> encounters = new ArrayList<>();

        Module module = moduleService.getDefault();
        EncounterType encounterType = module.getEncounterTypesList().get(0);

        Patient patient = TestData.getTestPatientNoUuid();
        patient.setFirstname("androidFname");
        patient.setMiddlename(mname);
        patient.setClientcode(clientcode);
        newPatient = patientRepository.save(patient);

        Encounter encounter = new Encounter();
        encounter.setEncounterType(encounterType);
        encounter.setStarttime(localDate.toDate());
        encounter.setStartdate(localDate.toDate());
        encounter.setGeoloctaion("KKK");
        encounter.setPatient(newPatient);
        encounter.setCompleted(completed);
        newEncounter = encounterRepository.save(encounter);

        /*

02f54b8e-323c-416a-86d4-30656df1a17a

5dc4167e-2a8a-46d2-8062-90efee6697a3

         */

        Observation observation = new Observation();
        MConcept conceptStaffTestedBefore = conceptRepository.find(3);
        observation.setObsDate(localDate.toDate());
        observation.setmConcept(conceptStaffTestedBefore);
        observation.setValueBoolean(true);
        observation.setEncounter(newEncounter);

        observationRepository.save(observation);

        observation = new Observation();
        MConcept conceptWhen = conceptRepository.findbySyncId("5dc4167e-2a8a-46d2-8062-90efee6697a3");
        observation.setObsDate(localDate.toDate());
        observation.setmConcept(conceptWhen);
        observation.setValueDecimal(7);
        observation.setEncounter(newEncounter);

        observationRepository.save(observation);

        /*

49	Select Test (Screening)	90000
        Unigold		    90003
        Determine       90001
        First Response	90002

50	Select Test (Confirmatory)	90003
        Determine	    30001
        First Response	30002
        Unigold	        30003


32	Test No 1 Result	    90001
        Positive -10002
        Negative -10001

52	Test No 2 Result	    90001
        Positive -10002
        Negative -10001

55	PCR Done	850
        Yes	-7120
        No	-7121
        N/A -7122
53	PCR Result	            851
        Negative -7123
        Positive -7124
        Pending  -7125

37	Final HIV result today	244
        Positive        -4151
        Negative	    -4150
        Indeterminate   -4152

         */
        MConcept conceptTest1Type = conceptRepository.find(49);
        MConcept conceptTest1Result = conceptRepository.find(32);
        MConcept conceptTest2Type = conceptRepository.find(50);
        MConcept conceptTest2Result = conceptRepository.find(52);
        MConcept conceptPCRDone = conceptRepository.find(55);
        MConcept conceptPCRResult = conceptRepository.find(53);
        MConcept conceptFinalResult = conceptRepository.find(37);
        /*

49	Select Test (Screening)	90000
        Unigold		    90003
        Determine       90001
        First Response	90002
         */
        observation = new Observation();
        observation.setObsDate(localDate.toDate());
        observation.setmConcept(conceptTest1Type);
        observation.setValueNumeric(90001);
        observation.setEncounter(newEncounter);
        observationRepository.save(observation);
/*

32	Test No 1 Result	    90001
        Positive -10002
        Negative -10001
*/
        observation = new Observation();
        observation.setObsDate(localDate.toDate());
        observation.setmConcept(conceptTest1Result);
        observation.setValueNumeric(10002);
        observation.setEncounter(newEncounter);
        observationRepository.save(observation);
/*


50	Select Test (Confirmatory)	90003
        Determine	    30001
        First Response	30002
        Unigold	        30003

         */
        observation = new Observation();
        observation.setObsDate(localDate.toDate());
        observation.setmConcept(conceptTest2Type);
        observation.setValueNumeric(30002);
        observation.setEncounter(newEncounter);
        observationRepository.save(observation);


        /*

52	Test No 2 Result	    90001
        Positive -10002
        Negative -10001
         */
        observation = new Observation();
        observation.setObsDate(localDate.toDate());
        observation.setmConcept(conceptTest2Result);
        observation.setValueNumeric(10001);
        observation.setEncounter(newEncounter);
        observationRepository.save(observation);



        /*

55	PCR Done	850
        Yes	-7120
        No	-7121
        N/A -7122
         */
        observation = new Observation();
        observation.setObsDate(localDate.toDate());
        observation.setmConcept(conceptPCRDone);
        observation.setValueNumeric(7120);
        observation.setEncounter(newEncounter);
        observationRepository.save(observation);
        /*

53	PCR Result	            851
        Negative -7123
        Positive -7124
        Pending  -7125

         */
        observation = new Observation();
        observation.setObsDate(localDate.toDate());
        observation.setmConcept(conceptPCRResult);
        observation.setValueNumeric(7124);
        observation.setEncounter(newEncounter);
        observationRepository.save(observation);



        /*


37	Final HIV result today	244
        Positive        -4151
        Negative	    -4150
        Indeterminate   -4152

         */
        observation = new Observation();
        observation.setObsDate(localDate.toDate());
        observation.setmConcept(conceptFinalResult);
        observation.setValueNumeric(4151);
        observation.setEncounter(newEncounter);
        observationRepository.save(observation);

        newPatient = patientRepository.refresh(newPatient);
        return newPatient;
    }

    public Patient getPatientNewWithEncounters(String clientcode, String mname) throws SQLException {
        return getPatientNewWithEncounters(clientcode, mname, true);
    }
}

/*
    private Date obsDate;
    private MConcept mConcept;
    private String valueText;
    private String valueMultipleChoice;
    private int valueNumeric;
    private double valueDecimal;
    private int valueLookup;
    private Date valueDate;
    private Date valueDateTime;
    private Boolean valueBoolean;
    private Encounter encounter;
*/