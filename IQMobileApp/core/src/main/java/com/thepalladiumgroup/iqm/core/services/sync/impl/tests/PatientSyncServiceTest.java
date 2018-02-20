package com.thepalladiumgroup.iqm.core.services.sync.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IPatientRepository;
import com.thepalladiumgroup.iqm.core.data.impl.PatientRepository;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.services.IPatientService;
import com.thepalladiumgroup.iqm.core.services.impl.PatientService;
import com.thepalladiumgroup.iqm.core.services.sync.IPatientSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.impl.PatientSyncService;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;
import com.thepalladiumgroup.iqm.core.tests.Factory;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public class PatientSyncServiceTest extends BaseCoreTest {

    String uuid = "f60e6d32-f47d-42fa-a4e5-f4957e77aea5";
    private IPatientService patientService;
    private IPatientSyncService patientSyncService;
    private IPatientRepository patientRepository;
    private List<Integer> testPIds = new ArrayList<>();
    private Patient testPatient, testPatient2, testPatientEncounters1, testPatientEncounters2;
    private Factory factory;


    @Before
    public void setUp() throws SQLException {
        factory = new Factory(applicationManager);
        patientService = new PatientService(applicationManager);
        patientSyncService = new PatientSyncService(applicationManager);
        patientRepository = new PatientRepository(applicationManager);
//        testPatient = factory.getPatientNew("TEST0012", "Sally");
//        testPIds.add(testPatient.getId());
//        testPatient2 = factory.getPatientNew("TEST0022", "Mwangi");
//        testPIds.add(testPatient2.getId());
//        testPatientEncounters1 = factory.getPatientNewWithEncounters("TEST0002", "Shiko");
//        testPIds.add(testPatientEncounters1.getId());
//        testPatientEncounters2 = factory.getPatientNewWithEncounters("TEST0003", "Ouko");
//        testPIds.add(testPatientEncounters2.getId());
    }

    @Test
    public void should_initialize() throws Exception {
        patientSyncService.initialze();
        Assert.assertNotNull(patientSyncService.getServer());
        Assert.assertNotNull(patientSyncService.getAdapter());
        System.out.println(patientSyncService.getServer());
    }

    @Test
    public void should_read_all_patients() throws Exception {
        List<Patient> patients = patientSyncService.readAllPatients();
        Assert.assertNotNull(patients);
        for (Patient u : patients) {
            System.out.println(u);
        }
    }

    @Test
    public void should_send_patient_new() throws Exception {
        Assert.assertNotNull(testPatient);
        patientSyncService.sendPatient(testPatient);
        List<Patient> patients = patientSyncService.readAllPatients();
        Assert.assertNotNull(patients);
        Patient found = null;
        for (Patient p : patients) {
            if (p.getUuid().equalsIgnoreCase(testPatient.getUuid())) {
                found = p;
            }
        }
        Assert.assertNotNull(found);
        Assert.assertEquals("TEST0012", found.getClientcode());
        System.out.println(found);
    }

    @Test
    public void should_send_patient_new_with_encounters() throws Exception {
        Assert.assertNotNull(testPatientEncounters1);
        patientSyncService.sendPatient(testPatientEncounters1);
        List<Patient> patients = patientSyncService.readAllPatients();
        Assert.assertNotNull(patients);
        Patient found = null;
        for (Patient p : patients) {
            if (p.getUuid().equalsIgnoreCase(testPatientEncounters1.getUuid())) {
                found = p;
            }
        }
        Assert.assertNotNull(found);
        System.out.println(found);
    }

    @Test
    public void should_send_couples() throws Exception {


        List<Patient> patientsToSend = new ArrayList<>();
        /*
        Patient patientA = patientRepository.find(193);
        Assert.assertNotNull(patientA);
        Assert.assertNotNull(patientA.getEncountersList());
        */
        Patient patientB = patientRepository.find(202);
        Assert.assertNotNull(patientB);
        Assert.assertNotNull(patientB.getEncountersList());
        patientsToSend.add(patientB);


        Patient patientA = patientRepository.find(203);
        Assert.assertNotNull(patientA);
        Assert.assertNotNull(patientA.getEncountersList());

        patientsToSend.add(patientA);

        for (Patient p : patientsToSend) {
            Patient pts = patientService.getToSend(p.getId());
            patientSyncService.sendPatient(pts);
        }


        List<Patient> patients = patientSyncService.readAllPatients();
        Assert.assertNotNull(patients);
        Patient foundA = findPatient(patientA, patients);
        Assert.assertNotNull(foundA);
        System.out.println(foundA);
        Patient foundB = findPatient(patientB, patients);
        Assert.assertNotNull(foundB);
        System.out.println(foundB);
    }

    @Test
    public void should_send_patient_from_database() throws Exception {
        Patient kimani = patientRepository.find(193);
        Assert.assertNotNull(kimani);
        Assert.assertNotNull(kimani.getEncountersList());

        patientSyncService.sendPatient(kimani);

        List<Patient> patients = patientSyncService.readAllPatients();
        Assert.assertNotNull(patients);
        Patient found = null;
        for (Patient p : patients) {
            if (p.getUuid().equalsIgnoreCase(kimani.getUuid())) {
                found = p;
            }
        }
        Assert.assertNotNull(found);
        System.out.println(found);
    }

    //    @Test
    public void should_send_patient_exisiting() throws Exception {
        Assert.assertNotNull(testPatient);
        patientSyncService.sendPatient(testPatient);
        List<Patient> patients = patientSyncService.readAllPatients();
        Assert.assertNotNull(patients);
        Patient found = null;
        for (Patient p : patients) {
            if (p.getUuid().equalsIgnoreCase(testPatient.getUuid())) {
                found = p;
            }
        }
        Assert.assertNotNull(found);
        System.out.println("Exising:" + found);
        System.out.println("------------------------------------------------ ");


        testPatient.setMiddlename("Kanyari");
        Patient updatedPatient = patientRepository.update(testPatient);

        patients = new ArrayList<>();
        Assert.assertNotNull(updatedPatient);
        patientSyncService.sendPatient(updatedPatient);
        patients = patientSyncService.readAllPatients();
        Assert.assertNotNull(patients);
        found = null;
        for (Patient p : patients) {
            if (p.getUuid().equalsIgnoreCase(updatedPatient.getUuid())) {
                found = p;
            }
        }
        Assert.assertNotNull(found);
        Assert.assertEquals("Kanyari", found.getMiddlename());
        System.out.println("Changed to:" + found);
    }

    @After
    public void tearDown() throws SQLException {


//        for (int id : testPIds) {
//            try {
//                patientRepository.delete(id);
//            } catch (SQLException ex) {
//
//            }
//
//        }
    }

    private Patient findPatient(Patient search, List<Patient> patients) {
        Patient found = null;
        for (Patient p : patients) {
            if (p.getUuid().equalsIgnoreCase(search.getUuid())) {
                found = p;
                break;
            }
        }
        return found;
    }
}