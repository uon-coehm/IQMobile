package com.thepalladiumgroup.iqm.presentation.view;

import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.presentation.presenter.IServiceAreaPresenter;

import java.util.List;


/**
 * Created by Koske Kimutai [2016/04/18]
 */
public interface IServiceAreaView {

    void setPresenter(IServiceAreaPresenter presenter);
    IRegistrationView getRegistrationData();

    Patient getServciceArea();

    void setServciceArea(Patient servciceArea);

    void setLookups(List<Lookup> lookups);

    void setPartnersList(List<Patient> partners);

    Patient getPartner();

    boolean inEditMode();
    void initialize();
    boolean viewIsValid();
    void updateRegistrationData();

    void serializeView();

    void setViewErrors(String error);
}
