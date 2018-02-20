package com.thepalladiumgroup.iqm.core.model;

import org.joda.time.LocalDate;

/**
 * Created by Koske Kimutai [2016/05/06]
 */
public class PatientAgeDetail {
    private AgeUnit ageUnit;
    private int age;
    private LocalDate birthDate;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public AgeUnit getAgeUnit() {
        return ageUnit;
    }

    public void setAgeUnit(AgeUnit ageUnit) {
        this.ageUnit = ageUnit;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


}
