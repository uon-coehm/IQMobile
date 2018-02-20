package com.thepalladiumgroup.iqm.core.data.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IEncounterRepository;
import com.thepalladiumgroup.iqm.core.data.IObservationRepository;
import com.thepalladiumgroup.iqm.core.data.IPatientRepository;
import com.thepalladiumgroup.iqm.core.data.impl.EncounterRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ObsevationRepository;
import com.thepalladiumgroup.iqm.core.data.impl.PatientRepository;
import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.Observation;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.PatientStats;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;
import com.thepalladiumgroup.iqm.core.tests.Factory;
import com.thepalladiumgroup.iqm.core.tests.TestData;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/16]
 */
public class PatientRepositoryTest extends BaseCoreTest {

    private IPatientRepository repository;
    private IEncounterRepository encounterRepository;
    private IObservationRepository observationRepository;

    private List<Integer> testpatientIds;
    private Factory factory;
    private Patient testPatientEncounters1;


    @Before
    public void setUp() throws SQLException {

        repository = new PatientRepository(applicationManager);
        encounterRepository = new EncounterRepository(applicationManager);
        observationRepository = new ObsevationRepository(applicationManager);

        testpatientIds = new ArrayList<>();
        factory = new Factory(applicationManager);
        testPatientEncounters1 = factory.getPatientNewWithEncounters("TEST0002", "Shiko");
        testpatientIds.add(testPatientEncounters1.getId());




        for (Patient patient : TestData.getTestPatients()) {
            Patient newpatient = repository.save(patient);
            testpatientIds.add(newpatient.getId());
        }
    }

    @Test
    public void should_find_byusername() throws SQLException {
        Patient patient = repository.findByCode("102");
        Assert.assertEquals("Mary", patient.getFirstname());
        System.out.println("Patient:" + patient.toString());
    }

    @Test
    public void should_save_with_encounters() throws SQLException {
        Patient patient = new Factory(applicationManager).getPatientNewWithEncounters("TEST0005", "Lolo");
        testpatientIds.add(patient.getId());
        Assert.assertNotNull(patient);
        Assert.assertNotNull(patient.getEncounters());
        Assert.assertNotNull(patient.getEncountersList().get(0).getObservations());
        System.out.println("Patient:" + patient.toString());
        for (Encounter e:patient.getEncounters()) {
            System.out.println(" >." + e.toString());
            for (Observation o:e.getObservations()) {
                System.out.println(" >>." + o.toString());
            }
        }
    }

    @Test
    public void should_delete_and_remove_encounters() throws SQLException {
        Patient patient = repository.find(testPatientEncounters1.getId());
        Assert.assertNotNull(patient);
        Encounter e = patient.getEncountersList().get(0);
        Assert.assertNotNull(e);
        Assert.assertNotNull(e.getObservationsList());
        repository.delete(patient);

        Patient patientdeleted = repository.find(testPatientEncounters1.getId());
        Assert.assertNull(patientdeleted);
        List<Encounter> encounters = encounterRepository.getByPatient(testPatientEncounters1.getId());
        Assert.assertNull(encounters);

        List<Observation> observations = observationRepository.getByEncounter(e.getId());
        Assert.assertNull(observations);


    }

    @Test
    public void should_get_alldemographics() throws SQLException {
        List<Patient> patients = repository.getAllDemographics();
        Assert.assertTrue(patients.size() > 0);

        for (Patient p : patients
                ) {
            System.out.println(p);
        }

    }

    @Test
    public void should_get_pateintStats() throws SQLException {
        PatientStats patientStats = repository.getStats();
        Assert.assertTrue(patientStats.getFemaleCount() > 0);
        Assert.assertTrue(patientStats.getMaleCount() > 0);
        Assert.assertTrue(patientStats.getTotalCount() > 0);
        System.out.println(patientStats);
    }

    @After
    public void tearDown() throws SQLException {
        for (int patientid : testpatientIds) {
            repository.delete(patientid);
        }
    }
}
