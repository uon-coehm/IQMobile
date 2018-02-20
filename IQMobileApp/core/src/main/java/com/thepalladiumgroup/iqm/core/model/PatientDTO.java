package com.thepalladiumgroup.iqm.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/05/24]
 */
public class PatientDTO {
    private Patient patient = new Patient();
    private List<Encounter> encounters = new ArrayList<>();
    private List<Observation> observations = new ArrayList<>();

    public static PatientDTO create(Patient patient) {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setObservations(generateObservationDTO(patient));
        patientDTO.setEncounters(generateEncounterDTO(patient));
        patientDTO.setPatient(generatePatientDTO(patient));

        return patientDTO;
    }

    private static Patient generatePatientDTO(Patient patient) {

        patient.setEncounters(null);
        if (patient.getPartner() != null) {
//            PatientDTO partnerDTO=new PatientDTO();
//            List<Encounter> encounterslist=new ArrayList<>();
//            Patient partner=patient.getPartner();
//
//            partnerDTO.setObservations(generateObservationDTO(partner));
//            partnerDTO.setEncounters(generateEncounterDTO(partner));
//            partnerDTO.setPatient(generatePatientDTO(partner));
//
//            partner=partnerDTO.getPatient();
//            encounterslist=partnerDTO.getEncounters();
//            for (Encounter e:encounterslist) {
//
//                e.addObservation();
//            }
//
//            patient.getPartner().setEncounters(null);
        }
        return patient;
    }

    private static List<Encounter> generateEncounterDTO(Patient patient) {
        List<Encounter> encounterList = new ArrayList<>();
        if (patient.getEncounters() == null) {
            return encounterList;
        }
        for (Encounter e : patient.getEncounters()) {
            e.setPatient(null);
            e.setPatientPk(patient.getId());
            e.setEncounterTypePk(e.getEncounterType().getId());
            e.setEncounterType(null);
            e.setObservations(null);
            encounterList.add(e);
        }
        return encounterList;
    }

    private static List<Observation> generateObservationDTO(Patient patient) {

        List<Observation> observationList = new ArrayList<>();
        if (patient.getEncounters() == null) {
            return observationList;
        }
        for (Encounter e : patient.getEncounters()) {

            for (Observation o : e.getObservations()) {
                o.setEncounterPk(o.getEncounter().getId());
                o.setEncounter(null);
                o.setmConceptPk(o.getmConcept().getId());
                o.setmConcept(null);
                observationList.add(o);
            }
        }

        return observationList;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Encounter> getEncounters() {
        return encounters;
    }

    public void setEncounters(List<Encounter> encounters) {
        this.encounters = encounters;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }
}
