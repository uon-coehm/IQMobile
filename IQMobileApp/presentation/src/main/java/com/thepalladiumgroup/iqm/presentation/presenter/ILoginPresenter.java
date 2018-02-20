package com.thepalladiumgroup.iqm.presentation.presenter;


import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.presentation.view.ILoginView;

/**
 * Created by Koske Kimutai [2016/04/12]
 */
public interface ILoginPresenter {
    ILoginView getView();
    void loadUsers();
    boolean login();
    void  loadLookups();
    void setCurrentUser(User user);
}
