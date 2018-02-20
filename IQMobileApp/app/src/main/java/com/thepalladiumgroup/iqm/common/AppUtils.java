package com.thepalladiumgroup.iqm.common;

import android.widget.DatePicker;
import android.widget.EditText;

import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Koske Kimutai [2016/04/15]
 */
public class AppUtils {

    private final LocalDate minDate;
    private final LocalDate maxDate;
    private DatePicker datePicker;

    public AppUtils() {
        this.minDate = new LocalDate(1900, 1, 1);
        this.maxDate = new LocalDate();
    }

    public AppUtils(LocalDate minDate) {
        this.minDate = minDate;
        this.maxDate = new LocalDate();
    }

    public AppUtils(LocalDate minDate, LocalDate maxDate) {
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    public LocalDate getMinDate() {
        return minDate;
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(DatePicker datePicker) {
        this.datePicker = datePicker;
    }

    public void setDateDefaults(DatePicker datePicker, final EditText editText) {
        this.datePicker = datePicker;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            this.datePicker.setMinDate(getMinDate().getMillis());
//            this.datePicker.setMaxDate(getMaxDate().getMillis());
//        } else {

        final int minYear = getMaxDate().getYear();
        final int minMonth = getMaxDate().getMonthOfYear() - 1;
        final int minDay = getMaxDate().getDayOfMonth();

        this.datePicker.init(minYear, minMonth, minDay,

                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar newDate = new GregorianCalendar();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        //if greater than max
                        if (newDate.after(getMaxDate())) {
                            view.init(getMaxDate().getYear(), getMaxDate().getMonthOfYear() - 1, getMaxDate().getDayOfMonth(), this);
                        }
                        //if less than min date
                        if (newDate.before(getMinDate())) {
                            view.init(getMinDate().getYear(), getMinDate().getMonthOfYear() - 1, getMinDate().getDayOfMonth(), this);
                        }


                        if (editText != null) {
                            //editText.setText();

                        }
                    }

                });
        //}

    }
    public void setRegDateDefaults(DatePicker datePicker, final EditText editText) {
        this.datePicker = datePicker;


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            this.datePicker.setMinDate(getMinDate().getMillis());
//            this.datePicker.setMaxDate(getMaxDate().getMillis());
//        } else {
        final int minYear = getMaxDate().getYear();
        final int minMonth = getMaxDate().getMonthOfYear() - 1;
        final int minDay = getMaxDate().getDayOfMonth();

        this.datePicker.init(minYear, minMonth, minDay,

                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar newDate = new GregorianCalendar();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        //if greater than max
                        if (newDate.after(getMaxDate())) {
                            view.init(getMaxDate().getYear(), getMaxDate().getMonthOfYear() - 1, getMaxDate().getDayOfMonth(), this);
                        }
                        //if less than min date
                        if (newDate.before(getMinDate())) {
                            view.init(getMinDate().getYear(), getMinDate().getMonthOfYear() - 1, getMinDate().getDayOfMonth(), this);
                        }


                        if (editText != null) {
                            //editText.setText();

                        }
                    }

                });
        //}

    }

    public void setDateDobAgeDefualts(DatePicker datePicker, final EditText editTextAgeAtRegistration) {
        return;
        /*
        // Calendar
        this.datePicker = datePicker;
        final int minYear = this.calendar.get(Calendar.YEAR);
        final int minMonth = this.calendar.get(Calendar.MONTH);
        final int minDay = this.calendar.get(Calendar.DAY_OF_MONTH);

        this.datePicker.init(minYear, minMonth, minDay,
                new DatePicker.OnDateChangedListener() {

                    public void onDateChanged(DatePicker view, int year,
                                              int month, int day) {

                        if (day == 0) {
                            day = 1;
                        }
                        if (month == 0) {
                            month = 1;
                        }

                        Date newDate = new Date(year, month, day);
                        boolean clearAge = false;

                        //if greater than max
                        if (newDate.after(maxDate)) {
                            view.init(maxDate.getYear(), maxDate.getMonth(), maxDate.getDay(), this);
                            clearAge = true;
                        }
                        //if less than min date
                        if (newDate.before(minDate)) {
                            view.init(1900, 1, 1, this);
                            clearAge = true;
                        }

                        if (editTextAgeAtRegistration != null) {
                            if (!clearAge) {
                                if (editTextAgeAtRegistration.getText().toString().length() == 0) {
                                    LocalDate currdate = new LocalDate(year, month, day);
                                    LocalDate now = new LocalDate();
                                    Years age = Years.yearsBetween(currdate, now);
                                    int currentAge = age.getYears();
                                    if (currentAge > 0) {
                                        editTextAgeAtRegistration.setText(String.valueOf(age.getYears()));
                                    }
                                }
                            }
                        }
                    }
                });
        */
    }
    public void setDateRegDefualts(DatePicker datePicker) {
/*
        // Calendar
        this.calendar = new GregorianCalendar();
        this.datePicker = datePicker;
        final int minYear = this.datePicker.getYear();
        final int minMonth = this.datePicker.getMonth();
        final int minDay = this.datePicker.getDayOfMonth();

        this.datePicker.init(minYear, minMonth, minDay,
                new DatePicker.OnDateChangedListener() {

                    public void onDateChanged(DatePicker view, int year,
                                              int month, int day) {

                        if (day == 0) {
                            day = 1;
                        }
                        if (month == 0) {
                            month = 1;
                        }

                        Date newDate = new Date(year, month, day);
                        boolean clearAge = false;

                        //if greater than max
                        if (newDate.after(maxDate)) {
                            view.init(maxDate.getYear(), maxDate.getMonth(), maxDate.getDay(), this);
                            clearAge = true;
                        }
                        //if less than min date
                        if (newDate.before(minDate)) {
                            view.init(minDate.getYear(), minDate.getMonth(),minDate.getDay(), this);
                            clearAge = true;
                        }

                    }
                });
        */
    }

}
