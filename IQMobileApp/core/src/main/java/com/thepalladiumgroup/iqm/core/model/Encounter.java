package com.thepalladiumgroup.iqm.core.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Stack;

/**
 * Created by Koske Kimutai [2016/04/22]
 */
public class Encounter extends Entity {

    @ForeignCollectionField(eager = true)
    transient Collection<Observation> observations = new ArrayList<>();
    @DatabaseField
    private Date startdate;
    @DatabaseField
    private Date starttime;
    @DatabaseField
    private String geoloctaion;
    //@DatabaseField(foreignAutoRefresh = true, foreign = true, foreignAutoCreate = false,maxForeignAutoRefreshLevel = 2)
    @DatabaseField(foreignAutoRefresh = true, foreign = true, foreignAutoCreate = false,
            columnDefinition = "Integer REFERENCES patient(id) ON DELETE CASCADE ON UPDATE CASCADE")
    private transient Patient patient;

    private int patientPk;
    @DatabaseField(foreignAutoRefresh = true, foreign = true, foreignAutoCreate = false,
            columnDefinition = "Integer REFERENCES encountertype(id)")
    private EncounterType encounterType;
    private int encounterTypePk;
    @DatabaseField
    private boolean completed;
    private transient Stack<Observation> observationStack;

    public Encounter() {

    }

    public Encounter(EncounterType encounterType, Patient patient) {
        this.startdate = this.starttime = new Date();
        this.encounterType = encounterType;
        this.patient = patient;

    }

    public Encounter(String geoloctaion, Date startdate, Date starttime) {
        this.geoloctaion = geoloctaion;
        this.startdate = startdate;
        this.starttime = starttime;

    }

    public String getGeoloctaion() {
        return geoloctaion;
    }

    public void setGeoloctaion(String geoloctaion) {
        this.geoloctaion = geoloctaion;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public EncounterType getEncounterType() {
        return encounterType;
    }

    public void setEncounterType(EncounterType encounterType) {
        this.encounterType = encounterType;
    }

    public Collection<Observation> getObservations() {
        return observations;
    }

    public void setObservations(Collection<Observation> observations) {
        this.observations = observations;
    }

    public List<Observation> getObservationsList() {
        return new ArrayList<>(observations);
    }

    public Stack<Observation> getObservationStack() {
        return observationStack;
    }

    public void setObservationStack(Stack<Observation> observationStack) {
        this.observationStack = observationStack;
    }

    public Stack<Observation> getAcitveObservationStack() {
        observationStack = new Stack<>();
        List<Observation> obs = getObservationsList();
        Collections.sort(obs);
        for (Observation observation : obs) {
            observationStack.push(observation);
        }

        return observationStack;

    }

    public Observation getPrevious(MConcept concept) {
        Observation found = null;
        int position = -1;
        List<Observation> obs = getObservationsList();
        Collections.sort(obs);

        int current = 0;
        for (Observation observation : obs) {
            if (observation.getmConcept().getId() == concept.getId()) {
                found = observation;
                position = current;
                break;
            }
            current++;
        }

        if (position > -1) {

            try {
                found = obs.get(position - 1);
            } catch (Exception ex) {
            }
        }

        if (found == null) {
            try {
                position = obs.size() - 1;
                found = obs.get(position);
            } catch (Exception ex) {
            }
        }
        return found;
    }

    public List<Observation> getDirtyObs(Observation dirtyObs, Encounter encounter) {
        List<Observation> dirtyObsList = new ArrayList<>();
        List<Observation> obs = encounter.getObservationsList();
        Collections.sort(obs);

        int startposition = obs.indexOf(dirtyObs);
        int total = obs.size();
        int current = startposition + 1;
        int lastposition = total - 1;

        while (current <= lastposition) {
            dirtyObsList.add(obs.get(current));
            current++;
        }

        return dirtyObsList;
    }

    public Observation getNext(MConcept concept) {
        Observation found = null;
        int position = -1;
        List<Observation> obs = getObservationsList();
        Collections.sort(obs);

        int current = 0;
        for (Observation observation : obs) {
            if (observation.getmConcept().getId() == concept.getId()) {
                found = observation;
                position = current;
                break;
            }
            current++;
        }

        if (position > -1) {

            try {
                found = obs.get(position + 1);
            } catch (Exception ex) {
            }
        }
        return found;
    }

    public void addObservation(Observation observation) {
        observation.setEncounter(this);
        observations.add(observation);
    }


    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getPatientPk() {
        return patientPk;
    }

    public void setPatientPk(int patientPk) {
        this.patientPk = patientPk;
    }

    public int getEncounterTypePk() {
        return encounterTypePk;
    }

    public void setEncounterTypePk(int encounterTypePk) {
        this.encounterTypePk = encounterTypePk;
    }

    @Override
    public String toString() {
        return getId() + "-" + encounterType.getDisplay() + " (" + startdate + ") - Patient[" + getPatient().getId() + "," + getPatient().getLastname() + "]";
    }

    public String entryDetail() {
        return getId() + "- Started:(" + startdate + ")";
    }
}
