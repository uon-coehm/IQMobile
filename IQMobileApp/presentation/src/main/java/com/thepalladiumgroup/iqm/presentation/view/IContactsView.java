package com.thepalladiumgroup.iqm.presentation.view;

import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.presentation.presenter.IContactsPresenter;

import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/18]
 */
public interface IContactsView {
    void setPresenter(IContactsPresenter presenter);
    IRegistrationView getRegistrationData();

    Patient getContacts();

    void setContacts(Patient contacts);

    void setLookups(List<Lookup> lookups);
    boolean inEditMode();

    void initialize();
    boolean viewIsValid();
    void serializeView();
    void updateRegistrationData();
}
