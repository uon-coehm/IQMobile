package com.thepalladiumgroup.iqm.core.services.impl.tests;

import com.thepalladiumgroup.iqm.core.data.ILookupRepository;
import com.thepalladiumgroup.iqm.core.data.impl.LookupRepository;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.services.ILookupService;
import com.thepalladiumgroup.iqm.core.services.impl.LookupService;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;
import com.thepalladiumgroup.iqm.core.tests.TestData;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/22]
 */
public class LookupServiceTest extends BaseCoreTest {

    private ILookupService lookupService;
    private Lookup newLookup = new Lookup(-11, "NEW LOOKUP", -100);
    private ILookupRepository lookupRepository;
    private List<Integer> testlookupIds;
    @Before
    public void setUp() throws SQLException {
        testlookupIds = new ArrayList<>();
        lookupService = new LookupService(applicationManager);
        newLookup.setUuid("6488919e-a723-4ac0-a2ba-78fb1aadfadc");
        lookupRepository = new LookupRepository(applicationManager);

        for (Lookup l : TestData.getLookups()) {
            Lookup lookup = lookupRepository.save(l);
            testlookupIds.add(lookup.getId());
        }
    }

    @Test
    public void should_Get_Lookups_ByCode_Id() throws Exception {
        List<Lookup> lookups = lookupService.getLookupsByCodeId(-100);
        Assert.assertTrue(lookups.size() > 0);
        for (Lookup l : lookups) {
            System.out.println(l);
        }
    }

    @Test
    public void should_sync_Lookup_new() throws SQLException {
        Lookup syncedNew = null;

        lookupService.syncLookup(newLookup);

        syncedNew = lookupRepository.findbySyncId(newLookup.getUuid());
        testlookupIds.add(syncedNew.getId());
        Assert.assertNotNull(syncedNew);
        System.out.println(syncedNew);

    }

    @Test
    public void should_sync_Module_existing() throws SQLException {
        String name = "FAKE LOOKUP";
        Lookup lookup = lookupRepository.findbySyncId("53853cd3-effd-4fc6-b8eb-4164c4c5b2ab");
        Assert.assertNotNull(lookup);
        lookup.setName(name);

        lookupService.syncLookup(lookup);

        Lookup syncedLookup = lookupRepository.findbySyncId(lookup.getUuid());

        Assert.assertNotNull(syncedLookup);
        Assert.assertEquals("FAKE LOOKUP", syncedLookup.getName());

        System.out.println(syncedLookup);
    }

    @After
    public void tearDown() throws SQLException {

        for (int userid : testlookupIds) {
            lookupRepository.delete(userid);
        }
    }
}