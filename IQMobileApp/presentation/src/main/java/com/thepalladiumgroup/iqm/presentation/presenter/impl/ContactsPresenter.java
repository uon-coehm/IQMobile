package com.thepalladiumgroup.iqm.presentation.presenter.impl;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.services.ILookupService;
import com.thepalladiumgroup.iqm.core.services.impl.LookupService;
import com.thepalladiumgroup.iqm.presentation.presenter.IContactsPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IContactsView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/18]
 */
public class ContactsPresenter implements IContactsPresenter {
    private final Gson gson;
    private final int RELATIONSHIP_LOOKUP = 8;
    private final ILookupService lookupService;
    private IContactsView view;

    public ContactsPresenter(IContactsView view, IApplicationManager applicationManager) {
        this.view = view;
        this.view.setPresenter(this);
        this.lookupService = new LookupService(applicationManager);
        gson = Converters.registerAll(new GsonBuilder()).create();
    }

    @Override
    public IContactsView getView() {
        return view;
    }


    @Override
    public boolean dataIsValid() {
        return getView().viewIsValid();
    }

    @Override
    public String getJsonView() {


        String currentPatientJson = gson.toJson(getView().getContacts());
        return currentPatientJson;
    }

    @Override
    public void loadLookups() {

        List<Lookup> lookups = null;

        try {

            lookups = new ArrayList<>();
            Lookup blank = new Lookup();
            blank.setId(-1);
            blank.setName("Select Option");
            blank.setRank(-1);
            lookups.add(blank);
            for (Lookup l : lookupService.getLookupsByCodeId(RELATIONSHIP_LOOKUP)) {
                lookups.add(l);
            }
            Collections.sort(lookups);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        getView().setLookups(lookups);
    }

    @Override
    public void loadContacts() {
        if (view.inEditMode()) {
            Patient patient = view.getRegistrationData().getPatientForEdit();
            view.setContacts(patient);
        }
    }

    @Override
    public void saveContacts() {

    }
}
