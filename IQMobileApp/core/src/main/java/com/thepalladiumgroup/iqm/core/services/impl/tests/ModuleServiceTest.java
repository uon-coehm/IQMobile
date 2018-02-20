package com.thepalladiumgroup.iqm.core.services.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IModuleRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ModuleRepository;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Module;
import com.thepalladiumgroup.iqm.core.services.IModuleService;
import com.thepalladiumgroup.iqm.core.services.impl.ModuleService;
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
 * Created by Koske Kimutai [2016/04/13]
 */

public final class ModuleServiceTest extends BaseCoreTest {

    private final String uuid = "cdf81e36-4850-4310-9b6e-12abd37b5b47";
    private IModuleService moduleService;
    private List<Integer> testmoduleIds;
    private List<Module> testModules;
    private IModuleRepository repository;
    private Module newModule = new Module("TestMS1", "Test Module SA", "STA");


    @Before
    public void setUp() throws SQLException {
        newModule.setUuid(uuid);
        moduleService = new ModuleService(applicationManager);
        testmoduleIds = new ArrayList<>();
        testModules = TestData.getTestModules();
        repository = new ModuleRepository(applicationManager);
        for (Module m : testModules) {
            Module module = repository.save(m);
            testmoduleIds.add(module.getId());
        }
    }


    @Test
    public void should_sync_Module_new() throws SQLException {
        Module syncedNew = null;
        newModule.addEncounterType(new EncounterType("SSSTestE1", "SSSSWard Survey", "SSSWS"));
        moduleService.syncModule(newModule);

        syncedNew = repository.findbySyncId(newModule.getUuid());
        testmoduleIds.add(syncedNew.getId());
        Assert.assertNotNull(syncedNew);

        for (Module u : repository.getAll()) {
            System.out.println(u);
            for (EncounterType e : u.getEncounterTypes()) {
                System.out.println(">>>  " + e);
            }
        }
    }


    @Test
    public void should_sync_Module_existing() throws SQLException {
        String display = "7007";
        Module module = repository.findByName("TestM1");
        module.setDisplay(display);
        module.getEncounterTypesList().get(0).setDisplay("xxxxxxxxxxxx");
        moduleService.syncModule(module);

        Module syncedUser = repository.findbySyncId(module.getUuid());

        Assert.assertNotNull(syncedUser);
        Assert.assertEquals("7007", syncedUser.getDisplay());

        for (Module u : repository.getAll()) {
            System.out.println(u);
            for (EncounterType e : u.getEncounterTypes()) {
                System.out.println(">>>  " + e);
            }
        }
    }


    @After
    public void tearDown() throws SQLException {

        for (int userid : testmoduleIds) {
            repository.delete(userid);
        }
    }
}