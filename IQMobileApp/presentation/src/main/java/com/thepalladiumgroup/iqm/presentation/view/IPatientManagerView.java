package com.thepalladiumgroup.iqm.presentation.view;

import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Module;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.presentation.presenter.IPatientManagerPresenter;

import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/16]
 */
public interface IPatientManagerView {
    void setPresenter(IPatientManagerPresenter presenter);

    List<Module> getModules();
    void setModules(List<Module> modules);

    List<EncounterType> getEncounterTypes();

    void setEncounterTypes(List<EncounterType> encounterTypes);

    List<Encounter> getEncounters();

    void setEncounters(List<Encounter> encounters);

    Patient getPatient();
    void setPatient(Patient patient);
    void initialize();
    void setViewErrors(String error);
}
