package com.thepalladiumgroup.iqm.presentation.presenter;

import com.thepalladiumgroup.iqm.core.model.ActiveSession;
import com.thepalladiumgroup.iqm.core.model.Observation;
import com.thepalladiumgroup.iqm.presentation.view.IObservationView;

/**
 * Created by Koske Kimutai [2016/04/21]
 */
public interface IObservationPresenter {
    IObservationView getView();



    void loadPatient(int patientid);

    void loadPatient(int patientid, int encounterTypeId);

    void loadEncounter();

    void loadConcepts();

    void loadActiveSession(ActiveSession session, boolean inReview);

    void setActiveConcept();


    void saveActiveSession(ActiveSession session);




    void movePrevious();

    void moveNext();

    void begin();
    void finish();

    Observation saveObservation(Observation observation);

}
