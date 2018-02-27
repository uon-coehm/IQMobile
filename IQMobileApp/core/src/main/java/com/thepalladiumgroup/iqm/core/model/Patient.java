package com.thepalladiumgroup.iqm.core.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/12]
 */
public class Patient extends Entity {


    private transient final DateTimeFormatter DMY = DateTimeFormat.forPattern("dd MMM yyyy");

    @ForeignCollectionField(eager = true)
    transient Collection<Encounter> encounters = new ArrayList<>();
    @DatabaseField
    private String firstname;
    @DatabaseField
    private String middlename;
    @DatabaseField
    private String lastname;
    @DatabaseField
    private int sex;
    @DatabaseField
    private Date dob;
    @DatabaseField(uniqueCombo = true, uniqueIndexName = "unique_id_type_and_patient_id")
    private String clientcode;
    @DatabaseField
    private String contactphone;
    @DatabaseField
    private String kin;
    @DatabaseField
    private String kinphone;
    @DatabaseField
    private int kinrelation;
    @DatabaseField
    private String kinrelationother;
    @DatabaseField
    private Date enrollmentdate;
    @DatabaseField
    private Date enrollmenttime;
    @DatabaseField(uniqueCombo = true, uniqueIndexName = "unique_id_type_and_patient_id")
    private int idtype;
    @DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = "partner_id")
    private Patient partner;
    private boolean estimateddob;

    public String RegTime;
    public String StartTime;
    public String StopTime;

    private transient PatientAgeDetail patientAgeDetail;


    public Patient() {
        checkdobIsEstimated();
    }

    public Patient(String firstname, String middlename, String lastname, int sex, Date dob) {
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.sex = sex;
        this.dob = dob;
        checkdobIsEstimated();
    }

//    public Patient(String firstname, String middlename, String lastname, int sex, LocalDate dob) {
//        this.firstname = firstname;
//        this.middlename = middlename;
//        this.lastname = lastname;
//        this.sex = sex;
//        this.dob = dob.toDate();
//        checkdobIsEstimated();
//    }


    public Patient(String firstname, String middlename, String lastname, int sex, Date dob, int idtype, String clientcode, String contactphone, String kinphone, int kinrelation, String kinrelationother, Date enrollmentdate) {
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.sex = sex;
        this.dob = dob;
        this.idtype = idtype;
        this.clientcode = clientcode;

        this.contactphone = contactphone;
        this.kinphone = kinphone;
        this.kinrelation = kinrelation;
        this.kinrelationother = kinrelationother;
        this.enrollmentdate = enrollmentdate;
        this.enrollmenttime = enrollmentdate;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getClientcode() {
        return clientcode;
    }

    public void setClientcode(String clientcode) {
        this.clientcode = clientcode;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public String getKin() {
        return kin;
    }

    public void setKin(String kin) {
        this.kin = kin;
    }

    public String getKinphone() {
        return kinphone;
    }

    public void setKinphone(String kinphone) {
        this.kinphone = kinphone;
    }

    public int getKinrelation() {
        return kinrelation;
    }

    public void setKinrelation(int kinrelation) {
        this.kinrelation = kinrelation;
    }

    public String getKinrelationother() {
        return kinrelationother;
    }

    public void setKinrelationother(String kinrelationother) {
        this.kinrelationother = kinrelationother;
    }

    public Date getEnrollmentdate() {
        return enrollmentdate;
    }

    public void setEnrollmentdate(Date enrollmentdate) {
        this.enrollmentdate = enrollmentdate;
        this.enrollmenttime = new Date();
    }


    public LocalDate getEnrollmentdateLocal() {
        return new LocalDate(this.enrollmentdate);
    }

    public Date getEnrollmenttime() {
        return enrollmenttime;
    }

    public void setEnrollmenttime(Date enrollmenttime) {
        this.enrollmenttime = enrollmenttime;
    }

    public int getIdtype() {
        return idtype;
    }

    public void setIdtype(int idtype) {
        this.idtype = idtype;
    }

    public Patient getPartner() {
        return partner;
    }

    public void setPartner(Patient partner) {
        this.partner = partner;
    }

    public boolean isCouple() {
        return partner != null;
    }

    public boolean isEstimateddob() {
        return estimateddob;
    }

    public void setEstimateddob(boolean estimateddob) {
        this.estimateddob = estimateddob;
    }

    public PatientAgeDetail getPatientAgeDetail() {
        return patientAgeDetail;
    }

    public void setPatientAgeDetail(PatientAgeDetail patientAgeDetail) {
        this.patientAgeDetail = patientAgeDetail;
    }

    public Collection<Encounter> getEncounters() {
        return encounters;
    }

    public void setEncounters(ForeignCollection<Encounter> encounters) {
        this.encounters = encounters;
    }

    public List<Encounter> getEncountersList() {
        return new ArrayList<>(encounters);
    }

    public String getFullName() {
        return firstname + " " + middlename + " " + lastname;
    }

    public String getGender() {
        if (sex == 16) {
            return "Male";
        }
        return "Female";
    }
    public String getGenderShort() {
        if (sex == 16) {
            return "M";
        }
        return "F";
    }

    public Period getAgeInDetail() {
        LocalDate localDob = new LocalDate(dob);
        LocalDate recordDate = getdateToday();
        Period period = new Period(localDob, recordDate, PeriodType.yearMonthDay());
        return period;
    }

    public String getAgeInDetailString() {
        Period period = getAgeInDetail();
        return period.getYears() + " yrs " + period.getMonths() + " months " + period.getDays() + " days";
    }
    public int getCurrentAge() {
        return getAgeInDetail().getYears();
    }

    public int getCurrentAgeInYears() {
        LocalDate birthdate = new LocalDate(this.dob);
        LocalDate now = getdateToday();
        Years age = Years.yearsBetween(birthdate, now);
        return age.getYears();
    }
    public int getCurrentAgeInMonths() {
        LocalDate birthdate = new LocalDate(this.dob);
        LocalDate now = getdateToday();
        Months age = Months.monthsBetween(birthdate, now);
        return age.getMonths();
    }

    public int getCurrentAgeInDays() {
        LocalDate birthdate = new LocalDate(this.dob);
        LocalDate now = getdateToday();
        Days age = Days.daysBetween(birthdate, now);
        return age.getDays();
    }
    public String getCurrentAgeString() {
        this.patientAgeDetail = new PatientAgeDetail();
        String ageString = "";
        Period period = getAgeInDetail();
        if (period.getYears() == 0) {
            if (period.getMonths() == 0) {
                ageString = period.getDays() + " days";

            } else {
                ageString = period.getMonths() + " months";
            }
        } else {
            if (isInfant()) {
                ageString = getCurrentAgeInMonths() + " months";
            } else {
                ageString = period.getYears() + " years";
            }
        }

        return ageString;
    }

    public String getDobString() {
        LocalDate localDob = new LocalDate(this.dob);
        return localDob.toString(DMY);
    }

    public LocalDate getDobLocal() {
        return new LocalDate(this.dob);
    }

    public Date calculateDobFromAgeInYrs(int age) {
        LocalDate leoDate = new LocalDate();
        if (age == 0) {
            this.dob = leoDate.toDate();
            return this.dob;
        }

        LocalDate birthDate;
        birthDate = leoDate.minusYears(age);
        this.dob = birthDate.toDate();
        return this.dob;
    }

    public Date calculateEstimatedDobFromAgeInYrs(int age) {
        this.estimateddob = true;
        LocalDate leoDate = getdateToday();

        if (age == 0) {
            this.dob = leoDate.toDate();
            return this.dob;
        }

        LocalDate birthDate;
        birthDate = leoDate.minusYears(age);
        this.dob = birthDate.toDate();
        return this.dob;
    }

    public Date calculateDobFromAgeInMnths(int age) {
        LocalDate leoDate = new LocalDate();
        if (age == 0) {
            this.dob = leoDate.toDate();
            return this.dob;
        }

        LocalDate birthDate;
        birthDate = leoDate.minusMonths(age);
        this.dob = birthDate.toDate();
        return this.dob;
    }

    public Date calculateDobFromAgeInDays(int age) {
        LocalDate leoDate = new LocalDate();
        if (age == 0) {
            this.dob = leoDate.toDate();
            return this.dob;
        }

        LocalDate birthDate;
        birthDate = leoDate.minusDays(age);
        this.dob = birthDate.toDate();
        return this.dob;
    }

    public boolean isMale() {
        return sex == 16;
    }
    public boolean isFemaleMale() {
        return sex == 17;
    }
    public boolean isInfant() {
        return getCurrentAgeInMonths() < 19;
    }

    public boolean isChild() {
        return !isInfant() && getCurrentAge() < 16;
    }

    public boolean hasEncounter() {
        if (encounters != null) {
            return encounters.size() > 0;
        }
        return false;
    }

    public void assignPartner(Patient partner) {
        partner.setPartner(this);
        this.partner = partner;

    }
    public void updateDemographics(Patient updatedPatient) {
        if (getId() == updatedPatient.getId()) {
            this.firstname = updatedPatient.firstname;
            this.middlename = updatedPatient.middlename;
            this.lastname = updatedPatient.lastname;
            this.sex = updatedPatient.sex;
            this.dob = updatedPatient.dob;
        }
    }

    public void updateContacts(Patient updatedPatient) {
        if (getId() == updatedPatient.getId()) {
            this.contactphone = updatedPatient.contactphone;
            this.kinphone = updatedPatient.kinphone;
            this.kinrelation = updatedPatient.kinrelation;
            this.kinrelationother = updatedPatient.kinrelationother;
        }
    }

    public void updateRegistration(Patient updatedPatient) {
        if (getId() == updatedPatient.getId()) {
            this.clientcode = updatedPatient.clientcode;
            this.enrollmentdate = updatedPatient.enrollmentdate;
        }
    }

    private LocalDate getdateToday() {

        LocalDate leoDate = new LocalDate();

        if (estimateddob) {
            leoDate = leoDate.withDayOfMonth(15);
            leoDate = leoDate.withMonthOfYear(6);
        }

        return leoDate;
    }


    public String showDetails() {

        return getFullName() + ", " + getGender() + ", " + getDobString() + " [ " + getCurrentAgeString() + " ]";
    }

    public void checkdobIsEstimated() {
        if (!estimateddob && dob != null) {
            estimateddob = (getDobLocal().getMonthOfYear() == 6) && (getDobLocal().getDayOfMonth() == 15);
        }
    }

    public void addEncounters(List<Encounter> encounterslist) {
        for (Encounter e : encounterslist) {
            encounters.add(e);
        }
    }

    @Override
    public String toString() {
        return firstname + " " + middlename + " " + lastname;
    }

    @Override
    public boolean equals(Object ob) {
        if (ob == null) {
            return false;
        }
        if (this == ob) {
            return true;
        }
        if (ob instanceof Patient) {
            Patient other = (Patient) ob;
            return this.getId() == other.getId();
        }
        return false;
    }

}
