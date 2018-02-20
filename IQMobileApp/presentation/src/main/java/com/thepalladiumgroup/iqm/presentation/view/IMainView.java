package com.thepalladiumgroup.iqm.presentation.view;

import com.thepalladiumgroup.iqm.core.model.PatientStats;
import com.thepalladiumgroup.iqm.core.model.RecordStats;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.presentation.presenter.IMainPresenter;

/**
 * Created by Koske Kimutai [2016/04/12]
 */
public interface IMainView {
    void setPresenter(IMainPresenter presenter);

    void setCurrentUser(User user);

    void setPatientStats(PatientStats patientStats);

    void setRecordStats(RecordStats recordStats);

    void initialize();
}
