package com.thepalladiumgroup.iqm.presentation.view;

import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.presentation.presenter.ILoginPresenter;

import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/12]
 */
public interface ILoginView {
    void setPresenter(ILoginPresenter presenter);
    void setUsersList(List<User> Users);
    User getUser();

    void initialize();

    boolean viewIsValid();

    void showError(String error);
    void setLookups(List<Lookup> lookups);
}
