package com.thepalladiumgroup.iqm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.codepond.wizardroid.WizardStep;

/**
 * Created by Koske Kimutai [2016/04/18]
 */
public class FormStep1 extends WizardStep {

    //Wire the layout to the step
    public FormStep1() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step1, container, false);
        TextView tv = (TextView) v.findViewById(R.id.textViewStep1);
        tv.setText("This is an example of Step 1 in the wizard. Press the Next " +
                "button to proceed to the next step. Hit the back button to go back to the calling activity.");

        return v;
    }
}