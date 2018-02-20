package com.thepalladiumgroup.iqm.presentation.presenter;


import com.thepalladiumgroup.iqm.presentation.view.IServiceAreaView;

/**
 * Created by Koske Kimutai [2016/04/18]
 */
public interface IServiceAreaPresenter {
    IServiceAreaView getView();

    boolean dataIsValid();

    String getJsonView();

    boolean savePatient();

    void loadLookups();

    void loadPartners();
    void loadServiceArea();

    void saveServiceArea();
}
