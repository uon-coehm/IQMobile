/*
 * Copyright (c) 2013, Inmite s.r.o. (www.inmite.eu).
 *
 * All rights reserved. This source code can be used only for purposes specified
 * by the given license contract signed by the rightful deputy of Inmite s.r.o.
 * This source code can be used only by the owner of the license.
 *
 * Any disputes arising in respect of this agreement (license) shall be brought
 * before the Municipal Court of Prague.
 */

package eu.inmite.android.lib.validations.form.validators;

import android.text.TextUtils;

import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import eu.inmite.android.lib.validations.form.annotations.DateInPast;
import eu.inmite.android.lib.validations.form.annotations.ValidatorFor;

import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

/**
 * @author Tomas Vondracek
 */
@ValidatorFor(DateInPast.class)
public class PastDateValidator extends BaseDateValidator {

    @Override
    protected DateFormat getDateFormat(final Annotation annotation) {
        DateInPast dateAnnotation = (DateInPast) annotation;
        final DateFormat dateFormat;
        if (TextUtils.isEmpty(dateAnnotation.datePattern())) {
            dateFormat = DateFormat.getDateInstance(dateAnnotation.dateStyle());
        } else {
            dateFormat = new SimpleDateFormat(dateAnnotation.datePattern());
        }
        return dateFormat;
    }

    @Override
    protected boolean validateDate(final Calendar cal, final Annotation annotation) {
        final Calendar today = Calendar.getInstance();
        today.set(HOUR_OF_DAY, 0);
        today.set(MINUTE, 0);
        today.set(SECOND, 0);
        today.set(MILLISECOND, 0);

        DateInPast dateAnnotation = (DateInPast) annotation;
        if (!dateAnnotation.allowToday()) {
            today.add(DAY_OF_YEAR, 1);
        }
        return today.getTimeInMillis() <= cal.getTimeInMillis();
    }

}
