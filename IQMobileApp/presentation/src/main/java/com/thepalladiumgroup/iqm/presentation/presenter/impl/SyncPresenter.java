package com.thepalladiumgroup.iqm.presentation.presenter.impl;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.Module;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.Server;
import com.thepalladiumgroup.iqm.core.model.SyncAction;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.core.services.IApplicationService;
import com.thepalladiumgroup.iqm.core.services.IConceptService;
import com.thepalladiumgroup.iqm.core.services.IEncounterTypeService;
import com.thepalladiumgroup.iqm.core.services.ILookupService;
import com.thepalladiumgroup.iqm.core.services.IModuleService;
import com.thepalladiumgroup.iqm.core.services.IPatientService;
import com.thepalladiumgroup.iqm.core.services.IUserService;
import com.thepalladiumgroup.iqm.core.services.impl.ApplicationService;
import com.thepalladiumgroup.iqm.core.services.impl.ConceptService;
import com.thepalladiumgroup.iqm.core.services.impl.EncounterTypeService;
import com.thepalladiumgroup.iqm.core.services.impl.LookupService;
import com.thepalladiumgroup.iqm.core.services.impl.ModuleService;
import com.thepalladiumgroup.iqm.core.services.impl.PatientService;
import com.thepalladiumgroup.iqm.core.services.impl.UserService;
import com.thepalladiumgroup.iqm.core.services.sync.IConceptSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.ILookupSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.IModuleSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.IPatientSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.IUserSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.impl.ConceptSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.impl.LookupSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.impl.ModuleSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.impl.PatientSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.impl.UserSyncService;
import com.thepalladiumgroup.iqm.presentation.presenter.ISyncPresenter;
import com.thepalladiumgroup.iqm.presentation.view.ISyncView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/18]
 */
public class SyncPresenter implements ISyncPresenter {
    //TODO: remove set DEV_MODE to false
    private static final boolean DEV_MODE = false;
    private static final Logger LOG = LoggerFactory.getLogger(SyncPresenter.class);
    private final IPatientService patientService;
    private final IUserService userService;

    private final ILookupService lookupService;
    private final IModuleService moduleService;
    private final IEncounterTypeService encounterTypeService;
    private final IConceptService conceptService;
    private final IPatientSyncService patientSyncService;
    private final IUserSyncService userSyncService;
    private final ILookupSyncService lookupSyncService;
    private final IModuleSyncService moduleSyncService;
    private final IConceptSyncService conceptSyncService;
    private final IApplicationService applicationService;
    private boolean userOk = false;
    private boolean lookupOk = false;
    private boolean modulesOk = false;
    private boolean conceptOk = false;
    private SyncUserAppSettingsTask syncUserAppSettingsTask;
    private SyncLookupAppSettingsTask syncLookupAppSettingsTask;
    private SyncModuleAppSettingsTask syncModuleAppSettingsTask;
    private SyncConceptsAppSettingsTask syncConceptsAppSettingsTask;
    private SyncDataTask syncDataTask;

    private User user;
    private ISyncView view;
    private String status;
    private String error;
    private int totalSteps = 8;
    private int step = 0;
    private int totalStepsData = 2;
    private int stepData = 0;

    public SyncPresenter(ISyncView view, IApplicationManager applicationManager) {
        this.view = view;
        this.view.setPresenter(this);
        patientService = new PatientService(applicationManager);
        userService = new UserService(applicationManager);
        lookupService = new LookupService(applicationManager);
        moduleService = new ModuleService(applicationManager);
        encounterTypeService = new EncounterTypeService(applicationManager);
        conceptService = new ConceptService(applicationManager);

        patientSyncService = new PatientSyncService(applicationManager);
        userSyncService = new UserSyncService(applicationManager);
        lookupSyncService = new LookupSyncService(applicationManager);
        moduleSyncService = new ModuleSyncService(applicationManager);
        conceptSyncService = new ConceptSyncService(applicationManager);

        applicationService = new ApplicationService(applicationManager);
        try {
            user = applicationManager.getCurrentUser();
        } catch (Exception e) {
            LOG.debug("Error loading user:", e);
        }
    }

    @Override
    public ISyncView getView() {
        return view;
    }

    @Override
    public void loadSettings() {
        try {
            getView().showAppSettingsProgress(false);
            getView().showAppDataProgress(false);
            userSyncService.initialze();
            getView().setServer(userSyncService.getServer());
        } catch (SQLException e) {
            getView().setViewAppSettingsErrors(e.getMessage());
        }
    }

    @Override
    public void saveServer() {
        Server server = getView().getServer();
        if (user != null) {
            server.setUserid(user.getId());
        }
        try {
            applicationService.saveServer(server);
            loadSettings();
        } catch (SQLException e) {
            getView().setViewAppSettingsErrors(e.getMessage());
        }
    }

    @Override
    public void syncAppSettings() {
        step = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getUserTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            getUserTask().execute();
        }
    }

    public void pushUsers() {
    }

    @Override
    public void syncAppData() {
        stepData = 0;
        getDataTask().execute();
    }

    @Override
    public String getError() {
        return status;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public AsyncTask<SyncAction, Integer, Boolean> getUserTask() {
        if (syncUserAppSettingsTask == null) {
            syncUserAppSettingsTask = new SyncUserAppSettingsTask();
        }
        return syncUserAppSettingsTask;
    }

    @Override
    public AsyncTask<SyncAction, Integer, Boolean> getLookupTask() {
        if (syncLookupAppSettingsTask == null) {
            syncLookupAppSettingsTask = new SyncLookupAppSettingsTask();
        }
        return syncLookupAppSettingsTask;
    }

    @Override
    public AsyncTask<SyncAction, Integer, Boolean> getModuleTask() {
        if (syncModuleAppSettingsTask == null) {
            syncModuleAppSettingsTask = new SyncModuleAppSettingsTask();
        }
        return syncModuleAppSettingsTask;
    }

    @Override
    public AsyncTask<SyncAction, Integer, Boolean> getConceptTask() {
        if (syncConceptsAppSettingsTask == null) {
            syncConceptsAppSettingsTask = new SyncConceptsAppSettingsTask();
        }
        return syncConceptsAppSettingsTask;
    }

    @Override
    public AsyncTask<SyncAction, Integer, Boolean> getDataTask() {
        if (syncDataTask == null) {
            syncDataTask = new SyncDataTask();
        }
        return syncDataTask;
    }

    private void beforeTask() {
        error = "";
        status = "ready";
        getView().showAppSettingsProgress(true);
        getView().setBusy(true);
        getView().setViewAppSettingsErrors(error);
        getView().setViewAppSettingsStatus(status);
    }

    private void beforeDataTask() {
        error = "";
        status = "ready";
        getView().showAppDataProgress(true);
        getView().setBusy(true);
        getView().setViewAppDataErrors(error);
        getView().setViewAppDataStatus(status);
    }

    private void progressUpdate(Integer... values) {
        ((Activity) getView()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getView().setViewAppSettingsStatus(status);
                getView().setViewAppSettingsErrors(error);
            }
        });
    }

    private void progressDataUpdate(Integer... values) {
        ((Activity) getView()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getView().setViewAppDataStatus(status);
                getView().setViewAppDataErrors(error);
            }
        });
    }

    private void afterTask(Boolean aBoolean) {
        getView().showAppSettingsProgress(false);
        getView().setBusy(false);
    }

    private void afterDataTask(Boolean aBoolean) {
        getView().showAppDataProgress(false);
        getView().setBusy(false);
    }

    private int calculateProgress(int current, int total) {
        Double progress;
        Double i = Double.valueOf(current);
        Double c = Double.valueOf(total);
        progress = (i / c) * 100;
        return progress.intValue();
    }

    private void lala(int time) {
        if (DEV_MODE) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class SyncUserAppSettingsTask extends AsyncTask<SyncAction, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            beforeTask();
            syncLookupAppSettingsTask = null;
            syncModuleAppSettingsTask = null;
            syncConceptsAppSettingsTask = null;
        }

        @Override
        protected Boolean doInBackground(SyncAction... params) {

            String action = "Users";

            boolean success = true;


            int usersFound = 0;
            List<User> users = null;
            List<Lookup> lookups = null;


            LOG.debug("pulling " + action + "...");

            step++;
            // 1 - pull Users

            status = "Step " + step + " of " + totalSteps + ": reading " + action + "...";
            LOG.debug(status);
            publishProgress(10);

            //TODO: remove sleep in production
            lala(3000);

            try {
                users = userSyncService.readAllUsers();
                if (users != null) {
                    usersFound = users.size();
                }
                status = "Step " + step + " of " + totalSteps + ": [" + usersFound + "] " + action + " found";
                LOG.debug(status);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);

            } catch (IOException e) {
                success = false;
                error = e.getMessage();
                status = "Step " + step + " of " + totalSteps + ": reading " + action + " [Failed]";
                LOG.debug(status);
                LOG.debug("The following error occurred", e);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);
            }


            step++;
            // 2 - sync Users

            if (users != null) {

                try {
                    userService.ClearAll();
                    userService.loadUsers();
                } catch (SQLException e) {
                    success = false;
                    error = e.getMessage();
                    status = "clear users Failed";
                    LOG.debug(status);
                    LOG.debug("The following error occurred", e);
                    publishProgress(10);
                }

                status = "Step " + step + " of " + totalSteps + ": updating " + action + "...";
                LOG.debug(status);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);


                int count = 0;
                for (User u : users) {
                    count++;
                    status = "Step " + step + " of " + totalSteps + ": updating " + action + " [" + calculateProgress(count, usersFound) + " %]";
                    //LOG.debug(status);
                    publishProgress(10);

                    //TODO: remove sleep in production
                    lala(3000);
                    try {

                        userService.syncUser(u);
                        //LOG.debug("updated " + action + " " + u.toString());
                    } catch (SQLException e) {
                        success = false;
                        error = e.getMessage();
                        status = "updating " + action + " " + u.toString() + " Failed";
                        LOG.debug(status);
                        LOG.debug("The following error occurred", e);
                        publishProgress(10);
                    }
                }
            }
            if (success) {
                status = "reading " + action + " completed successfully";
            } else {
                status = "reading " + action + " competed with errors";
            }
            LOG.debug(status);
            publishProgress(10);
            //TODO: remove sleep in production
            lala(3000);
            return success;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getLookupTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                getLookupTask().execute();
            }
        }
    }

    private class SyncLookupAppSettingsTask extends AsyncTask<SyncAction, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            syncUserAppSettingsTask = null;
        }

        @Override
        protected Boolean doInBackground(SyncAction... params) {

            String action = "Lookups";

            boolean success = true;
            int lookupsFound = 0;
            List<Lookup> lookups = null;

            LOG.debug("pulling " + action + "...");

            step++;
            // 1 - pull Users

            status = "Step " + step + "/" + totalSteps + ": reading " + action + "";
            LOG.debug(status);
            publishProgress(10);

            //TODO: remove sleep in production
            lala(3000);

            try {

                lookups = lookupSyncService.readAllLookups();
                if (lookups != null) {
                    lookupsFound = lookups.size();
                }


                status = "Step " + step + "/" + totalSteps + ":[" + lookupsFound + "] " + action + " found";
                LOG.debug(status);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);

            } catch (IOException e) {
                success = false;
                error = e.getMessage();
                status = "Step " + step + "/" + totalSteps + ":reading " + action + " [Failed]";
                LOG.debug(status);
                LOG.debug("The following error occurred", e);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);
            }


            step++;
            // 2 - sync Users

            if (lookups != null) {

                try {
                    lookupService.getAllLookups();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                status = "Step " + step + "/" + totalSteps + ":updating " + action + "...";
                LOG.debug(status);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);


                int count = 0;
                for (Lookup u : lookups) {
                    count++;
                    status = "Step " + step + "/" + totalSteps + ":updating " + action + " [" + calculateProgress(count, lookupsFound) + " %]";
                    //LOG.debug(status);
                    publishProgress(10);

                    //TODO: remove sleep in production
                    lala(3000);
                    try {
                        lookupService.syncLookup(u);
                        //LOG.debug("updated " + action + " " + u.toString());
                    } catch (SQLException e) {
                        success = false;
                        error = e.getMessage();
                        status = "updating " + action + " " + u.toString() + " Failed";
                        LOG.debug(status);
                        LOG.debug("The following error occurred", e);
                        publishProgress(10);
                    }
                }
            }
            if (success) {
                status = "reading " + action + " completed successfully";
            } else {
                status = "reading " + action + " competed with errors";
            }
            LOG.debug(status);
            publishProgress(10);
            //TODO: remove sleep in production
            lala(3000);
            return success;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getModuleTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                getModuleTask().execute();
            }

        }
    }

    private class SyncModuleAppSettingsTask extends AsyncTask<SyncAction, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            syncLookupAppSettingsTask = null;
        }

        @Override
        protected Boolean doInBackground(SyncAction... params) {

            String action = "Modules";

            boolean success = true;
            int modulesFound = 0;
            List<Module> modules = null;

            LOG.debug("pulling " + action + "...");

            step++;
            // 1 - pull Users

            status = "Step " + step + "/" + totalSteps + ":reading " + action + "...";
            LOG.debug(status);
            publishProgress(10);

            //TODO: remove sleep in production
            lala(3000);

            try {
                modules = moduleSyncService.readAllModules();
                if (modules != null) {
                    modulesFound = modules.size();
                }
                status = "Step " + step + "/" + totalSteps + ":[" + modulesFound + "] " + action + " found";
                LOG.debug(status);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);

            } catch (IOException e) {
                success = false;
                error = e.getMessage();
                status = "Step " + step + "/" + totalSteps + ":reading " + action + " [Failed]";
                LOG.debug(status);
                LOG.debug("The following error occurred", e);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);
            }


            step++;
            // 2 - sync Users

            try {
                moduleService.loadModules();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (modules != null) {

                status = "Step " + step + "/" + totalSteps + ":updating " + action + "...";
                LOG.debug(status);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);


                int count = 0;
                for (Module u : modules) {
                    count++;
                    status = "Step " + step + "/" + totalSteps + ":updating " + action + "[" + String.valueOf(count) + " of " + String.valueOf(modulesFound) + "]";
                    LOG.debug(status);
                    publishProgress(10);

                    //TODO: remove sleep in production
                    lala(3000);
                    try {
                        moduleService.syncModule(u);
                        LOG.debug("updated " + action + " " + u.toString());
                        status = "Step " + step + "/" + totalSteps + ":updating " + action + " [" + calculateProgress(count, modulesFound) + " %]";
                        LOG.debug(status);
                        publishProgress(10);

                    } catch (SQLException e) {
                        success = false;
                        error = e.getMessage();
                        status = "updating " + action + " " + u.toString() + " Failed";
                        LOG.debug(status);
                        LOG.debug("The following error occurred", e);
                        publishProgress(10);
                    }
                }
            }


            if (success) {
                status = "reading " + action + " completed successfully";
            } else {
                status = "reading " + action + " competed with errors";
            }
            LOG.debug(status);
            publishProgress(10);
            //TODO: remove sleep in production
            lala(3000);
            return success;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getConceptTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                getConceptTask().execute();
            }
        }
    }

    private class SyncConceptsAppSettingsTask extends AsyncTask<SyncAction, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            syncModuleAppSettingsTask = null;
        }

        @Override
        protected Boolean doInBackground(SyncAction... params) {

            String action = "Concepts";

            boolean success = true;
            int conceptsFound = 0;
            List<MConcept> concepts = null;
            int encounterTypeId = 0;
            EncounterType encounterType = null;


            LOG.debug("pulling " + action + "...");

            step++;
            // 1 - pull Users

            status = "Step " + step + "/" + totalSteps + ":reading " + action + "...";
            LOG.debug(status);
            publishProgress(10);

            //TODO: remove sleep in production
            lala(3000);

            try {
                try {
                    Module module = moduleService.getByName("HTS");
                    if (module != null) {
                        encounterType = encounterTypeService.getByModule(module.getId());
                        if (encounterType != null) {
                            encounterTypeId = encounterType.getId();
                        }
                    }

                } catch (SQLException e) {

                }

                concepts = conceptSyncService.readAllConcepts(encounterTypeId);
                if (concepts != null) {
                    conceptsFound = concepts.size();
                }
                status = "Step " + step + "/" + totalSteps + ":[" + conceptsFound + "] " + action + " found";
                LOG.debug(status);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);

            } catch (IOException e) {
                success = false;
                error = e.getMessage();
                status = "Step " + step + "/" + totalSteps + ":reading " + action + " [Failed]";
                LOG.debug(status);
                LOG.debug("The following error occurred", e);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);
            }


            step++;
            // 2 - sync Users

            try {
                conceptService.loadConcepts(encounterTypeId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (concepts != null && encounterType != null) {


                status = "Step " + step + "/" + totalSteps + ":updating " + action + "...";
                LOG.debug(status);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);


                int count = 0;
                for (MConcept u : concepts) {
                    count++;
                    status = "Step " + step + "/" + totalSteps + ":updating " + action + " [" + calculateProgress(count, conceptsFound) + " %]";
                    //LOG.debug(status);
                    publishProgress(10);

                    //TODO: remove sleep in production
                    lala(3000);
                    try {
                        u.setEncounterType(encounterType);
                        conceptService.syncConcept(u);
                        //LOG.debug("updated " + action + " " + u.toString());
                    } catch (SQLException e) {
                        success = false;
                        error = e.getMessage();
                        status = "updating " + action + " " + u.toString() + " Failed";
                        LOG.debug(status);
                        LOG.debug("The following error occurred", e);
                        publishProgress(10);
                    }
                }
            }
            if (success) {
                status = "settings update completed successfully";
            } else {
                status = "settings update completed with errors";
            }
            LOG.debug(status);
            publishProgress(10);
            //TODO: remove sleep in production
            lala(3000);
            return success;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            afterTask(aBoolean);
        }
    }

    private class SyncDataTask extends AsyncTask<SyncAction, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            beforeDataTask();
        }

        @Override
        protected Boolean doInBackground(SyncAction... params) {

            String action = "Patients";

            boolean success = true;

            int usersFound = 0;
            List<Patient> patients = null;
            List<Integer> patientIds = new ArrayList<>();

            LOG.debug("pushing " + action + "...");

            stepData++;
            // 1 - check Patients

            status = "Step " + stepData + "/" + totalStepsData + ":sending " + action + "...";
            LOG.debug(status);
            publishProgress(10);

            //TODO: remove sleep in production
            lala(3000);

            try {
                patientIds = patientService.getAllCompletePateintIds();
                if (patientIds != null) {
                    usersFound = patientIds.size();
                }
                status = "Step " + stepData + "/" + totalStepsData + ":[" + usersFound + "] " + action + " found";
                LOG.debug(status);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);

            } catch (SQLException e) {
                success = false;
                error = e.getMessage();
                status = "Step " + stepData + "/" + totalStepsData + ":reading " + action + " [Failed]";
                LOG.debug(status);
                LOG.debug("The following error occurred", e);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);
            }


            step++;
            // 2 - send Patients

            if (patientIds != null) {

                status = "Step " + stepData + "/" + totalStepsData + ":sending " + action + "...";
                LOG.debug(status);
                publishProgress(10);
                //TODO: remove sleep in production
                lala(3000);


                int count = 0;
                for (Integer u : patientIds) {
                    count++;
                    status = "Step " + stepData + "/" + totalStepsData + ":sending " + action + " [" + count + "/" + usersFound + "]";
                    LOG.debug(status);
                    publishProgress(10);

                    //TODO: remove sleep in production
                    lala(3000);
                    try {
                        Patient toSend = patientService.getToSend(u);
                        patientSyncService.sendPatient(toSend);
                        try {
                            patientService.deletePatientRecords(toSend.getId());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        LOG.debug("updated " + action + " " + toSend.toString());
                    } catch (IOException e) {
                        success = false;
                        error = e.getMessage();
                        status = "sending " + action + " " + String.valueOf(u) + " Failed";
                        LOG.debug(status);
                        LOG.debug("The following error occurred", e);
                        publishProgress(10);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    status = "Step " + stepData + "/" + totalStepsData + ":sending " + action + " [" + calculateProgress(count, usersFound) + " %]";
                    //LOG.debug(status);
                    publishProgress(10);

                }
            }
            if (success) {
                status = "sending " + action + " completed successfully";
            } else {
                status = "sending " + action + " completed with errors";
            }
            LOG.debug(status);
            publishProgress(10);
            //TODO: remove sleep in production
            lala(3000);
            return success;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDataUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            afterDataTask(aBoolean);
        }
    }
}
