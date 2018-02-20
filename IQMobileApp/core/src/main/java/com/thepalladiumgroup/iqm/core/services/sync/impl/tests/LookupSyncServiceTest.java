package com.thepalladiumgroup.iqm.core.services.sync.impl.tests;

import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.services.ILookupService;
import com.thepalladiumgroup.iqm.core.services.impl.LookupService;
import com.thepalladiumgroup.iqm.core.services.sync.ILookupSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.impl.LookupSyncService;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public class LookupSyncServiceTest extends BaseCoreTest {

    private ILookupSyncService lookupSyncService;
    private ILookupService lookupService;

    @Before
    public void setUp() throws SQLException {
        lookupSyncService = new LookupSyncService(applicationManager);
        lookupService = new LookupService(applicationManager);
    }

    @Test
    public void should_initialize() throws Exception {
        lookupSyncService.initialze();
        Assert.assertNotNull(lookupSyncService.getServer());
        Assert.assertNotNull(lookupSyncService.getAdapter());

        System.out.println(lookupSyncService.getServer());
    }

    @Test
    public void should_read_all_lookups() throws Exception {
        lookupSyncService.initialze();
        List<Lookup> lookups = lookupSyncService.readAllLookups();
        Assert.assertNotNull(lookups);
        for (Lookup u : lookups) {
            System.out.println(u.getCodeid() + " | " + u.getName() + " | " + u.getIqcareid());
        }
    }

    @Test
    public void should_sync_all_lookups() throws Exception {
        lookupSyncService.initialze();
        List<Lookup> lookups = lookupSyncService.readAllLookups();
        Assert.assertNotNull(lookups);


        lookupService.getAllLookups();
        for (Lookup lookup : lookups) {
            lookupService.syncLookup(lookup);
        }

        List<Lookup> knhstaff = lookupService.getLookupsByCodeId(849);
        Assert.assertNotNull(knhstaff);
        for (Lookup u : knhstaff) {
            System.out.println(u.getCodeid() + " | " + u.getName() + " | " + u.getIqcareid());
        }
        Lookup stafffamily = lookupService.getLookupIQcareId(7119);
        Assert.assertNotNull(stafffamily);
        Assert.assertEquals("No, Staff Family", stafffamily.getName());
        System.out.println(stafffamily);
    }
}