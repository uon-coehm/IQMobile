package com.thepalladiumgroup.iqm;

import android.app.Activity;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.layouts.BasicWizardLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Koske Kimutai [2016/04/18]
 */


public class RegisterWizard extends BasicWizardLayout {
    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(LoginActivity.class);

    /**
     * Note that initially BasicWizardLayout inherits from {@link android.support.v4.app.Fragment} and therefore you must have an empty constructor
     */
    public RegisterWizard() {
        super();
    }

    //You must override this method and create a wizard flow by
    //using WizardFlow.Builder as shown in this example
    @Override
    public WizardFlow onSetup() {
        /* Optionally, you can set different labels for the control buttons
        setNextButtonLabel("Advance");
        setBackButtonLabel("Return");
        setFinishButtonLabel("Finalize"); */

        return new WizardFlow.Builder()
                .addStep(DemographicForm.class)           //Add your steps in the order you want them
                .addStep(ContactForm.class)           //to appear and eventually call create()
                .addStep(SelectServiceForm.class)
                .create();                              //to create the wizard flow.
    }

    @Override
    public void onWizardComplete() {
        super.onWizardComplete();
        SLF_LOGGER.debug("WIZARD COMPLETE");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}