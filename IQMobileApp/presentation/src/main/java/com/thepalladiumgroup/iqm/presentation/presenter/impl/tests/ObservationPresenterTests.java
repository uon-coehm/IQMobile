package com.thepalladiumgroup.iqm.presentation.presenter.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IPatientRepository;
import com.thepalladiumgroup.iqm.core.data.impl.PatientRepository;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.services.sync.IPatientSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.impl.PatientSyncService;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;
import com.thepalladiumgroup.iqm.core.tests.Factory;
import com.thepalladiumgroup.iqm.presentation.presenter.IObservationPresenter;
import com.thepalladiumgroup.iqm.presentation.presenter.impl.ObservationPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IObservationView;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/06/14]
 */
public class ObservationPresenterTests extends BaseCoreTest {
    String uuid = "f60e6d32-f47d-42fa-a4e5-f4957e77aea5";
    private IPatientSyncService patientSyncService;
    private IPatientRepository patientRepository;
    private List<Integer> testPIds = new ArrayList<>();
    private Patient testPatientEncounters1;
    private Factory factory;
    private IObservationView observationView;
    private IObservationPresenter observationPresenter;


    @Before
    public void setUp() throws SQLException {
        factory = new Factory(applicationManager);
        patientSyncService = new PatientSyncService(applicationManager);
        patientRepository = new PatientRepository(applicationManager);
        testPatientEncounters1 = factory.getPatientNewWithEncounters("TEST0002", "Shiko");
        testPIds.add(testPatientEncounters1.getId());
        IObservationView view = Mockito.mock(IObservationView.class);
        observationPresenter = new ObservationPresenter(view, applicationManager);
        observationView = observationPresenter.getView();
    }

    @Test
    public void should_load_patient() throws Exception {
        Assert.assertNotNull(testPatientEncounters1);
        observationPresenter.loadPatient(testPatientEncounters1.getId());

        Assert.assertNotNull(observationView.getPatient());
        System.out.println(observationView.getPatient());
    }


    @After
    public void tearDown() throws SQLException {
        for (int id : testPIds) {
            try {
                patientRepository.delete(id);
            } catch (SQLException ex) {

            }
        }
    }
}
