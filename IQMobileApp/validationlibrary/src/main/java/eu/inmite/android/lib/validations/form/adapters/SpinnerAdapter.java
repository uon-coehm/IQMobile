package eu.inmite.android.lib.validations.form.adapters;

import android.widget.Spinner;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.iface.IFieldAdapter;

/**
 * @author Tomas Vondracek
 */
public class SpinnerAdapter implements IFieldAdapter<Spinner, Object> {

    @Override
    public Object getFieldValue(final Annotation annotation, final Spinner fieldView) {
        return fieldView.getSelectedItem();
    }
}
