package com.thepalladiumgroup.iqm.core.data.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IEncounterRepository;
import com.thepalladiumgroup.iqm.core.data.impl.EncounterRepository;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.RecordStats;
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
 * Created by Koske Kimutai [2016/04/25]
 */
public class EncounterTypeRepositoryTests extends BaseCoreTest {

    private IEncounterRepository repository;

    private List<Integer> testpatientIds;
    private Factory factory;
    private Patient testPatientEncounters1, testPatientEncounters2;


    @Before
    public void setUp() throws SQLException {

        repository = new EncounterRepository(applicationManager);

        testpatientIds = new ArrayList<>();
        factory = new Factory(applicationManager);
        testPatientEncounters1 = factory.getPatientNewWithEncounters("TEST0002", "Shiko");
        testpatientIds.add(testPatientEncounters1.getId());
        testPatientEncounters2 = factory.getPatientNewWithEncounters("TEST00032", "Njeri", false);
        testpatientIds.add(testPatientEncounters2.getId());

    }


    @Test
    public void should_get_recordStats() throws SQLException {
        RecordStats recordStats = repository.getRecordStats();
        Assert.assertTrue(recordStats.getTotalCount() > 0);
        Assert.assertTrue(recordStats.getCompleteCount() > 0);
        Assert.assertTrue(recordStats.getPendingCount() > 0);
        System.out.println(recordStats);
    }

    @After
    public void tearDown() throws SQLException {
        for (int patientid : testpatientIds) {
            repository.delete(patientid);
        }
    }
}
