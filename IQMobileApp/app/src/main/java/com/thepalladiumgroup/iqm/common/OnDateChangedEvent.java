package com.thepalladiumgroup.iqm.common;

import android.widget.DatePicker;

import org.joda.time.LocalDate;

/**
 * Created by Koske Kimutai [2016/05/06]
 */
public class OnDateChangedEvent implements DatePicker.OnDateChangedListener {

    private final int MIN_AGE = 110;
    private LocalDate minDate;
    private LocalDate maxDate;
    private DatePicker datePicker;

    public OnDateChangedEvent() {
        LocalDate defaultMin = new LocalDate();
        this.minDate = defaultMin.minusYears(MIN_AGE);
        this.maxDate = new LocalDate();
    }

    public OnDateChangedEvent(LocalDate minDate) {

        this.minDate = minDate;
        this.maxDate = new LocalDate();
    }

    public OnDateChangedEvent(LocalDate minDate, LocalDate maxDate) {
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    public LocalDate getMinDate() {
        return minDate;
    }

    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(DatePicker datePicker) {
        this.datePicker = datePicker;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        LocalDate newDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);

        //if greater than max
        if (newDate.isAfter(getMaxDate())) {
            view.init(getMaxDate().getYear(), getMaxDate().getMonthOfYear() - 1, getMaxDate().getDayOfMonth(), this);
        }
        //if less than min date
        if (newDate.isBefore(getMinDate())) {
            view.init(getMinDate().getYear(), getMinDate().getMonthOfYear() - 1, getMinDate().getDayOfMonth(), this);
        }
    }
}
