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

package eu.inmite.android.lib.validations;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.iface.IValidationCallback;

import static org.junit.Assert.assertTrue;

/**
 * @author Tomas Vondracek
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ContinuousValidationTest {

    private static ModelWithValidation initModel() {
        ModelWithValidation model = new ModelWithValidation(Robolectric.application);
        model.txtNumber.setText("");
        model.edit.setText("");
        return model;
    }

    @Test
    public void startedValidationShouldBeStoppable() {
        ModelWithValidation model = initModel();

        FormValidator.startLiveValidation(model, model.layout, new IValidationCallback() {
            @Override
            public void validationComplete(boolean result, List<FormValidator.ValidationFail> failedValidations, List<View> passed) {
            }
        });
        boolean stopped = FormValidator.stopLiveValidation(model);
        assertTrue(stopped);
    }

    private static class ModelWithValidation {

        @NotEmpty
        TextView txtNumber;
        @NotEmpty
        EditText edit;

        LinearLayout layout;

        private ModelWithValidation(Context context) {
            txtNumber = new TextView(context);
            txtNumber.setId(10000);
            edit = new EditText(context);

            layout = new LinearLayout(context);
            layout.addView(txtNumber);
            layout.addView(edit);
        }
    }
}
