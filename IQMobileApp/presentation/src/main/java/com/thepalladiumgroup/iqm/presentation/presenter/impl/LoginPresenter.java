package com.thepalladiumgroup.iqm.presentation.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.core.services.IConceptService;
import com.thepalladiumgroup.iqm.core.services.ILookupService;
import com.thepalladiumgroup.iqm.core.services.IUserService;
import com.thepalladiumgroup.iqm.core.services.impl.ConceptService;
import com.thepalladiumgroup.iqm.core.services.impl.LookupService;
import com.thepalladiumgroup.iqm.core.services.impl.UserService;
import com.thepalladiumgroup.iqm.presentation.presenter.ILoginPresenter;
import com.thepalladiumgroup.iqm.presentation.view.ILoginView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/12]
 */
public class LoginPresenter implements ILoginPresenter {

    //region fields
    private static final Logger LOG = LoggerFactory.getLogger(LoginPresenter.class);
    private final IUserService userService;
    private final ILoginView view;
    private final ILookupService lookupService;
    private final IConceptService conceptService;
    //endregion

    public LoginPresenter(ILoginView view, IApplicationManager applicationManager) {
        this.view = view;
        this.view.setPresenter(this);
        this.userService = new UserService(applicationManager);
        this.lookupService = new LookupService(applicationManager);
        this.conceptService = new ConceptService(applicationManager);
    }

    @Override
    public ILoginView getView() {
        return view;
    }

    @Override
    public void loadUsers() {
        try {
            List<User> userList = userService.loadUsers();
            Collections.sort(userList);
            view.setUsersList(userList);
        } catch (SQLException e) {
            LOG.debug(e.getStackTrace().toString());
            getView().showError("Error occured during login" + e.getMessage());
        }
    }

    @Override
    public void loadLookups() {
        List<MConcept> mconcepts = null;
        int iStrategyConceptId = 0;

        try {
            mconcepts = conceptService.getAllConcepts();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < mconcepts.size(); i++) {
            MConcept concept=mconcepts.get(i);
            if(concept.getDisplay().toLowerCase().contains("service area")) {
                iStrategyConceptId = concept.getLookupcodeid();
                break;
            }
        }

        List<Lookup> lookups = null;

        try {
            lookups = lookupService.getLookupsByCodeId(iStrategyConceptId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getView().setLookups(lookups);
    }

    @Override
    public boolean login() {
        User user = getView().getUser();
        int iStategyIQCareId = 0;
        LOG.debug("attempting to login {}", user.toString());
        User authenticUser = null;
        try {
            authenticUser = userService.validateUser(user);
        } catch (SQLException e) {
            LOG.debug(e.getStackTrace().toString());
            getView().showError("Error occured during login" + e.getMessage());
            return false;
        }

        if (authenticUser == null) {
            getView().showError("Wrong password ! try agin");
            return false;
        }
        else{
            iStategyIQCareId = user.getStrategy();
            authenticUser.setStrategy(iStategyIQCareId);
        }

        setCurrentUser(authenticUser);
        return true;
    }

    @Override
    public void setCurrentUser(User user) {
        SharedPreferences sharedPref = ((Activity) getView()).getSharedPreferences("IQM_Prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString("IQMUser", userJson);
        editor.commit();
        LOG.debug("IQMUser:" + user.toString());
    }
}
