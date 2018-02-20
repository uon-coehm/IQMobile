package com.thepalladiumgroup.iqm.core.tests;


import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.thepalladiumgroup.iqm.core.BuildConfig;
import com.thepalladiumgroup.iqm.core.IApplicationManager;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by Koske Kimutai [2016/04/14]
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class, application = TestApplication.class)
public abstract class BaseCoreTest {

    protected IApplicationManager applicationManager;
    protected OrmLiteSqliteOpenHelper databaseHelper;

    public BaseCoreTest() {
        applicationManager = (IApplicationManager) RuntimeEnvironment.application;
        databaseHelper = applicationManager.getDatabaseHelper();
    }
}
