package com.thepalladiumgroup.iqm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.IDatabaseHelper;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.core.services.IApplicationService;
import com.thepalladiumgroup.iqm.core.services.impl.ApplicationService;
import com.thepalladiumgroup.iqm.database.DatabaseHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Koske Kimutai [2016/04/13]
 */
public class IQMobileApplication extends Application implements IApplicationManager {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(IQMobileApplication.class);
    private static Context mContext;

    private IDatabaseHelper mDatabaseHelper;
    private IApplicationService mApplicationService;


    public OrmLiteSqliteOpenHelper getDatabaseHelper() {
        return (OrmLiteSqliteOpenHelper) mDatabaseHelper;
    }


    @Override
    public User getCurrentUser() {
        SharedPreferences sharedPref = getSharedPreferences("IQM_Prefs", Context.MODE_PRIVATE);
        String userJson = sharedPref.getString("IQMUser", null);
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        return user;
    }

    @Override
    public void onCreate() {
        MultiDex.install(getApplicationContext());
        super.onCreate();

        SLF_LOGGER.debug("STARTING application...");
        mContext = getApplicationContext();

        mDatabaseHelper = new DatabaseHelper(this);

        mApplicationService = new ApplicationService(this);
        mApplicationService.initialize();
        SLF_LOGGER.debug("DATABASE LOCATION" + mDatabaseHelper.getfullDatabaseName());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
