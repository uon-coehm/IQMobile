package com.thepalladiumgroup.iqm.presentation.presenter;


import com.thepalladiumgroup.iqm.presentation.view.IContactsView;

/**
 * Created by Koske Kimutai [2016/04/18]
 */
public interface IContactsPresenter {

    IContactsView getView();

    boolean dataIsValid();

    String getJsonView();

    void loadLookups();

    void loadContacts();

    void saveContacts();
}
