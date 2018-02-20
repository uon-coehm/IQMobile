package com.thepalladiumgroup.iqm.core.services.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IPatientRepository;
import com.thepalladiumgroup.iqm.core.data.impl.PatientRepository;
import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.PatientStats;
import com.thepalladiumgroup.iqm.core.services.IPatientService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/16]
 */
public class PatientService implements IPatientService {

    private final IApplicationManager applicationManager;
    private final IPatientRepository patientRepository;

    public PatientService(IApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
        this.patientRepository = new PatientRepository(applicationManager);
    }

    @Override
    public List<Patient> getAll() throws SQLException {
        return patientRepository.getAll();
    }

    @Override
    public List<Patient> getAllDemographics() throws SQLException {
        return patientRepository.getAllDemographics();
    }

    @Override
    public List<Integer> getAllCompletePateintIds() throws SQLException {
        return patientRepository.getAllPatientIdToSend();
    }

    @Override
    public List<Patient> getAllComplete(boolean completeonly) throws SQLException {
        if (!completeonly) {
            return getAll();
        }

        List<Patient> patients = new ArrayList<>();
        List<Patient> all = getAll();
        if (all != null) {
            for (Patient p : getAll()) {
                List<Encounter> encounters = p.getEncountersList();
                if (encounters != null) {
                    if (encounters.get(0) != null) {
                        if (encounters.get(0).isCompleted()) {
                            patients.add(p);
                        }
                    }
                }
            }
        }

        return patients;
    }

    @Override
    public List<Patient> find(String search) throws SQLException {
        return patientRepository.getAll(search);
    }

    @Override
    public Patient find(int id) throws SQLException {
        return patientRepository.find(id);
    }

    @Override
    public Patient findForEdit(int id) throws SQLException {
        return patientRepository.getDemographicsById(id);
    }

    @Override
    public Patient findDemographics(int id) throws SQLException {
        List<Patient> patients = patientRepository.getAllDemographicsById(id);
        if (patients.size() > 0) {
            return patients.get(0);
        }
        return null;
    }

    @Override
    public Patient findPatientPartnerInfo(int id) throws SQLException {
        return patientRepository.getPatientPartnerInfo(id);
    }

    @Override
    public Patient findPatientPartnerInfo(int id, int encounterTypeId) throws SQLException {
        return patientRepository.getPatientPartnerInfo(id, encounterTypeId);
    }

    @Override
    public Patient getToSend(int id) throws SQLException {
        return patientRepository.getToSend(id);
    }

    @Override
    public Patient findByCode(String cilentcode) throws SQLException {
        return patientRepository.findByCode(cilentcode);
    }

    @Override
    public PatientStats getStats() throws SQLException {
        return patientRepository.getStats();
    }

    @Override
    public boolean Save(Patient patient) throws SQLException {
        Patient saved = null;
        saved = patientRepository.save(patient);
        updatePartnerRelations(saved);
        return saved != null;
    }

    @Override
    public boolean update(Patient patient) throws SQLException {
        Patient saved = null;
        saved = patientRepository.update(patient);
        updatePartnerRelations(saved);
        return saved != null;
    }

    @Override
    public void updatePartnerRelations(Patient patient) throws SQLException {
        if (patient.isCouple()) {
            Patient partner = patient.getPartner();
            if (!partner.isCouple()) {
                partner.setPartner(patient);
                patientRepository.update(partner);
            }
        }
    }

    @Override
    public void Delete(int id) throws SQLException {
        patientRepository.delete(id);
    }

    @Override
    public void deletePatientRecords(int id) throws SQLException {
        patientRepository.deleteRecords(id);
    }

    @Override
    public void cleanUpPatients() throws SQLException {
        patientRepository.cleanRecords();
    }
}
