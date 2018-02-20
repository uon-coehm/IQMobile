package com.thepalladiumgroup.iqm.core;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.thepalladiumgroup.iqm.core.model.User;

/**
 * Created by Koske Kimutai [2016/04/20]
 */
public interface IApplicationManager {
    OrmLiteSqliteOpenHelper getDatabaseHelper();


    User getCurrentUser();
}
