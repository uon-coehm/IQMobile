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
public class FormStep2 extends WizardStep {


    //You must have an empty constructor for every step
    public FormStep2() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step2, container, false);
        TextView tv = (TextView) v.findViewById(R.id.textViewStep2);
        tv.setText("This is an example of Step 2 and also the last step in this wizard. " +
                "By pressing Finish you will conclude this wizard and go back to the main activity." +
                "Hit the back button to go back to the previous step.");
        return v;
    }
}