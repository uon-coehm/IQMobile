package com.thepalladiumgroup.iqm.presentation.presenter.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.ActiveSession;
import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.Observation;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.core.services.IApplicationService;
import com.thepalladiumgroup.iqm.core.services.IConceptService;
import com.thepalladiumgroup.iqm.core.services.IEncounterService;
import com.thepalladiumgroup.iqm.core.services.IEncounterTypeService;
import com.thepalladiumgroup.iqm.core.services.ILookupService;
import com.thepalladiumgroup.iqm.core.services.IObservationService;
import com.thepalladiumgroup.iqm.core.services.IPatientService;
import com.thepalladiumgroup.iqm.core.services.impl.ApplicationService;
import com.thepalladiumgroup.iqm.core.services.impl.ConceptService;
import com.thepalladiumgroup.iqm.core.services.impl.EncounterService;
import com.thepalladiumgroup.iqm.core.services.impl.EncounterTypeService;
import com.thepalladiumgroup.iqm.core.services.impl.LookupService;
import com.thepalladiumgroup.iqm.core.services.impl.ObservationService;
import com.thepalladiumgroup.iqm.core.services.impl.PatientService;
import com.thepalladiumgroup.iqm.presentation.presenter.IObservationPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IObservationView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/21]
 */
public class ObservationPresenter implements IObservationPresenter {
    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(ObservationPresenter.class);
    private final IApplicationService applicationService;
    private final IPatientService patientService;
    private final IEncounterTypeService encounterTypeService;
    private final IEncounterService encounterService;
    private final IConceptService conceptService;
    private final IObservationService observationService;
    private final ILookupService lookupService;
    private User user;
    private IObservationView view;
    private List<Observation> parentObs = new ArrayList<>();


    public ObservationPresenter(IObservationView view, IApplicationManager applicationManager) {
        this.view = view;
        this.view.setPresenter(this);
        this.patientService = new PatientService(applicationManager);
        this.encounterTypeService = new EncounterTypeService(applicationManager);
        this.applicationService = new ApplicationService(applicationManager);
        this.encounterService = new EncounterService(applicationManager);
        this.observationService = new ObservationService(applicationManager);
        this.lookupService = new LookupService(applicationManager);
        this.conceptService = new ConceptService(applicationManager);
        try {
            user = applicationManager.getCurrentUser();
        } catch (Exception e) {
            SLF_LOGGER.debug("Error loading user:", e);
        }

    }

    @Override
    public IObservationView getView() {
        return view;
    }


    @Override
    public void loadPatient(int patientid) {
        SLF_LOGGER.debug("loading patient " + patientid);
        if (patientid == 0) {
            return;
        }
        if (getView().getPatient() != null) {
            return;
        }
        Patient patient = null;
        try {
            //TODO: demographics needed only ?
            //patient = patientService.find(patientid);

            patient = patientService.findPatientPartnerInfo(patientid);
        } catch (SQLException e) {
            view.setViewErrors("Error loading patient data " + e.getMessage());
            SLF_LOGGER.debug(e.getMessage());
        }
        getView().setPatient(patient);
    }

    @Override
    public void loadPatient(int patientid, int encounterTypeId) {
        SLF_LOGGER.debug("loading patient " + patientid);
        if (patientid == 0) {
            return;
        }
        if (getView().getPatient() != null) {
            return;
        }
        Patient patient = null;
        try {
            //TODO: demographics needed only ?
            //patient = patientService.find(patientid);

            patient = patientService.findPatientPartnerInfo(patientid, encounterTypeId);
        } catch (SQLException e) {
            view.setViewErrors("Error loading patient data " + e.getMessage());
            SLF_LOGGER.debug(e.getMessage());
        }
        getView().setPatient(patient);
    }

    @Override
    public void loadEncounter() {

        loadEncounterType();
        SLF_LOGGER.debug("loading encounter...");
        if (getView().getEncounter() != null) {
            return;
            //TODO: Load encounter error
        }
        try {
            Encounter encounter;
            if (getView().getEncounterId() == 0) {
                SLF_LOGGER.debug("starting New encounter...");
                Encounter newEncounter = new Encounter(getView().getEncounterType(), getView().getPatient());
                if (user != null) {
                    //newEncounter.setUserid(user.getId()); //Set IQCare ID instead
                    newEncounter.setUserid(user.getIqcareid());
                }
                encounter = encounterService.saveNew(newEncounter);
                getView().setEncounterId(encounter.getId());
            } else {
                SLF_LOGGER.debug("continuing encounter...");
                encounter = encounterService.getById(getView().getEncounterId());
            }
            getView().setEncounter(encounter);
        } catch (SQLException e) {
            SLF_LOGGER.debug(e.getMessage());
        }

    }

    private void loadEncounterType() {
        SLF_LOGGER.debug("loading encounter-type ...");
        if (getView().getEncounterType() != null) {
            return;
        }
        try {
            //TODO: encounterType name needed only ?
//            EncounterType encounterType = encounterTypeService.getById(getView().getEncounterTypeId());

            EncounterType encounterType = encounterTypeService.getInfo(getView().getEncounterTypeId());
            getView().setEncounterType(encounterType);
        } catch (SQLException e) {
            view.setViewErrors("Error loading encounter-type " + e.getMessage());
            SLF_LOGGER.debug(e.getMessage());
        }
    }

    @Override
    public void loadConcepts() {
        SLF_LOGGER.debug("loading concepts...");


        List<MConcept> encounterTypeConcepts = new ArrayList<>();
        try {
            encounterTypeConcepts = conceptService.getByEncounterTypeId(getView().getEncounterType().getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SLF_LOGGER.debug("loading concepts DONE !");

        if (getView().getConcepts() == null) {
            List<MConcept> concepts = new ArrayList<>();
            for (MConcept concept : encounterTypeConcepts) {
                if (concept.hasLookups()) {
                    try {
                        List<Lookup> lookupsSelect = new ArrayList<>();
                        Lookup blank = new Lookup();
                        blank.setId(-1);
                        blank.setName("Select Option");
                        blank.setIqcareid(-1);
                        blank.setRank(-1);
                        lookupsSelect.add(blank);
                        for (Lookup l : lookupService.getLookupsByCodeId(concept.getLookupcodeid())) {
                            lookupsSelect.add(l);
                        }
                        concept.addLookups(lookupsSelect);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                concepts.add(concept);
            }
            Collections.sort(concepts);
            getView().setConcepts(concepts);
        }
    }

    @Override
    public void loadActiveSession(ActiveSession session, boolean inReview) {

        SLF_LOGGER.debug("checking SESSION DATA...");
        List<ActiveSession> activeSessions = new ArrayList<>();

        //check encounterTypeId
        ActiveSession sessionencounterTypeId = applicationService.getActiveSession(session.getActiveActivity(), session.getActivePatientId(), "encounterTypeId");
        if (sessionencounterTypeId == null) {
            ActiveSession se = new ActiveSession(session.getActiveActivity());
            se.setActivePatientId(session.getActivePatientId());
            se.createSession("encounterTypeId", String.valueOf(getView().getEncounterTypeId()));
            sessionencounterTypeId = applicationService.saveActiveSession(se);
        }
        activeSessions.add(sessionencounterTypeId);

        //check encountereId
        ActiveSession sessionencounterId = applicationService.getActiveSession(session.getActiveActivity(), session.getActivePatientId(), "encounterId");
        if (sessionencounterId == null) {
            ActiveSession sei = new ActiveSession(session.getActiveActivity());
            sei.setActivePatientId(session.getActivePatientId());
            sei.createSession("encounterId", String.valueOf(getView().getEncounterId()));
            sessionencounterId = applicationService.saveActiveSession(sei);
        }
        activeSessions.add(sessionencounterId);

        //check conceptId
        if (!inReview) {
            ActiveSession sessionconceptId = applicationService.getActiveSession(session.getActiveActivity(), session.getActivePatientId(), "conceptId");
            if (sessionconceptId == null) {
                ActiveSession sc = new ActiveSession(session.getActiveActivity());
                sc.setActivePatientId(session.getActivePatientId());
                MConcept first = getView().getConcepts().get(0);
                getView().setConceptId(first.getId());
                sc.createSession("conceptId", String.valueOf(first.getId()));
                sessionconceptId = applicationService.saveActiveSession(sc);
            } else {
                getView().setConceptId(Integer.parseInt(sessionconceptId.getActiveValue()));
            }
            activeSessions.add(sessionconceptId);
        }


        getView().setActiveSession(activeSessions);

        SLF_LOGGER.debug("========= SESSION DATA BEGIN =======");
        for (ActiveSession s : activeSessions) {
            SLF_LOGGER.debug(s.toString());
        }
        SLF_LOGGER.debug("========= SESSION DATA END =======");
    }

    @Override
    public void setActiveConcept() {
        if (getView().getActiveConcept() == null) {
            MConcept activeConcept = findConcept(getView().getConceptId());
            if (activeConcept == null) {
                activeConcept = findConceptByPostion(0);
            }
            getView().setActiveConcept(activeConcept);
        }

    }

    private MConcept findConcept(int conceptId) {
        for (MConcept concept : getView().getConcepts()) {
            if (concept.getId() == conceptId)
                return concept;
        }
        return null;
    }

    private MConcept findConceptByPostion(int conceptId) {
        return getView().getConcepts().get(conceptId);
    }

    @Override
    public void saveActiveSession(ActiveSession session) {
        session.createSession("encounterTypeId", String.valueOf(getView().getEncounterTypeId()));
        applicationService.saveActiveSession(session);
        session.createSession("encounterId", String.valueOf(getView().getEncounterId()));
        applicationService.saveActiveSession(session);
        session.createSession("conceptId", String.valueOf(getView().getActiveConcept().getId()));
        applicationService.saveActiveSession(session);
    }

    @Override
    public void movePrevious() {
        MConcept current = getView().getActiveConcept();
        MConcept previousConcept = null;
        int positon = getView().getConcepts().indexOf(current);
        if (positon == 0 || positon < 0) {
            begin();
            return;
        }
        //MConcept previous = findConceptByPostion(positon - 1);
        Observation obsPervious = getView().getEncounter().getPrevious(current);
        if (null != obsPervious) {
            previousConcept = findConcept(obsPervious.getmConcept().getId());
            getView().setActiveConcept(previousConcept);
        }
    }


    @Override
    public void moveNext() {
        getView().setViewErrors("");
        MConcept activeConcept = getView().getActiveConcept();
        MConcept nextConcept = null;
        boolean canMoveNext = false;
        Observation activeConceptObservation = null;
        Observation parentConceptObservation = null;
        String activeConceptObsValue = "";
        String parentConceptObsValue = "";
        int lastPosition, position, nextPosition;

        //TODO: validate entry
        // get active obs

        for (Observation observation : getView().getObservations()) {
            if (observation != null && observation.getValueNumeric() == -1 && activeConcept.isRequired()) {
                getView().setViewErrors("The field requires a response");
                return;
            }
            if (observation != null) {
                activeConceptObservation = saveObservation(observation);
            }
        }

        if (activeConceptObservation != null) {
            activeConceptObsValue = activeConceptObservation.getObsvalueString();
        }

        // setup navigation

        position = getView().getCurrentQuestionPosition();
        lastPosition = getView().getTotalQuestions() - 1;

        if (position == lastPosition) {
            finish();
            return;
        }

        // check jump skips;

        if (activeConcept.hasJumpSkip()) {

            if (activeConcept.hasJumpSkipByParent()) {

                // get parent obs
                parentConceptObservation = findObservation(Integer.parseInt(activeConcept.getSkiptoparent()));
                if (parentConceptObservation != null) {
                    parentConceptObsValue = parentConceptObservation.getObsvalueString();

                    if (parentConceptObsValue.length() > 0 && activeConceptObsValue.length() > 0) {

                        // get parent condition for skip
                        String condition = "";
                        if (activeConcept.hasSkiptoParentCondition()) {
                            condition = activeConcept.getSkiptoparentcondition();
                        }

                        //check if parent condition is met
                        if (parentConceptObsValue.contains(condition)) {

                            //check skip to condrion

                            if (!activeConcept.hasSkiptoCondition()) {
                                nextConcept = findConcept(Integer.parseInt(activeConcept.getSkipto()));
                            } else {
                                if (activeConceptObsValue.length() > 0) {
                                    if (activeConcept.getSkiptocondition().contains("!")) {
                                        if (!activeConceptObsValue.contains(activeConcept.getSkiptocondition().replace("!", ""))) {
                                            nextConcept = findConcept(Integer.parseInt(activeConcept.getSkipto()));
                                        }
                                    } else {
                                        if (activeConceptObsValue.contains(activeConcept.getSkiptocondition())) {
                                            nextConcept = findConcept(Integer.parseInt(activeConcept.getSkipto()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } else {


                if (!activeConcept.hasSkiptoCondition()) {
                    nextConcept = findConcept(Integer.parseInt(activeConcept.getSkipto()));
                } else {
                    if (activeConceptObsValue.length() > 0) {
                        if (activeConcept.getSkiptocondition().contains("!")) {
                            if (!activeConceptObsValue.contains(activeConcept.getSkiptocondition().replace("!", ""))) {
                                nextConcept = findConcept(Integer.parseInt(activeConcept.getSkipto()));
                            }
                        } else {
                            if (activeConceptObsValue.contains(activeConcept.getSkiptocondition())) {
                                nextConcept = findConcept(Integer.parseInt(activeConcept.getSkipto()));
                            }
                        }
                    }
                }
            }
            if (nextConcept != null) {
                if (validateNextConceptGender(nextConcept)) {
                    getView().setActiveConcept(nextConcept);
                    return;
                }
            }
        }


        while (canMoveNext == false) {

            //position = getView().getCurrentQuestionPosition();
            //check if last page

            if (position == lastPosition) {
                finish();
                return;
            }

            //move to next position
            nextPosition = position + 1;

            parentConceptObservation = null;
            parentConceptObsValue = "";

            //set nextConcept

            nextConcept = findConceptByPostion(nextPosition);

            if (getView().getPatient().isInfant()) {

                if (nextConcept.isChildWithSingleParent(true)) {
                    //get Parent
                    parentConceptObservation = findObservationDetail(nextConcept.getParent());
                    if (parentConceptObservation != null) {
                        parentConceptObsValue = parentConceptObservation.getObsvalueString();
                        if (parentConceptObsValue.length() > 0 && activeConceptObsValue.length() > 0) {

                            String condition = "";
                            if (nextConcept.getParentconditionchildren() != null) {
                                condition = nextConcept.getParentconditionchildren();
                            }
                            if (nextConcept.negateConditionChildren()) {
                                condition = nextConcept.getCleanParentconditionChildren();
                            }

                            if (condition.length() == 0 || nextConcept.anyConditionChildren()) {
                                canMoveNext = validateNextConceptGender(nextConcept);
                            } else {
                                if (nextConcept.negateConditionChildren()) {
                                    if (!parentConceptObsValue.contains(condition)) {
                                        canMoveNext = validateNextConceptGender(nextConcept);
                                    }
                                } else {
                                    if (parentConceptObsValue.contains(condition)) {
                                        canMoveNext = validateNextConceptGender(nextConcept);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    canMoveNext = validateNextConceptGender(nextConcept);
                }

            } else {


                if (nextConcept.isChildWithSingleParent(false)) {

                    //get Parent
                    parentConceptObservation = findObservationDetail(nextConcept.getParent());
                    if (parentConceptObservation != null) {
                        parentConceptObsValue = parentConceptObservation.getObsvalueString();
                        if (parentConceptObsValue.length() > 0 && activeConceptObsValue.length() > 0) {

                            String condition = "";
                            if (nextConcept.getParentcondition() != null) {
                                condition = nextConcept.getParentcondition();
                            }
                            if (nextConcept.negateCondition()) {
                                condition = nextConcept.getCleanParentcondition();
                            }

                            if (condition.length() == 0 || nextConcept.anyCondition()) {
                                canMoveNext = validateNextConceptGender(nextConcept);
                            } else {
                                if (nextConcept.negateCondition()) {
                                    if (!parentConceptObsValue.contains(condition)) {
                                        canMoveNext = validateNextConceptGender(nextConcept);
                                    }
                                } else {
                                    if (parentConceptObsValue.contains(condition)) {
                                        canMoveNext = validateNextConceptGender(nextConcept);
                                    }
                                }
                            }
                        }
                    }

                } else {
                    canMoveNext = validateNextConceptGender(nextConcept);
                }
            }

            position++;
            SLF_LOGGER.debug("NAVIGATIONS [ Postion moved:" + position + " next position:" + (nextPosition + 1) + "]");
        }

        //nextConcept = findConceptByPostion(nextPosition);
        getView().setActiveConcept(nextConcept);
    }


    private boolean validateNextConceptGender(MConcept nextConcept) {

        if (nextConcept.isMaleOnly()) {
            return getView().getPatient().isMale();
        }
        if (nextConcept.isFremaleOnly()) {
            return getView().getPatient().isFemaleMale();
        }

        return true;
    }

    private boolean validateNextConceptChildren(MConcept nextConcept) {

        if (nextConcept.isMaleOnly()) {
            return getView().getPatient().isMale();
        }
        if (nextConcept.isFremaleOnly()) {
            return getView().getPatient().isFemaleMale();
        }

        return true;
    }


    @Override
    public void begin() {

    }

    @Override
    public void finish() {
        Encounter encounter = getView().getEncounter();

        try {
            encounterService.markAsComplete(encounter, true);
            getView().moveFinal();

        } catch (SQLException e) {
            getView().setViewErrors(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Observation saveObservation(Observation observation) {
        if (user != null) {
            //observation.setUserid(user.getId()); //Set IQCare ID instead
            observation.setUserid(user.getIqcareid());
        }
        Observation obs = null;
        try {
            obs = observationService.save(observation);
            saveActiveSessionConcept(obs);
            getView().setEncounter(obs.getEncounter());
            SLF_LOGGER.debug("saved:" + obs.toString());
        } catch (SQLException e) {
            getView().setViewErrors(e.getMessage());
            e.printStackTrace();
        }
        return obs;
    }

    private void saveActiveSessionConcept(Observation observation) {
        if (observation != null) {
            MConcept concept = observation.getmConcept();
            ActiveSession session = getView().getActiveSessions().get(0);
            ActiveSession newConceptSession = new ActiveSession(session.getActiveActivity());
            newConceptSession.setActivePatientId(session.getActivePatientId());
            newConceptSession.createSession("conceptId", String.valueOf(concept.getId()));
            applicationService.saveActiveSession(newConceptSession);
        }
    }

    private Observation findObservation(int conceptId) {

        Encounter currentEncounter = getView().getEncounter();

        for (Observation o : currentEncounter.getObservationsList()) {
            if (o.getmConcept().getId() == conceptId) {
                return o;
            }
        }
        return null;
    }

    private Observation findObservation(MConcept concept) {

        Encounter currentEncounter = getView().getEncounter();

        for (Observation o : currentEncounter.getObservationsList()) {
            if (o.getmConcept().getId() == concept.getId()) {
                return o;
            }
        }
        return null;
    }

    private Observation findObservationDetail(MConcept concept) {
        Observation obsDetail = null;
        Observation obs = findObservation(concept);

        if (obs != null) {
            try {
                obsDetail = observationService.getById(obs.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return obsDetail;
    }

    private void beforeTask() {
        getView().isBusy(true);
        getView().showPrgressDialog(true, "Loading...");
    }

    private void afterTask() {
        getView().isBusy(false);
        getView().showPrgressDialog(false, null);
    }

}
