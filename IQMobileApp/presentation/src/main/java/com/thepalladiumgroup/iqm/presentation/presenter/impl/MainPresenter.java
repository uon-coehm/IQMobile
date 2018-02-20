package com.thepalladiumgroup.iqm.presentation.presenter.impl;


import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.PatientStats;
import com.thepalladiumgroup.iqm.core.model.RecordStats;
import com.thepalladiumgroup.iqm.core.services.IEncounterService;
import com.thepalladiumgroup.iqm.core.services.IPatientService;
import com.thepalladiumgroup.iqm.core.services.impl.EncounterService;
import com.thepalladiumgroup.iqm.core.services.impl.PatientService;
import com.thepalladiumgroup.iqm.presentation.presenter.IMainPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IMainView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;


/**
 * Created by Koske Kimutai [2016/04/15]
 */
public class MainPresenter implements IMainPresenter {

    private static final Logger LOG = LoggerFactory.getLogger(SyncPresenter.class);
    private final IApplicationManager applicationManager;
    private final IPatientService patientService;
    private final IEncounterService encounterService;
    private IMainView view;

    public MainPresenter(IMainView view, IApplicationManager applicationManager) {
        this.view = view;
        this.view.setPresenter(this);
        this.applicationManager = applicationManager;
        this.patientService = new PatientService(applicationManager);
        this.encounterService = new EncounterService(applicationManager);
    }

    @Override
    public IMainView getView() {
        return view;
    }

    @Override
    public void cleanUp() {
        try {
            patientService.cleanUpPatients();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadCurrentUser() {

        getView().setCurrentUser(applicationManager.getCurrentUser());
    }

    @Override
    public void loadPatientStats() {

        PatientStats patientStats = new PatientStats(0, 0, 0);
        try {
            patientStats = patientService.getStats();
            getView().setPatientStats(patientStats);
        } catch (SQLException e) {
            LOG.debug("Error getting stats", e);
        }
    }

    @Override
    public void loadRecordStats() {
        RecordStats recordStats = new RecordStats(0, 0, 0);
        try {
            recordStats = encounterService.getRecordStats();
            getView().setRecordStats(recordStats);
        } catch (SQLException e) {
            LOG.debug("Error getting Record stats", e);
        }
    }
}
