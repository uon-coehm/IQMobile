package com.thepalladiumgroup.iqm.presentation.presenter;


import com.thepalladiumgroup.iqm.presentation.view.IDemographicView;

/**
 * Created by Koske Kimutai [2016/04/16]
 */
public interface IDemographicPresenter {
    IDemographicView getView();
    String getJsonView();
    void calculateDob(int age);
    void calculateDobByUnit();
    void loadDemographics();
}
