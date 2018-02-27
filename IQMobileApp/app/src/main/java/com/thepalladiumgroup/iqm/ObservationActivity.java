package com.thepalladiumgroup.iqm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.thepalladiumgroup.iqm.common.Messages;
import com.thepalladiumgroup.iqm.core.model.ActiveSession;
import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.MDataType;
import com.thepalladiumgroup.iqm.core.model.Observation;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.SyncAction;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.presentation.presenter.IObservationPresenter;
import com.thepalladiumgroup.iqm.presentation.presenter.impl.ObservationPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IObservationView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ObservationActivity extends AppCompatActivity implements IObservationView {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(ObservationActivity.class);
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);


    private final ActiveSession mActiveSession = new ActiveSession(ObservationActivity.class.getName());
    private final InitializeTask initializeTask = new InitializeTask();
    IObservationPresenter presenter;
    private Patient patient;
    private List<MConcept> concepts;
    private List<ActiveSession> activeSessions = new ArrayList<>();
    private List<String> data;
    private List<Observation> observations;
    private Observation observation;
    private MConcept concept;
    private Encounter encounter;
    private EncounterType encounterType;
    private int index;
    private int questionNumber;
    private int indexTotal;
    private int encounterTypeId;
    private int encounterId;
    private int patientId;
    private int conceptId;
    private boolean mode_new;
    private boolean mode_continue;
    private boolean mode_review;
    private boolean naChecked = false;
    private TextView mFullName;
    private TextView mSex;
    private TextView mDob;
    private TextView mAge;
    private TextView mError;
    private TextView mClientcode;
    private TextView mCurrentPage;
    private TextView mCurrentPageTotal;
    private TextView mQuestionStatus;
    private ProgressBar mProgressBarStatus;
    private TextView mCurrentConcept;
    private TextView mCurrentConceptInsturction;
    private TextView mEncounterId;
    private TextView mEncounterType;
    private Button mNextButton;
    private Button mPreviousButton;
    private CheckBox mNACheckbox;
    private List<CheckBox> mCheckboxGroup = new ArrayList<>();
    private LinearLayout mObsWidgetConatiner;
    private ProgressDialog progressBar;
    private boolean bBackButtonClicked = false;
    private EditText etControl;

    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    @Override
    public void setPresenter(IObservationPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Patient getPatient() {
        return patient;
    }

    @Override
    public void setPatient(Patient patient) {
        this.patient = patient;
        //setCurrentView(patient);
    }

    @Override
    public List<ActiveSession> getActiveSessions() {
        return getCurrentActiveSessions();
    }

    @Override
    public void setActiveSession(List<ActiveSession> activeSessions) {
        this.activeSessions = activeSessions;
    }

    @Override
    public Encounter getEncounter() {
        return this.encounter;
    }

    @Override
    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    @Override
    public EncounterType getEncounterType() {
        return encounterType;
    }

    @Override
    public void setEncounterType(EncounterType encounterType) {
        this.encounterType = encounterType;
        //displayEncounterType(encounterType);
    }

    private void displayEncounterType(EncounterType encounterType) {
        this.encounterType = encounterType;
        if (encounterType != null) {
            mEncounterType.setText(this.encounterType.getDisplay());
        }
    }

    @Override
    public int getEncounterId() {
        return encounterId;
    }

    @Override
    public void setEncounterId(int encounterId) {
        this.encounterId = encounterId;
    }

    @Override
    public List<MConcept> getConcepts() {
        return concepts;
    }

    @Override
    public void setConcepts(List<MConcept> concepts) {
        this.concepts = concepts;
    }

    @Override
    public int getEncounterTypeId() {
        return encounterTypeId;
    }

    @Override
    public void setEncounterTypeId(int encounterTypeId) {
        this.encounterTypeId = encounterTypeId;
    }

    @Override
    public int getPatientId() {
        return patientId;
    }

    @Override
    public int getConceptId() {
        return conceptId;
    }

    @Override
    public void setConceptId(int conceptId) {
        this.conceptId = conceptId;
    }

    @Override
    public MConcept getActiveConcept() {
        return concept;
    }

    @Override
    public void setActiveConcept(MConcept concept) {
        showActiveConcpet(concept);
    }

    @Override
    public Observation getActiveObservation() {
        return observation;
    }

    @Override
    public void setActiveObservation(Observation observation) {
        this.observation = observation;
    }

    @Override
    public int getCurrentQuestionPosition() {
        this.index = 0;
        if (this.concepts != null) {
            this.index = this.concepts.indexOf(this.concept);
        }
        return this.index;
    }

    @Override
    public int getCurrentQuestionNumber() {
        this.questionNumber = getCurrentQuestionPosition() + 1;
        return this.questionNumber;
    }

    @Override
    public int getTotalQuestions() {
        this.indexTotal = 0;
        if (this.concepts != null) {
            indexTotal = this.concepts.size();
        }
        return indexTotal;
    }


    public User getCurrentUser(){
        IQMobileApplication app = (IQMobileApplication) this.getApplication();
        return app.getCurrentUser();
    }

    @Override
    public boolean canMovePrevious() {
        return false;
    }

    @Override
    public boolean canMoveNext() {
        return false;
    }

    @Override
    public void moveFinal() {
        Messages.show(this, "Records were successfuly saved");

        Intent intent = new Intent(this, PateintManagerActivity.class);
        intent.putExtra("patient_id", patientId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean inEditMode() {
        return false;
    }

    @Override
    public void setViewErrors(String error) {
        mError.setText("");
        if (error.length() != 0) {
            mError.setText(error);
        }
    }

    @Override
    public void showProgress() {
        String status = "Question " + getCurrentQuestionNumber() + " of " + getTotalQuestions();
        mQuestionStatus.setText(status);
        if (getCurrentQuestionNumber() > 0 && getTotalQuestions() > 0) {
            Double progress;
            Double i = Double.valueOf(getCurrentQuestionNumber());
            Double c = Double.valueOf(getTotalQuestions());
            progress = (i / c) * 100;
            mProgressBarStatus.setProgress(progress.intValue());
        }
    }

    @Override
    public void isBusy(boolean state) {
        mNextButton.setEnabled(!state);
        mPreviousButton.setEnabled(!state);
    }

    @Override
    public void showPrgressDialog(boolean state, String message) {

        if (!state) {
            progressBar.dismiss();
            return;
        }

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading...");
        if (message != null) {
            progressBar.setMessage(message);
        }
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);
        initialize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        SLF_LOGGER.debug("Open Pateint Home");
        Intent intent = new Intent(this, PateintManagerActivity.class);
        intent.putExtra("patient_id", patientId);
        startActivity(intent);
        finish();
    }


    @Override
    public void initialize() {
        patientId = 0;
        encounterTypeId = 0;
        encounterId = 0;

        mode_new = false;
        mode_continue = false;
        mode_review = false;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.observation_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_patient_observation));

        Bundle extras = getIntent().getExtras();
        initControls();

        if (extras != null) {
            patientId = extras.getInt("active_patient_id");
            encounterTypeId = extras.getInt("encounter_type_id");
            encounterId = extras.getInt("encounter_id");
            mode_new = extras.getBoolean("mode_new");
            mode_continue = extras.getBoolean("mode_continue");
            mode_review = extras.getBoolean("mode_review");
        }


        presenter = new ObservationPresenter(this, (IQMobileApplication) getApplication());
        initializeTask.execute();
    }

    private void initControls() {
        mFullName = (TextView) findViewById(R.id.textViewObsPatientName);
        mSex = (TextView) findViewById(R.id.textViewObsPatientSex);
        mDob = (TextView) findViewById(R.id.textViewObsPatientDob);
        mAge = (TextView) findViewById(R.id.textViewObsPatientAge);
        mError = (TextView) findViewById(R.id.textViewObsError);
        mClientcode = (TextView) findViewById(R.id.textViewObsPatientIdentifier);
        mCurrentPage = (TextView) findViewById(R.id.textViewObsPage);
        mCurrentConcept = (TextView) findViewById(R.id.textViewConceptQuestion);
        mCurrentConceptInsturction = (TextView) findViewById(R.id.textViewConceptQuestionInfo);
        mObsWidgetConatiner = (LinearLayout) findViewById(R.id.layout_obs_widget);
        mEncounterId = (TextView) findViewById(R.id.textViewObsEncounter);
        mEncounterType = (TextView) findViewById(R.id.textViewObsEncounterType);
        mQuestionStatus = (TextView) findViewById(R.id.textViewQuestionStatus);
        mProgressBarStatus = (ProgressBar) findViewById(R.id.progressBarStatus);
        mNextButton = (Button) findViewById(R.id.buttonNextConcept);
        mPreviousButton = (Button) findViewById(R.id.buttonPreviousConcept);
    }

    private void SetFocusOnControl(EditText et)
    {
        if(mode_review==false) {
            etControl = et;
            et.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    private void HideKeyboard()
    {
        if(etControl!=null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etControl.getWindowToken(), 0);
        }
    }

    @Override
    public void loadWidget() {

        Observation obs = null;
        mCheckboxGroup = new ArrayList<>();

        if (mObsWidgetConatiner.getChildCount() > 0) {
            mObsWidgetConatiner.removeAllViews();
        }

        if (concept == null) {
            return;
        }

        obs = restoreObservation(concept);
        if (obs != null) {
            mObsWidgetConatiner.setTag(obs.getId());
        } else {
            mObsWidgetConatiner.setTag(0);
        }

        if (concept.getDataTypeMap().getDataType() == MDataType.TEXT) {
            // add edit text
            EditText et = new EditText(this);
            setIdOfView(et);

            et.setTag(concept);
            if (obs != null) {
                et.setText(obs.getObsvalueString());
            } else {
                if (concept.isAutcompute()) {
                    String result = concept.getComputedResult(getPatient(), getEncounter().getObservationsList(), getCurrentUser(), TransactionTime.RegTime, TransactionTime.StartTime);
                    if (result != null) {
                        et.setText(result);
                        et.setEnabled(false);
                    }
                }
            }
            SetFocusOnControl(et);
            mObsWidgetConatiner.addView(et);
        }
        if (concept.getDataTypeMap().getDataType() == MDataType.TEXTMULTI) {
            // add edit text
            EditText et = new EditText(this);
            setIdOfView(et);
            et.setTag(concept);
            if (obs != null) {
                et.setText(obs.getObsvalueString());
            }
            SetFocusOnControl(et);
            mObsWidgetConatiner.addView(et);
        }
        if (concept.getDataTypeMap().getDataType() == MDataType.NUMERICDECIMAL) {
            // add edit text
            EditText et = new EditText(this);
            setIdOfView(et);
            et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            et.setTag(concept);
            if (obs != null) {
                et.setText(obs.getObsvalueString());
            }
            SetFocusOnControl(et);
            mObsWidgetConatiner.addView(et);
        }
        if (concept.getDataTypeMap().getDataType() == MDataType.NUMERIC) {
            // add edit text
            EditText et = new EditText(this);
            setIdOfView(et);
            et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            et.setTag(concept);
            if (obs != null) {
                et.setText(obs.getObsvalueString());
            }
            SetFocusOnControl(et);
            mObsWidgetConatiner.addView(et);
        }

        if (concept.getDataTypeMap().getDataType() == MDataType.YESNO) {

            RadioGroup rg = new RadioGroup(this); //create the RadioGroup

            //---------------------------------------------------------------------------
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    if(bBackButtonClicked==false && mode_review==false) {
                        MoveToNextQuestion();
                    }
                }
            });
            //---------------------------------------------------------------------------

            setIdOfView(rg);
            rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
            rg.setTag(concept);

            RadioButton rbYes = new RadioButton(this);
            setIdOfView(rbYes);
            rbYes.setText("Yes");
            rbYes.setTag(1);
            rg.addView(rbYes);

            RadioButton rbNo = new RadioButton(this);
            setIdOfView(rbNo);
            rbNo.setText("No");
            rbNo.setTag(0);
            rg.addView(rbNo);

            Boolean checkedYes = false;
            if (obs != null) {
                //checkedYes = restoreObservation(concept).getValueBoolean();|
                checkedYes = obs.getValueBoolean();
                //
            } else {
                if (concept.isAutcompute()) {
                    String result = concept.getComputedResult(getPatient(), getEncounter().getObservationsList(), getCurrentUser(), TransactionTime.RegTime, TransactionTime.StartTime);
                    if (result != null) {
                        checkedYes = result.equals("1");
                    }
                }
            }
            if (checkedYes == true) {
                rbNo.setChecked(false);
                rbYes.setChecked(true);
            } else {
                rbNo.setChecked(false);
                rbYes.setChecked(false);
            }

            mObsWidgetConatiner.addView(rg);
        }

        if (concept.getDataTypeMap().getDataType() == MDataType.SELECT) {
            List<Lookup> lookups = concept.getConceptLookups();
            ArrayAdapter<Lookup> lookupArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lookups);
            lookupArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            Spinner cb = new Spinner(this);
            setIdOfView(cb);
            cb.setTag(concept);

            //KK. Fix for auto move mext-------------------------------------------------------------------------------------
            cb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position > 0 && bBackButtonClicked==false && mode_review==false) {
                        MoveToNextQuestion();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //-------------------------------------------------------------------------------------

            if (concept.hasLookupCompute()) {
                List<Lookup> computedlookups = new ArrayList<>();
                List<Lookup> lookupsToCompute = new ArrayList<>();

                for (Lookup ll : lookups) {
                    computedlookups.add(ll);
                }

                String lookuresult = concept.getComputedLookup(getPatient(), getEncounter().getObservationsList());

                if (lookuresult.contains(":")) {
                    lookupsToCompute = findLookups(lookuresult);
                } else {
                    Lookup lookupForCompute = findLookup(Integer.parseInt(lookuresult));
                    lookupsToCompute.add(lookupForCompute);
                }

                for (Lookup l : lookups) {
                    for (Lookup lc : lookupsToCompute) {
                        if (l.equals(lc)) {
                            computedlookups.remove(l);
                        }
                    }
                }
                lookupArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, computedlookups);
                lookupArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            }

            cb.setAdapter(lookupArrayAdapter);

            if (obs != null) {
                int code = obs.getValueNumeric();
                cb.setSelection(lookupArrayAdapter.getPosition(restoreLookup(concept, code)));
            } else {
                if (concept.isAutcompute()) {


                    String result = concept.getComputedResult(getPatient(), getEncounter().getObservationsList(), getCurrentUser(), TransactionTime.RegTime, TransactionTime.StartTime);
                    if (result != null) {
                        Lookup lookup = findLookup(Integer.parseInt(result));

                        if (lookup != null) {
                            int pos = lookupArrayAdapter.getPosition(lookup);
                            cb.setSelection(pos);
                        }
                    }
                }
            }

            //KK. Fix for displaying select options
            if(!mode_review) {
                if(!bBackButtonClicked) {
                    cb.performClick();
                }
            }

            mObsWidgetConatiner.addView(cb);
        }
        if (concept.getDataTypeMap().getDataType() == MDataType.MULTISELECT) {

            List<Lookup> lookups = concept.getConceptLookups();
            List<Lookup> multilookups = new ArrayList<>();
            multilookups = lookups;
            LinearLayout linearLayoutCheckbox = new LinearLayout(this);
            setIdOfView(linearLayoutCheckbox);
            linearLayoutCheckbox.setTag(concept);
            linearLayoutCheckbox.setOrientation(LinearLayout.VERTICAL);

            mObsWidgetConatiner.addView(linearLayoutCheckbox);

            ///////////////

            if (concept.hasLookupCompute()) {
                List<Lookup> computedlookups = new ArrayList<>();
                List<Lookup> lookupsToCompute = new ArrayList<>();

                for (Lookup ll : lookups) {
                    computedlookups.add(ll);
                }

                String lookuresult = concept.getComputedLookup(getPatient(), getEncounter().getObservationsList());

                if (lookuresult.contains(":")) {
                    lookupsToCompute = findLookups(lookuresult);
                } else {
                    Lookup lookupForCompute = findLookup(Integer.parseInt(lookuresult));
                    lookupsToCompute.add(lookupForCompute);
                }

                for (Lookup l : lookups) {
                    for (Lookup lc : lookupsToCompute) {
                        if (l.equals(lc)) {
                            computedlookups.remove(l);
                        }
                    }
                }
                multilookups = computedlookups;
            }


            ////////////
            for (Lookup l : multilookups) {
                CheckBox cb = new CheckBox(this);
                setIdOfView(cb);
                cb.setText(l.getName());
                cb.setTag(l.getIqcareid());
                if (l.isloner()) {
                    mNACheckbox = cb;
                    mNACheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (buttonView.isChecked()) {
                                for (CheckBox checkBox : mCheckboxGroup) {
                                    if (checkBox.isChecked()) {
                                        checkBox.setChecked(false);
                                    }
                                }
                            }
                        }
                    });

                } else {
                    mCheckboxGroup.add(cb);
                    cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (buttonView.isChecked() && mNACheckbox != null) {
                                if (mNACheckbox.isChecked()) {
                                    mNACheckbox.setChecked(false);
                                }
                            }
                        }
                    });
                }
                if (obs != null) {
                    cb.setChecked(obs.valueInMultipleChoice(String.valueOf(l.getIqcareid())));
                }
                if (l.getId() > 0) {
                    linearLayoutCheckbox.addView(cb);
                }
            }
        }
    }

    @Override
    public List<String> getWidgetRawData() {

        return extractData();
    }

    @Override
    public void setWidgetRawData(List<String> rawdata) {

    }

    @Override
    public List<Observation> getObservations() {
        return extractObservations();
    }

    @Override
    public void setObservations(List<Observation> observations) {
        displayObservations(observations);
    }

    private List<String> extractData() {


        data = new ArrayList<>();
        String checkids = "";
        for (int i = 0; i < mObsWidgetConatiner.getChildCount(); i++) {
            View child = mObsWidgetConatiner.getChildAt(i);

            if (child instanceof RadioGroup) {
                RadioGroup radio = (RadioGroup) child;
                int selectedId = radio.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                data.add(radioButton.getTag().toString());
            }


            if (child instanceof CheckBox) {
                CheckBox cb = (CheckBox) child;
                if (cb.isChecked()) {
                    checkids += cb.getTag().toString() + ",";
                }
            }
            if (checkids.length() > 0) {
                data.add(checkids);
            }

            if (child instanceof Spinner) {
                Spinner radio = (Spinner) child;
                Lookup selected = (Lookup) radio.getSelectedItem();
                data.add(String.valueOf(selected.getIqcareid()));
            }
        }
        return data;
    }

    private List<Observation> extractObservations() {
        observations = new ArrayList<>();

        for (int i = 0; i < mObsWidgetConatiner.getChildCount(); i++) {
            View child = mObsWidgetConatiner.getChildAt(i);
            int id = Integer.parseInt(mObsWidgetConatiner.getTag().toString());
            if (child instanceof EditText) {
                EditText editText = (EditText) child;
                observations.add(readObservation((MConcept) editText.getTag(), editText.getText(), id));
            }

            if (child instanceof RadioGroup) {
                RadioGroup radioGroup = (RadioGroup) child;
                int radioRId = radioGroup.getCheckedRadioButtonId();
                if (radioRId != -1) {
                    RadioButton radioButton = (RadioButton) findViewById(radioRId);
                    Observation obsrb = readObservation((MConcept) radioGroup.getTag(), radioButton.getTag(), id);
                    observations.add(obsrb);
                    break;
                }
            }

            if (child instanceof LinearLayout) {
                String selection = "";
                LinearLayout widgetGroup = (LinearLayout) child;
                for (int j = 0; j < widgetGroup.getChildCount(); j++) {
                    View checkBox = widgetGroup.getChildAt(j);
                    if (checkBox instanceof CheckBox) {
                        CheckBox cb = (CheckBox) checkBox;
                        if (cb.isChecked()) {
                            selection += cb.getTag().toString() + ",";
                        }
                    }
                }
                if (selection.length() > 0) {
                    selection += "^";
                    selection = selection.replace(",^", "");
                    observations.add(readObservation((MConcept) widgetGroup.getTag(), selection, id));
                } else {
                    observations.add(readObservation((MConcept) widgetGroup.getTag(), selection, id));
                }
            }

            if (child instanceof Spinner) {
                Spinner spinner = (Spinner) child;
                Lookup selected = (Lookup) spinner.getSelectedItem();
                observations.add(readObservation((MConcept) spinner.getTag(), selected.getIqcareid(), id));
            }
        }
        return observations;
    }

    private Observation readObservation(MConcept concept, Object obsValue, int id) {

        Observation observation = new Observation(encounter);
        observation.setId(id);
        observation.setmConcept(concept);

//        if ((obsValue.toString() == null) || obsValue.toString().trim().length() == 0) {
//            return null;
//        }

        if (concept.getDataTypeMap().getDataType() == MDataType.TEXT) {

            if (obsValue.toString().trim().length() == 0) {
                observation.setValueText("");
                observation.setValueNumeric(-1);
            } else {
                observation.setValueText(obsValue.toString());
            }

        }
        if (concept.getDataTypeMap().getDataType() == MDataType.TEXTMULTI) {
            if (obsValue.toString().trim().length() == 0) {
                observation.setValueText("");
                observation.setValueNumeric(-1);
            } else {
                observation.setValueText(obsValue.toString());
            }
        }
        if (concept.getDataTypeMap().getDataType() == MDataType.NUMERIC) {
            if (observation.isNotNullorEmpty(obsValue.toString())) {
                observation.setValueNumeric(Integer.parseInt(obsValue.toString()));
            } else {
                observation.setValueNumeric(0);
            }
        }
        if (concept.getDataTypeMap().getDataType() == MDataType.NUMERICDECIMAL) {
            if (observation.isNotNullorEmpty(obsValue.toString())) {
                observation.setValueDecimal(Double.parseDouble(obsValue.toString()));
            } else {
                observation.setValueDecimal(0);
            }
        }

        if (concept.getDataTypeMap().getDataType() == MDataType.YESNO) {
            Boolean yes = obsValue.toString().equalsIgnoreCase("1");
            observation.setValueBoolean(yes);
        }

        if (concept.getDataTypeMap().getDataType() == MDataType.SELECT) {
            observation.setValueNumeric(Integer.parseInt(obsValue.toString()));
        }

        if (concept.getDataTypeMap().getDataType() == MDataType.MULTISELECT) {
            observation.setValueMultipleChoice(obsValue.toString());
        }

        return observation;
    }

    private Observation restoreObservation(MConcept concept) {

        for (Observation o : encounter.getObservationsList()) {
            if (o.getmConcept().getId() == concept.getId()) {
                return o;
            }
        }
        return null;
    }

    private Lookup restoreLookup(MConcept concept, int code) {
        for (Lookup o : concept.getConceptLookups()) {
            if (o.getIqcareid() == code) {
                return o;
            }
        }
        return null;
    }

    private Lookup findLookup(int code) {
        for (Lookup o : concept.getConceptLookups()) {
            if (o.getIqcareid() == code) {
                return o;
            }
        }
        return null;
    }

    private List<Lookup> findLookups(String codes) {
        List<Lookup> lookupCodes = new ArrayList<>();

        String[] allCodes = codes.split(":");
        for (String code : allCodes) {
            for (Lookup o : concept.getConceptLookups()) {
                if (o.getIqcareid() == Integer.parseInt(code)) {
                    lookupCodes.add(o);
                }
            }
        }
        return lookupCodes;
    }

    private void displayObservations(List<Observation> observations) {
        this.observations = observations;
    }

    private void showActiveConcpet(MConcept concept) {
        this.concept = concept;
        mCurrentConcept.setText(concept.getDisplay());
        mCurrentConceptInsturction.setText(concept.getDescription());
        showProgress();
        loadWidget();
    }

    private void setCurrentView(Patient patient) {
        this.patient = patient;
        mFullName.setText(this.patient.getFullName());
        mSex.setText(this.patient.getGender());
        mDob.setText(this.patient.getDobString());
        mAge.setText(this.patient.getCurrentAgeString());
        mClientcode.setText(String.valueOf(this.patient.getClientcode()));
    }

    private List<ActiveSession> getCurrentActiveSessions() {

        return activeSessions;
    }

    public void onClickPreviousConcept(View view) {
        MoveToPreviousQuestion();
    }

    public void onClickNextConcept(View view) {
        MoveToNextQuestion();
    }

    private void MoveToPreviousQuestion()
    {
        HideKeyboard();
        mPreviousButton.setEnabled(false);
        presenter.movePrevious();
        bBackButtonClicked = true;
        mPreviousButton.setEnabled(true);
    }

    private void MoveToNextQuestion()
    {
        HideKeyboard();
        mNextButton.setEnabled(false);
        presenter.moveNext();
        bBackButtonClicked = false;
        mNextButton.setEnabled(true);
    }

    private void setIdOfView(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {

            view.setId(generateViewId());

        } else {

            view.setId(View.generateViewId());

        }
    }

    private class InitializeTask extends AsyncTask<SyncAction, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            isBusy(true);
            showPrgressDialog(true, "Loading..");
        }

        @Override
        protected Boolean doInBackground(SyncAction... params) {
            //SLF_LOGGER.debug("initializing.........");
            boolean success = true;
            presenter.loadPatient(patientId, encounterTypeId);


            presenter.loadEncounter();

            presenter.loadConcepts();
            mActiveSession.setActivePatientId(String.valueOf(patientId));
            presenter.loadActiveSession(mActiveSession, mode_review);
            //presenter.setActiveConcept();
            return success;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //SLF_LOGGER.debug("initializing Done");
            setCurrentView(patient);
            displayEncounterType(encounterType);
            presenter.setActiveConcept();
            isBusy(false);
            showPrgressDialog(false, null);
        }
    }
}
