package com.thepalladiumgroup.iqm;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thepalladiumgroup.iqm.common.OnDateChangedEvent;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.presentation.presenter.IServiceAreaPresenter;
import com.thepalladiumgroup.iqm.presentation.presenter.impl.ServiceAreaPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IRegistrationView;
import com.thepalladiumgroup.iqm.presentation.view.IServiceAreaView;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.layouts.BasicWizardLayout;
import org.codepond.wizardroid.persistence.ContextVariable;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectServiceForm extends WizardStep implements IServiceAreaView {
    //region fields
    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(LoginActivity.class);
    private final Gson gson = Converters.registerAll(new GsonBuilder()).create();
    private Activity mActivity;
    private IRegistrationView mRegistration;
    private BasicWizardLayout mWizardS;
    private View currentView;
    private IServiceAreaPresenter presenter;
    private Patient servciceArea;
    private Patient partner;
    private String viewErrors;
    private Context currentContex;
    private List<Lookup> lookups;
    private List<Patient> partners;
    private ArrayAdapter<Lookup> lookupArrayAdapter;
    private ArrayAdapter<Patient> partnerArrayAdapter;
    //endregion

    //region controls
    private TextView mId;
    private DatePicker mRegistrationDate;
    private Spinner mIdTypes;
    private Spinner mPartners;
    @NotEmpty(messageId = R.string.validation_client_code, order = 1)
    private EditText mClientCode;
    private TextView mSummary;
    private TextView mError;
    //endregion

    @ContextVariable
    private String currentPatientJson;

    public SelectServiceForm() {
        // Required empty public constructor
    }

    @Override
    public void setPresenter(IServiceAreaPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public IRegistrationView getRegistrationData() {
        return mRegistration;
    }

    @Override
    public Patient getServciceArea() {
        return getCurrentView();
    }

    @Override
    public void setServciceArea(Patient servciceArea) {
        setCurrentView(servciceArea);
    }

    @Override
    public void setLookups(List<Lookup> lookups) {
        setupLookups(lookups);
    }

    @Override
    public void setPartnersList(List<Patient> partners) {
        setupPartners(partners);
    }

    @Override
    public Patient getPartner() {
        return getSelectedPartner();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_select_service_form, container, false);
        currentContex = container.getContext();
        mWizardS = (BasicWizardLayout) this.getParentFragment();
        initialize();
        return currentView;
    }

    @Override
    public void initialize() {

        //mRegistration.setSubtitle(mActivity.getString(R.string.title_service));
        mId = (TextView) currentView.findViewById(R.id.textViewRegistrationPatientId);
        mRegistrationDate = (DatePicker) currentView.findViewById(R.id.datePickerRegistrationDate);
        mClientCode = (EditText) currentView.findViewById(R.id.editTextRegistrationIdentifier);
        mSummary = (TextView) currentView.findViewById(R.id.textViewRegistrationPatientName);
        mIdTypes = (Spinner) currentView.findViewById(R.id.spinnerIdentifierType);
        mPartners = (Spinner) currentView.findViewById(R.id.spinnerPartnerList);
        mError = (TextView) currentView.findViewById(R.id.textViewRegistrationError);
        loadData();
        //setDefaults();
        presenter = new ServiceAreaPresenter(this, (IQMobileApplication) getActivity().getApplication());
        presenter.loadLookups();
        presenter.loadServiceArea();
        presenter.loadPartners();
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
    public void updateRegistrationData() {
        SLF_LOGGER.debug("editMode Registration");
        servciceArea.updateRegistration(getServciceArea());
        mRegistration.editPatient(servciceArea);
    }

    @Override
    public void serializeView() {
        SLF_LOGGER.debug("creating Demographics json...");
        currentPatientJson = presenter.getJsonView();
        SLF_LOGGER.debug("Demographics json:" + currentPatientJson);
    }

    @Override
    public void setViewErrors(String error) {
        showError(error);
    }

    private Patient getCurrentView() {
        LocalDate regDate = new LocalDate(mRegistrationDate.getYear(), mRegistrationDate.getMonth() + 1, mRegistrationDate.getDayOfMonth());

        servciceArea.setId(Integer.parseInt(mId.getText().toString()));


        if (mId.getTag() != null) {
            servciceArea.setUuid(mId.getTag().toString());
        }

        servciceArea.setEnrollmentdate(regDate.toDate());

        if (mIdTypes.getSelectedItem() != null) {
            Lookup lookup = (Lookup) mIdTypes.getSelectedItem();
            servciceArea.setIdtype(lookup.getIqcareid());
        }
        if (mPartners.getSelectedItem() != null) {
            Patient partner = (Patient) mPartners.getSelectedItem();
            if (partner.getId() != -1) {
                servciceArea.setPartner(partner);
            }
        }
        servciceArea.setClientcode(mClientCode.getText().toString());
        return servciceArea;
    }
    private void setCurrentView(Patient servciceArea) {
        this.servciceArea = servciceArea;
        mId.setText(String.valueOf(this.servciceArea.getId()));
        mId.setTag(this.servciceArea.getUuid());
        if (this.servciceArea.getEnrollmentdate() != null) {
            mRegistrationDate.updateDate(this.servciceArea.getEnrollmentdateLocal().getYear(), this.servciceArea.getEnrollmentdateLocal().getMonthOfYear() - 1, this.servciceArea.getEnrollmentdateLocal().getDayOfMonth());
        }
        Lookup selected = getSelectedLookup(this.servciceArea.getIdtype());
        if (selected != null) {
            mIdTypes.setSelection(lookupArrayAdapter.getPosition(selected));
        }
        mClientCode.setText(this.servciceArea.getClientcode());
        mWizardS = (BasicWizardLayout) this.getParentFragment();
    }

    private void setupLookups(List<Lookup> lookups) {
        this.lookups = lookups;
        lookupArrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, lookups);
        lookupArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        mIdTypes.setAdapter(lookupArrayAdapter);

    }

    private void setupPartners(List<Patient> partners) {
        this.partners = partners;
        partnerArrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, partners);
        partnerArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        mPartners.setAdapter(partnerArrayAdapter);

        if (this.servciceArea.isCouple()) {
            int position = partnerArrayAdapter.getPosition(this.servciceArea.getPartner());
            mPartners.setSelection(position);
        } else {
            mPartners.setSelection(0);
        }

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

    private Patient getSelectedPartner() {
        partner = null;
        if (mPartners.getSelectedItem() != null) {
            partner = (Patient) mPartners.getSelectedItem();
        }
        return partner;
    }
    private void loadData() {
        if (inEditMode()) {
            return;
        }
        this.servciceArea = gson.fromJson(currentPatientJson, Patient.class);
    }
    private void setDefaults() {

        OnDateChangedEvent onDateChangedEvent = new OnDateChangedEvent();
        if (this.servciceArea != null) {
            onDateChangedEvent.setMinDate(this.servciceArea.getDobLocal());
        }

        mRegistrationDate.init(onDateChangedEvent.getMaxDate().getYear(), onDateChangedEvent.getMaxDate().getMonthOfYear() - 1, onDateChangedEvent.getMaxDate().getDayOfMonth(), onDateChangedEvent);

    }
    private void showError(String error) {
        mError.setText("");
        if (error.length() != 0) {
            mError.setText(error);
        }
    }

    @Override
    public void onExit(int exitCode) {

        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                if (inEditMode()) {
                    updateRegistrationData();
                }
                if (presenter.savePatient()) {

                    Intent intent;

                    if (inEditMode()) {
                        SLF_LOGGER.debug("Open Pateint Home");
                        intent = new Intent(mActivity, PateintManagerActivity.class);
                        intent.putExtra("patient_id", servciceArea.getId());
                    } else {
                        SLF_LOGGER.debug("Open FindAdd");
                        intent = new Intent(mActivity, FindAddPatientActivity.class);
                    }

                    startActivity(intent);
                    getActivity().finish();
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
}
