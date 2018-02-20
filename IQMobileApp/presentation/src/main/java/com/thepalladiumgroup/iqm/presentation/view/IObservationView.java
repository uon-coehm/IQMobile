package com.thepalladiumgroup.iqm.presentation.view;

import com.thepalladiumgroup.iqm.core.model.ActiveSession;
import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.Observation;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.presentation.presenter.IObservationPresenter;

import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/21]
 */
public interface IObservationView {

    void setPresenter(IObservationPresenter presenter);

    int getPatientId();

    Patient getPatient();
    void setPatient(Patient patient);

    int getEncounterId();

    void setEncounterId(int encounterId);

    Encounter getEncounter();

    void setEncounter(Encounter encounter);

    int getEncounterTypeId();

    void setEncounterTypeId(int encounterTypeId);

    EncounterType getEncounterType();

    void setEncounterType(EncounterType encounterType);

    int getConceptId();

    void setConceptId(int conceptId);

    MConcept getActiveConcept();

    void setActiveConcept(MConcept concept);

    List<MConcept> getConcepts();
    void setConcepts(List<MConcept> concepts);

    List<ActiveSession> getActiveSessions();

    void setActiveSession(List<ActiveSession> activeSessions);

    Observation getActiveObservation();
    void setActiveObservation(Observation observation);

    int getCurrentQuestionPosition();
    int getCurrentQuestionNumber();
    int getTotalQuestions();

    void initialize();
    void loadWidget();
    List<String> getWidgetRawData();
    void setWidgetRawData(List<String> rawdata);
    List<Observation> getObservations();
    void setObservations(List<Observation> observations);
    boolean canMovePrevious();
    boolean canMoveNext();
    void moveFinal();
    boolean inEditMode();
    void setViewErrors(String s);

    void showProgress();

    void isBusy(boolean state);

    void showPrgressDialog(boolean state, String message);
}
