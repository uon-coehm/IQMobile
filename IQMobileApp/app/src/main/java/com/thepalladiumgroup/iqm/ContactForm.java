package com.thepalladiumgroup.iqm;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.presentation.presenter.IContactsPresenter;
import com.thepalladiumgroup.iqm.presentation.presenter.impl.ContactsPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IContactsView;
import com.thepalladiumgroup.iqm.presentation.view.IRegistrationView;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.layouts.BasicWizardLayout;
import org.codepond.wizardroid.persistence.ContextVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactForm extends WizardStep implements IContactsView, AdapterView.OnItemSelectedListener {
    //region fields
    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(LoginActivity.class);
    private static Context currentContex;
    private final Gson gson=Converters.registerDateTime(new GsonBuilder()).create();
    private final int DEFUALT_REL_ID = 425;
    private Activity mActivity;
    private IRegistrationView mRegistration;
    private View currentView;
    private IContactsPresenter presenter;
    private Patient contacts;
    private List<Lookup> lookups;
    private Lookup selectedLookup;
    private BasicWizardLayout mWizardC;
    private ArrayAdapter<Lookup> lookupArrayAdapter;
    //endregion

    //region controls
    private TextView mId;
    // @NotEmpty(messageId = R.string.validation_contact_Cell)
// @MinLength(value = 10, messageId = R.string.validation_contact_Cell_len, order = 1)
    private EditText mTelephone;
    private EditText mKin;
    private EditText mKinTelephone;
    private Spinner mKinRelationsList;
    @NotEmpty(messageId = R.string.validation_other_relation)
    private EditText mKinRelationOther;
    private TextView mSummary;
    //endregion

    @ContextVariable
    private String currentPatientJson;


    public ContactForm() {
        // Required empty public constructor
    }

    @Override
    public void setPresenter(IContactsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public IRegistrationView getRegistrationData() {
        return mRegistration;
    }

    @Override
    public Patient getContacts() {
        return getCurrentView();
    }

    @Override
    public void setContacts(Patient contacts) {
        setCurrentView(contacts);
    }

    @Override
    public void setLookups(List<Lookup> lookups) {
        setupLookups(lookups);
    }

    @Override
    public boolean inEditMode() {
        return mRegistration.inEditMode();
    }

    @Override
    public boolean isValidPage() {
        return viewIsValid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_contact_form, container, false);
        currentContex = container.getContext();
        mWizardC = (BasicWizardLayout) this.getParentFragment();
        initialize();
        return currentView;
    }
    @Override
    public void initialize() {
        //mRegistration.setSubtitle(mActivity.getString(R.string.title_contact));
        mId = (TextView) currentView.findViewById(R.id.textViewContactPatientId);
        mTelephone = (EditText) currentView.findViewById(R.id.editTextContactTelephone);
        mKin = (EditText) currentView.findViewById(R.id.editTextContactKin);
        mKinTelephone = (EditText) currentView.findViewById(R.id.editTextContactKinTelephone);
        mKinRelationsList = (Spinner) currentView.findViewById(R.id.spinnerRelations);
        mKinRelationsList.setOnItemSelectedListener(this);
        mKinRelationOther = (EditText) currentView.findViewById(R.id.editTextKinRelationOther);

        mSummary = (TextView) currentView.findViewById(R.id.textViewContactPatientName);


        loadData();
        IQMobileApplication app = (IQMobileApplication) getActivity().getApplication();

        presenter = new ContactsPresenter(this, app);
        presenter.loadLookups();
        presenter.loadContacts();
    }

    @Override
    public boolean viewIsValid() {

        boolean valid = false;
        if (this != null) {
            valid = FormValidator.validate(this, new SimpleErrorPopupCallback(this.getContext(), true));
        }
        return valid;
    }

    @Override
    public void serializeView() {
        SLF_LOGGER.debug("creating Demographics json...");
        currentPatientJson = presenter.getJsonView();
        SLF_LOGGER.debug("Demographics json:" + currentPatientJson);
    }

    @Override
    public void updateRegistrationData() {
        SLF_LOGGER.debug("editMode Contacts");
        contacts.updateContacts(getContacts());
        mRegistration.editPatient(contacts);
    }

    private Patient getCurrentView() {
        int kinrelid = DEFUALT_REL_ID;
        if (mKinRelationsList.getSelectedItem() != null) {
            Lookup selcted = (Lookup) mKinRelationsList.getSelectedItem();
            kinrelid = -1;
            if (selcted.getId() != -1) {
                kinrelid = selcted.getIqcareid();
            }
        }
        contacts.setId(Integer.parseInt(mId.getText().toString()));

        if (mId.getTag() != null) {
            contacts.setUuid(mId.getTag().toString());
        }

        contacts.setContactphone(mTelephone.getText().toString());
        contacts.setKin(mKin.getText().toString());
        contacts.setKinphone(mKinTelephone.getText().toString());
        contacts.setKinrelationother("");
        if (mKinRelationOther.getVisibility() == View.VISIBLE) {
            contacts.setKinrelationother(mKinRelationOther.getText().toString());
        }
        contacts.setKinrelation(kinrelid);


        return contacts;
    }

    private void setCurrentView(Patient contacts) {
        this.contacts = contacts;
        mId.setText(String.valueOf(contacts.getId()));
        mId.setTag(contacts.getUuid());
        mTelephone.setText(contacts.getContactphone());
        mKin.setText(contacts.getKin());
        mKinTelephone.setText(contacts.getKinphone());

        Lookup selected = getSelectedLookup(contacts.getKinrelation());
        if (selected != null) {
            mKinRelationsList.setSelection(lookupArrayAdapter.getPosition(selected));
            mKinRelationOther.setText(contacts.getKinrelationother());
        }
    }

    private void setupLookups(List<Lookup> lookups) {
        this.lookups = lookups;
        lookupArrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, lookups);
        lookupArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        mKinRelationsList.setAdapter(lookupArrayAdapter);

    }
    private void loadData() {
        if (inEditMode()) {
            return;
        }
        this.contacts = gson.fromJson(currentPatientJson, Patient.class);
    }
    private Lookup getSelectedLookup(int lookupid) {
        Lookup selected = null;
        for (Lookup lookup : lookups) {
            if (lookup.getIqcareid() == lookupid) {
                selected = lookup;
            }
        }
        return selected;
    }

    @Override
    public void onExit(int exitCode) {

        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                if(inEditMode())
                {
                    updateRegistrationData();
                }else {
                    serializeView();
                }
                break;
            case WizardStep.EXIT_PREVIOUS:
                //Do nothing...
                break;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        FormValidator.stopLiveValidation(this);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mRegistration = (IRegistrationView) mActivity;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedLookup = lookupArrayAdapter.getItem(position);
        if (selectedLookup.isother()) {
            mKinRelationOther.setVisibility(View.VISIBLE);
        } else {
            mKinRelationOther.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mKinRelationOther.setVisibility(View.INVISIBLE);
    }
}
