package com.thepalladiumgroup.iqm.core.tests;


import android.database.sqlite.SQLiteDatabase;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Koske Kimutai [2016/04/13]
 */
public final class DatabaseTest extends BaseCoreTest {

    @Test
    public void testGetReadableDatabase() {
        final SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Assert.assertEquals(false, db.isReadOnly());
        System.out.println("database:" + db.getPath());
    }

    @Test
    public void testGetWritableDatabase() {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Assert.assertEquals(false, db.isReadOnly());
        System.out.println("database:" + db.getPath());
    }
}
