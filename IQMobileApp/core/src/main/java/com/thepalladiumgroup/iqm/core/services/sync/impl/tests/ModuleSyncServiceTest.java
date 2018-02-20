package com.thepalladiumgroup.iqm.core.services.sync.impl.tests;

import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Module;
import com.thepalladiumgroup.iqm.core.services.IModuleService;
import com.thepalladiumgroup.iqm.core.services.impl.ModuleService;
import com.thepalladiumgroup.iqm.core.services.sync.IModuleSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.impl.ModuleSyncService;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public class ModuleSyncServiceTest extends BaseCoreTest {

    private IModuleSyncService moduleSyncService;
    private IModuleService moduleService;

    @Before
    public void setUp() throws SQLException {
        moduleSyncService = new ModuleSyncService(applicationManager);
        moduleService = new ModuleService(applicationManager);
    }

    @Test
    public void should_initialize() throws Exception {
        moduleSyncService.initialze();
        Assert.assertNotNull(moduleSyncService.getServer());
        Assert.assertNotNull(moduleSyncService.getAdapter());

        System.out.println(moduleSyncService.getServer());
    }

    @Test
    public void should_read_all_modules() throws Exception {
        moduleSyncService.initialze();
        List<Module> modules = moduleSyncService.readAllModules();
        Assert.assertNotNull(modules);
        for (Module u : modules) {
            System.out.println(u);
            Assert.assertNotNull(u.getEncounterTypes());
            for (EncounterType e : u.getEncounterTypes()) {
                System.out.println("---------");
                System.out.println(">>>>> " + e);
                System.out.println("---------");
            }
        }
    }

    @Test
    public void should_sync_all_modules() throws Exception {
        moduleSyncService.initialze();
        List<Module> modules = moduleSyncService.readAllModules();
        Assert.assertNotNull(modules);

        for (Module lookup : modules) {
            moduleService.syncModule(lookup);
        }

        Module module = moduleService.findByIQCareId(5);
        Assert.assertNotNull(module);
        Assert.assertEquals("HTSM", module.getDisplayshort());
        System.out.println(module);

        for (EncounterType e : module.getEncounterTypesList()) {
            Assert.assertNotNull(e);
            Assert.assertEquals("HTSF", e.getDisplayshort());
            System.out.println(e);
        }


    }
}