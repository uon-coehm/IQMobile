package com.thepalladiumgroup.iqm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.thepalladiumgroup.iqm.core.IDatabaseHelper;
import com.thepalladiumgroup.iqm.core.model.User;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Koske Kimutai [2016/04/14]
 */
/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper implements IDatabaseHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_PATH = "/data/data/com.thepalladiumgroup.iqm/databases/";
    private static final String DATABASE_NAME = "iqcaremobile.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 20160502;
    private String databasepath = "";
    private SQLiteDatabase database;
    private DatabaseInitializer initializer;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        initializer = new DatabaseInitializer(context);
        try {
            initializer.createDatabase();
            initializer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {

            Log.i(DatabaseHelper.class.getName(), "onCreate");

            TableUtils.createTable(connectionSource, User.class);
            /*
            TableUtils.createTable(connectionSource, ActiveSession.class);
            TableUtils.createTable(connectionSource, Device.class);

            TableUtils.createTable(connectionSource, Patient.class);
            TableUtils.createTable(connectionSource, Lookup.class);
            TableUtils.createTable(connectionSource, Server.class);

            TableUtils.createTable(connectionSource, MDataTypeMap.class);

            TableUtils.createTable(connectionSource, Module.class);
            TableUtils.createTable(connectionSource, EncounterType.class);
            TableUtils.createTable(connectionSource, MConcept.class);


            TableUtils.createTable(connectionSource, Encounter.class);
            TableUtils.createTable(connectionSource, Observation.class);
            */

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
        databasepath = db.getPath();
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");

            TableUtils.dropTable(connectionSource, User.class, true);
            /*
            TableUtils.dropTable(connectionSource, ActiveSession.class, true);
            TableUtils.dropTable(connectionSource, Device.class, true);

            TableUtils.dropTable(connectionSource, Patient.class, true);
            TableUtils.dropTable(connectionSource, Lookup.class, true);
            TableUtils.dropTable(connectionSource, Server.class, true);

            TableUtils.dropTable(connectionSource, MDataTypeMap.class, true);

            TableUtils.dropTable(connectionSource, Module.class, true);
            TableUtils.dropTable(connectionSource, EncounterType.class, true);
            TableUtils.dropTable(connectionSource, MConcept.class, true);


            TableUtils.dropTable(connectionSource, Encounter.class, true);
            TableUtils.dropTable(connectionSource, Observation.class, true);
            */
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
        databasepath = db.getPath();
    }


    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
    }

    @Override
    public String getfullDatabaseName() {
        return databasepath;
    }


}