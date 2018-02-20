package com.thepalladiumgroup.iqm.core.tests;

import com.thepalladiumgroup.iqm.core.model.ActiveSession;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.MDataType;
import com.thepalladiumgroup.iqm.core.model.Module;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.User;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/14]
 */
public class TestData {


    public static List<User> getTestUsers() {
        List<User> users = new ArrayList<>();

        users.add(new User("iq-maun-01", "pass1", "7ec06e3a-1abf-4b11-91c2-ba79e87b0402"));
        users.add(new User("iq-maun-02", "pass23", "2f23a58d-e38d-48f2-99ae-a20358cad2b0"));

        return users;
    }

    public static Patient getTestPatientNoUuid() {
        Patient p = new Patient("Test A FName", "Test A LName", "Test A MName", 17, new LocalDate(1988, 3, 23).toDate(), 40001, "1XX", "", "", 1, "", new LocalDate().toDate());

        return p;
    }

    public static Patient getTestPatient() {
        Patient p = new Patient("Test A FName", "Test A LName", "Test A MName", 17, new LocalDate(1988, 3, 23).toDate(), 40001, "1XX", "", "", 1, "", new LocalDate().toDate());
        p.setUuid("2e9f95f0-05c9-473c-933f-fd8ca61daa8c");
        return p;
    }

    public static Patient getTestPatientNewWithEncounter() {
        //Module m=new ModuleService() moduleService.getDefault();
        Patient p = new Patient("Test A FName", "Test A LName", "Test A MName", 17, new LocalDate(1988, 3, 23).toDate(), 40001, "106", "", "", 1, "", new LocalDate().toDate());
        //p.setUuid("2e9f95f0-05c9-473c-933f-fd8ca61daa8c");
        return p;
    }

    public static Patient getTestPatientExisitingWithEncounter() {
        Patient p = new Patient("Test A FName", "Test A LName", "Test A MName", 17, new LocalDate(1988, 3, 23).toDate(), 40001, "106", "", "", 1, "", new LocalDate().toDate());
        //p.setUuid("2e9f95f0-05c9-473c-933f-fd8ca61daa8c");
        return p;
    }
    public static List<Patient> getTestPatients() {
        List<Patient> patients = new ArrayList<>();

        Patient p = new Patient("Ali", "N", "Mtengo", 16, new LocalDate(1978, 3, 23).toDate(), 4001, "101", "", "", 1, "", new Date(2016, 01, 01));
        p.setUuid("60f7fc92-e13e-4969-aba5-0d7346b7203e");
        patients.add(p);
        p = new Patient("Mary", "A", "Namu", 16, new LocalDate(1998, 3, 23).toDate(), 4001, "102", "", "", 1, "", new Date(2016, 03, 01));
        p.setUuid("e0d105a5-d134-441b-9d18-e5c92cb1b42c");
        patients.add(p);

        return patients;
    }

    public static List<Module> getTestModules() {
        List<Module> modules = new ArrayList<>();

        Module moduleA = new Module("TestM1", "Test Module A", "TA");
        moduleA.setUuid("66608106-66fa-413d-ac75-034cd5d924ac");
        moduleA.addEncounterTypes(getEncounterTypesA());
        modules.add(moduleA);

        Module moduleB = new Module("TestM2", "Test Module B", "TB");
        moduleB.setUuid("560a9f00-496b-4106-a05c-db52e8a51a9a");
        moduleB.addEncounterTypes(getEncounterTypesB());
        modules.add(moduleB);

        return modules;
    }

    public static List<EncounterType> getEncounterTypesA() {
        List<EncounterType> encounterTypes = new ArrayList<>();
        encounterTypes.add(new EncounterType("TestE1", "Ward Survey", "WS"));
        encounterTypes.add(new EncounterType("TestE2", "Childran Ward Questioniarre", "CQ"));
        return encounterTypes;
    }

    public static List<Lookup> getLookups() {
        List<Lookup> lookups = new ArrayList<>();
        lookups.add(new Lookup(-1, "Test Lookup SA", -100, "53853cd3-effd-4fc6-b8eb-4164c4c5b2ab"));
        lookups.add(new Lookup(-2, "Test Lookup SB", -200, "73a0815f-9398-468b-8d82-deac1949a6f8"));
        return lookups;
    }
    public static List<EncounterType> getEncounterTypesB() {
        List<EncounterType> encounterTypes = new ArrayList<>();
        encounterTypes.add(new EncounterType("TestE3", "Visits Calender", "VC"));
        encounterTypes.add(new EncounterType("TestE4", "Appointments", "AP"));
        return encounterTypes;
    }

    public static List<MConcept> getConcepts() {
        List<MConcept> concepts = new ArrayList<>();

        MConcept concept = MConcept.CreateConcept("Q1", "Q1", MDataType.YESNO, 1, 1.0);
        concept.setUuid("ee33e09f-3d6b-48c9-8875-106cd16c5b6c");
        concepts.add(concept);

        concept = MConcept.CreateConcept("Q2", "Q2", "Text SingleLine", 2, 2.0);
        concept.setUuid("298beedc-3807-4af2-8820-33ab6900f58d");
        concepts.add(concept);

        concept = MConcept.CreateConceptWithParent("Q2a", "Q2a", "Text SingleLine", 3, 1, 2, "1");
        concept.setUuid("16cecce5-95a2-4d2f-8500-c6f3247a87b9");
        concepts.add(concept);

        concept = MConcept.CreateConceptWithParent("Q2b", "Q2b", "Text SingleLine", 4, 2, 2, "1");
        concept.setUuid("327ff395-5779-49ce-b84b-fc5f9695a8aa");
        concepts.add(concept);

        return concepts;
    }

    public static List<MConcept> getNewConcepts() {
        List<MConcept> concepts = new ArrayList<>();
        concepts.add(MConcept.CreateConcept("S1", "S1", MDataType.YESNO, 11, 1.0));
        concepts.add(MConcept.CreateConcept("S2", "S2", "Text SingleLine", 12, 2.0));
        concepts.add(MConcept.CreateConceptWithParent("S2a", "S2a", "Text SingleLine", 13, 1, 2, "1"));
        concepts.add(MConcept.CreateConceptWithParent("S2b", "S2b", "Text SingleLine", 44, 2, 2, "1"));
        return concepts;
    }

    public static List<ActiveSession> getActiveSessions() {
        List<ActiveSession> activeSessions = new ArrayList<>();
        activeSessions.add(new ActiveSession("TestData", "TK1", "TK1-01"));
        activeSessions.add(new ActiveSession("TestData", "TK2", "TK1-02"));
        return activeSessions;
    }
}
