package com.thepalladiumgroup.iqm.core.data.impl;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IEncounterRepository;
import com.thepalladiumgroup.iqm.core.data.IObservationRepository;
import com.thepalladiumgroup.iqm.core.data.IPatientRepository;
import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.Observation;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.PatientStats;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/16]
 */
public class PatientRepository extends BaseRepository<Patient> implements IPatientRepository {


    private IEncounterRepository encounterRepository;
    private IObservationRepository observationRepository;

    public PatientRepository(IApplicationManager applicationManager) {
        super(applicationManager);
        encounterRepository = new EncounterRepository(applicationManager);
        observationRepository = new ObsevationRepository(applicationManager);
    }

    @Override
    public Patient findByCode(String code) throws SQLException {
        List<Patient> results = entityDao.queryForEq("clientcode", code);
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public Patient findWithEncounters(int id) throws SQLException {
        Patient patient = find(id);
        if (patient != null) {
            if (patient.getEncountersList().size() == 0) {
                List<Encounter> encounters = encounterRepository.getAllbyfield("patient", id);
                if (encounters != null) {
                    for (Encounter e : encounters) {

                        List<Observation> observations = observationRepository.getAllbyfield("encounter", e.getId());
                    }
                }
            }
        }
        return patient;
    }


    @Override
    public List<Patient> getAll(String search) throws SQLException {
        List<Patient> results = entityDao.queryForEq("clientcode", search);
        return results;
    }

    @Override
    public List<Patient> getAllDemographics() throws SQLException {
        return getAllDemographicsById(-1);
    }

    @Override
    public Patient getDemographicsById(int id) throws SQLException {
        SLF_LOGGER.debug(">>FIND PATIENT with iterator >>");

        List<Patient> patients = new ArrayList<>();


        List<String> cols = new ArrayList<>();

        cols.add("contactphone");
        cols.add("dob");
        cols.add("enrollmentdate");
        cols.add("enrollmenttime");
        cols.add("firstname");
        cols.add("kin");
        cols.add("kinphone");
        cols.add("kinrelationother");
        cols.add("lastname");
        cols.add("middlename");
        cols.add("clientcode");
        cols.add("kinrelation");
        cols.add("partner_id");
        cols.add("sex");
        cols.add("uuid");
        cols.add("id");
        cols.add("iqcareid");
        cols.add("userid");
//        cols.add("estimateddob");
        cols.add("idtype");

        QueryBuilder<Patient, Integer> qb = entityDao.queryBuilder()
                .selectColumns(cols);

        qb.where().eq("id", id);


        CloseableIterator<Patient> iterator = entityDao.iterator(qb.prepare());
        try {
            while (iterator.hasNext()) {
                Patient account = iterator.next();
                patients.add(account);
            }
        } finally {
            // close it at the end to close underlying SQL statement
            iterator.close();
        }

        if (patients.size() > 0) {
            Patient p = patients.get(0);
            return p;
        }
        return null;
    }

    @Override
    public Patient getPatientPartnerInfo(int id) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        patients = getAllDemographicsById(id);

        for (Patient p : patients) {
            setPartnerInfo(p);
        }

        if (patients.size() > 0) {
            Patient p = patients.get(0);
            return p;
        }
        return null;

    }

    @Override
    public Patient getPatientPartnerInfo(int id, int encounterTypeId) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        patients = getAllDemographicsById(id);

        for (Patient p : patients) {
            setPartnerInfo(p, encounterTypeId);
        }

        if (patients.size() > 0) {
            Patient p = patients.get(0);
            return p;
        }
        return null;
    }

    @Override
    public Patient getToSend(int id) throws SQLException {

        SLF_LOGGER.debug(">>FIND PATIENT TO SEND with iterator >>");
        List<Patient> patients = new ArrayList<>();
        QueryBuilder<Patient, Integer> qb = entityDao.queryBuilder();
        qb.where().eq("id", id);
        CloseableIterator<Patient> iterator = entityDao.iterator(qb.prepare());
        try {
            while (iterator.hasNext()) {
                Patient account = iterator.next();
                patients.add(account);
            }
        } finally {
            // close it at the end to close underlying SQL statement
            iterator.close();
        }

        if (patients.size() > 0) {
            return patients.get(0);
        }
        return null;
    }

    @Override
    public List<Patient> getAllDemographicsById(int id) throws SQLException {
        SLF_LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> with iterator >>>>>>>>>>>>>>>>>>>");

        List<Patient> patients = new ArrayList<>();


        List<String> cols = new ArrayList<>();
        cols.add("id");
        cols.add("firstname");
        cols.add("middlename");
        cols.add("lastname");
        cols.add("sex");
        cols.add("dob");
        cols.add("enrollmentdate");
        cols.add("clientcode");

        QueryBuilder<Patient, Integer> qb = entityDao.queryBuilder()
                .selectColumns(cols);

        if (id > -1) {
            qb.where().eq("id", id);
        }

        CloseableIterator<Patient> iterator = entityDao.iterator(qb.prepare());
        try {
            while (iterator.hasNext()) {
                Patient account = iterator.next();
                patients.add(account);
            }
        } finally {
            // close it at the end to close underlying SQL statement
            iterator.close();
        }


        return patients;
    }


    private void setPartnerInfo(Patient patient) throws SQLException {
        int patientId = patient.getId();
        int partnerId = -1;
        GenericRawResults<String[]> partnerCountQuery = entityDao.queryRaw("SELECT count(partner_id) FROM patient WHERE partner_id IS NOT NULL AND patient.id =" + patientId);
        List<String[]> resultsCount = partnerCountQuery.getResults();
        String[] partnerResults = resultsCount.get(0);
        int partnerCount = Integer.parseInt(partnerResults[0]);

        if (partnerCount > 0) {
            GenericRawResults<String[]> partnerIdQuery = entityDao.queryRaw("SELECT partner_id FROM patient WHERE partner_id IS NOT NULL AND patient.id =" + patientId);
            List<String[]> results = partnerIdQuery.getResults();
            String[] resultArray = results.get(0);
            partnerId = Integer.parseInt(resultArray[0]);
        }

        if (partnerId > -1) {
            patient.setPartner(getDemographicsById(partnerId));

        }
    }

    private void setPartnerInfo(Patient patient, int etypeId) throws SQLException {
        int patientId = patient.getId();
        int partnerId = -1;
        GenericRawResults<String[]> partnerCountQuery = entityDao.queryRaw("SELECT count(partner_id) FROM patient WHERE partner_id IS NOT NULL AND patient.id =" + patientId);
        List<String[]> resultsCount = partnerCountQuery.getResults();
        String[] partnerResults = resultsCount.get(0);
        int partnerCount = Integer.parseInt(partnerResults[0]);

        if (partnerCount > 0) {
            GenericRawResults<String[]> partnerIdQuery = entityDao.queryRaw("SELECT partner_id FROM patient WHERE partner_id IS NOT NULL AND patient.id =" + patientId);
            List<String[]> results = partnerIdQuery.getResults();
            String[] resultArray = results.get(0);
            partnerId = Integer.parseInt(resultArray[0]);
        }

        if (partnerId > -1) {
            Patient partner = getDemographicsById(partnerId);
            setEncounters(partner, etypeId);
            patient.setPartner(partner);
        }
    }

    private void setEncounters(Patient patner, int encounterTypeId) throws SQLException {
        Encounter e = encounterRepository.getPartnerEncounter(patner.getId(), encounterTypeId);
        List<Encounter> partnerEncouners = new ArrayList<>();
        partnerEncouners.add(e);
        patner.addEncounters(partnerEncouners);
    }

    @Override
    public List<Integer> getAllPatientIdToSend() throws SQLException {
        List<Integer> patientIds = new ArrayList<>();
        GenericRawResults<String[]> rawResults = entityDao.queryRaw("SELECT patient_id FROM encounter WHERE completed = 1");
        for (String[] resultArray : rawResults) {
            patientIds.add(Integer.valueOf(resultArray[0]));
        }
        rawResults.close();
        return patientIds;
    }

    @Override
    public PatientStats getStats() throws SQLException {
        GenericRawResults<String[]> rawResults = entityDao.queryRaw("select count(id) from patient");
        List<String[]> results = rawResults.getResults();
        String[] resultArray = results.get(0);

        int patientCount = Integer.parseInt(resultArray[0]);

        GenericRawResults<String[]> rawResultsMale = entityDao.queryRaw("select count(id) from patient where sex=16");
        List<String[]> resultsMale = rawResultsMale.getResults();
        String[] resultArrayMale = resultsMale.get(0);

        int patientCountMale = Integer.parseInt(resultArrayMale[0]);

        GenericRawResults<String[]> rawResultsFemale = entityDao.queryRaw("select count(id) from patient where sex=17");
        List<String[]> resultsFemale = rawResultsFemale.getResults();
        String[] resultArrayFemale = resultsFemale.get(0);

        int patientCountFemale = Integer.parseInt(resultArrayFemale[0]);

        PatientStats patientStats = new PatientStats(patientCountMale, patientCountFemale, patientCount);

        return patientStats;
    }

    @Override
    public void deleteRecords(int patientId) throws SQLException {

        entityDao.executeRaw("" +
                "DELETE FROM observation WHERE encounter IN " +
                "   (SELECT id FROM encounter WHERE patient_id IN " +
                "       (SELECT id FROM patient WHERE id IN (" + patientId + "))" +
                "   )");
        entityDao.executeRaw("" +
                "DELETE FROM encounter WHERE patient_id IN " +
                "   (SELECT id FROM patient WHERE id IN (" + patientId + "))");

        entityDao.executeRaw("DELETE FROM patient WHERE id IN (" + patientId + ")");
    }

    @Override
    public void cleanRecords() throws SQLException {
        entityDao.executeRaw("DELETE FROM observation WHERE encounter NOT IN (SELECT id FROM encounter)");
        entityDao.executeRaw("DELETE FROM encounter WHERE patient_id  NOT IN (SELECT id FROM patient)");
    }

    @Override
    public void delete(Patient entity) throws SQLException {
        if (entity.getEncounters() != null) {
            for (Encounter e : entity.getEncounters()) {
                for (Observation o : e.getObservations()) {
                    observationRepository.delete(o.getId());
                }
                encounterRepository.delete(e.getId());
            }
        }
        super.delete(entity);
    }

    @Override
    public void delete(int id) throws SQLException {
        Patient p = entityDao.queryForId(id);
        super.delete(p);
    }
}

