package com.thepalladiumgroup.iqm.core.model.tests;

import com.thepalladiumgroup.iqm.core.model.Patient;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Koske Kimutai [2016/05/05]
 */
public class PatientTests {
    private Patient patient;
    private DateTimeFormatter dmy = DateTimeFormat.forPattern("dd MMM yyyy");

    @Before
    public void setUp() {
        LocalDate leo = new LocalDate();
        LocalDate dob = leo.minusYears(30);
        patient = new Patient("John", "M", "Doe", 16, dob.toDate());
    }

    @Test
    public void should_calculate_age_in_detail() {
        LocalDate newDob = new LocalDate();
        newDob = newDob.minusYears(1);
        newDob = newDob.minusMonths(1);
        newDob = newDob.minusDays(1);

        patient.setDob(newDob.toDate());
        int ageYr = patient.getAgeInDetail().getYears();
        int ageMnth = patient.getAgeInDetail().getMonths();
        int ageDay = patient.getAgeInDetail().getDays();

        Assert.assertEquals(1, ageYr);
        Assert.assertEquals(1, ageMnth);
        Assert.assertEquals(1, ageDay);

        System.out.println(patient.showDetails());
        System.out.println("Age Details: " + patient.getAgeInDetailString());
    }

    @Test
    public void should_calculate_dob_with_age_in_years() {
        LocalDate leoDate = new LocalDate();
        int age = 10;

        Date dt = patient.calculateDobFromAgeInYrs(age);
        LocalDate dob = new LocalDate(dt);

        Assert.assertEquals(leoDate.getYear() - age, dob.getYear());
        Assert.assertEquals(leoDate.getMonthOfYear(), dob.getMonthOfYear());
        Assert.assertEquals(leoDate.getDayOfMonth(), dob.getDayOfMonth());

        System.out.println(patient.showDetails());
        System.out.println("Age Details: " + patient.getAgeInDetailString());
    }
    @Test
    public void should_calculate_estimated_dob_with_age_in_years() {
        LocalDate leoDate = new LocalDate();
        int age = 10;

        Date dt = patient.calculateEstimatedDobFromAgeInYrs(age);
        LocalDate dob = new LocalDate(dt);

        Assert.assertEquals(leoDate.getYear() - age, dob.getYear());
        Assert.assertEquals(6, dob.getMonthOfYear());
        Assert.assertEquals(15, dob.getDayOfMonth());

        System.out.println(patient.showDetails());
        System.out.println("Age Details: " + patient.getAgeInDetailString());
    }

    @Test
    public void should_calculate_dob_with_age_in_months() {
        LocalDate leoDate = new LocalDate();
        int age = 10;

        Date dt = patient.calculateDobFromAgeInMnths(age);
        LocalDate dob = new LocalDate(dt);

        Assert.assertEquals(leoDate.minusMonths(age).getYear(), dob.getYear());
        Assert.assertEquals(leoDate.minusMonths(age).getMonthOfYear(), dob.getMonthOfYear());
        Assert.assertEquals(leoDate.minusMonths(age).getDayOfMonth(), dob.getDayOfMonth());

        System.out.println(patient.showDetails());
        System.out.println("Age Details: " + patient.getAgeInDetailString());
    }

    @Test
    public void should_calculate_dob_with_age_in_days() {
        LocalDate leoDate = new LocalDate();
        int age = 2;

        Date dt = patient.calculateDobFromAgeInDays(age);
        LocalDate dob = new LocalDate(dt);

        Assert.assertEquals(leoDate.minusDays(age).getYear(), dob.getYear());
        Assert.assertEquals(leoDate.minusDays(age).getMonthOfYear(), dob.getMonthOfYear());
        Assert.assertEquals(leoDate.minusDays(age).getDayOfMonth(), dob.getDayOfMonth());

        System.out.println(patient.showDetails());
        System.out.println("Age Details: " + patient.getAgeInDetailString());
    }


}
