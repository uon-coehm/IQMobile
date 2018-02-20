package com.thepalladiumgroup.iqm.core.tests;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.IDatabaseHelper;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.core.services.IApplicationService;
import com.thepalladiumgroup.iqm.core.services.impl.ApplicationService;

/**
 * Created by Koske Kimutai [2016/04/13]
 */
public class TestApplication extends Application implements IApplicationManager {

    private static Context mContext;
    private IDatabaseHelper mDatabaseHelper;
    private IApplicationService mApplicationService;


    public OrmLiteSqliteOpenHelper getDatabaseHelper() {
        return (OrmLiteSqliteOpenHelper) mDatabaseHelper;
    }

    @Override
    public User getCurrentUser() {
        SharedPreferences sharedPref = getSharedPreferences("IQM_Prefs_Test", Context.MODE_PRIVATE);
        String userJson = sharedPref.getString("IQMUserTest", null);
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        return user;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        mContext = getApplicationContext();

        mDatabaseHelper = new TestDatabaseHelper(this);
        mApplicationService = new ApplicationService(this);
        mApplicationService.initialize();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
