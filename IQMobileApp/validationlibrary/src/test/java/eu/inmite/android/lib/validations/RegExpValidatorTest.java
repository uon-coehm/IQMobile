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
import eu.inmite.android.lib.validations.form.annotations.RegExp;
import eu.inmite.android.lib.validations.form.iface.IValidationCallback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Tomas Vondracek
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RegExpValidatorTest {

    private static ModelWithValidation initModel() {
        ModelWithValidation model = new ModelWithValidation(Robolectric.application);
        model.txtNotNumber.setText("");
        model.editNumber.setText("");
        return model;
    }

    @Test
    public void validInputShouldPass() throws Exception {
        ModelWithValidation model = initModel();
        model.txtNotNumber.setText("absd,/.';");
        model.editNumber.setText("0123456789");

        final boolean valid = FormValidator.validate(Robolectric.application, model, null);
        assertTrue(valid);
    }

    @Test
    public void invalidInputShouldNotPass() throws Exception {
        ModelWithValidation model = initModel();
        model.txtNotNumber.setText("123");
        model.editNumber.setText("!No pasaran!");

        final int[] failCount = new int[1];
        final boolean valid = FormValidator.validate(Robolectric.application, model, new IValidationCallback() {
            @Override
            public void validationComplete(boolean result, List<FormValidator.ValidationFail> failedValidations, List<View> passed) {
                failCount[0] = failedValidations.size();
            }
        });

        assertFalse(valid);
        assertEquals(2, failCount[0]);
    }

    private static class ModelWithValidation {

        @RegExp("^[^0-9]+$")
        TextView txtNotNumber;

        @RegExp("^[0-9]+$")
        EditText editNumber;

        LinearLayout layout;

        private ModelWithValidation(Context context) {
            txtNotNumber = new TextView(context);
            txtNotNumber.setId(10000);
            editNumber = new EditText(context);

            layout = new LinearLayout(context);
            layout.addView(txtNotNumber);
            layout.addView(editNumber);
        }
    }
}
