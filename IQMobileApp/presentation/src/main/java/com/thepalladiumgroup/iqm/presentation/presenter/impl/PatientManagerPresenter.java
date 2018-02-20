package com.thepalladiumgroup.iqm.presentation.presenter.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Module;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.services.IEncounterService;
import com.thepalladiumgroup.iqm.core.services.IEncounterTypeService;
import com.thepalladiumgroup.iqm.core.services.IModuleService;
import com.thepalladiumgroup.iqm.core.services.IPatientService;
import com.thepalladiumgroup.iqm.core.services.impl.EncounterService;
import com.thepalladiumgroup.iqm.core.services.impl.EncounterTypeService;
import com.thepalladiumgroup.iqm.core.services.impl.ModuleService;
import com.thepalladiumgroup.iqm.core.services.impl.PatientService;
import com.thepalladiumgroup.iqm.presentation.presenter.IPatientManagerPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IPatientManagerView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/16]
 */
public class PatientManagerPresenter implements IPatientManagerPresenter {
    private final IPatientService patientService;
    private final IModuleService moduleService;
    private final IEncounterService encounterService;
    private final IEncounterTypeService encounterTypeService;
    private IPatientManagerView view;

    public PatientManagerPresenter(IPatientManagerView view, IApplicationManager applicationManager) {
        this.view = view;
        this.view.setPresenter(this);
        this.patientService = new PatientService(applicationManager);
        this.moduleService = new ModuleService(applicationManager);
        this.encounterTypeService = new EncounterTypeService(applicationManager);
        this.encounterService = new EncounterService(applicationManager);
    }

    @Override
    public IPatientManagerView getView() {
        return view;
    }

    @Override
    public void loadPatient(int patientid) {
        Patient patient = null;
        try {
            patient = patientService.findDemographics(patientid);
        } catch (SQLException e) {
            view.setViewErrors("Error loading patient " + e.getMessage());
            e.printStackTrace();
        }
        getView().setPatient(patient);
    }

    @Override
    public void loadModules() {
        Module module = null;
        List<Encounter> encounters = null;
        List<EncounterType> encounterTypes = null;
        EncounterType encounterType = null;

        try {
            module = moduleService.getByName("HTS");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Module> moduleList = new ArrayList<>();
        moduleList.add(module);
        getView().setModules(moduleList);

        try {
            encounterTypes = encounterTypeService.getByModuleId(module.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getView().setEncounterTypes(encounterTypes);
        encounterType = encounterTypes.get(0);

        try {
            encounters = encounterService.getSummaryByPatient(getView().getPatient().getId(), encounterType.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getView().setEncounters(encounters);


    }


}
