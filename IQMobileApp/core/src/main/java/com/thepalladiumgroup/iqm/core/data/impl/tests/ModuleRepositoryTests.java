package com.thepalladiumgroup.iqm.core.data.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IModuleRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ModuleRepository;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Module;
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
public class ModuleRepositoryTests extends BaseCoreTest {

    private IModuleRepository repository;
    private List<Integer> testmoduleIds;

    @Before
    public void setUp() throws SQLException {
        testmoduleIds = new ArrayList<>();
        repository = new ModuleRepository(applicationManager);
        for (Module module : TestData.getTestModules()) {
            Module newModule = repository.save(module);
            testmoduleIds.add(newModule.getId());
        }
    }

    @Test
    public void should_save_module_with_encounter_types() throws SQLException {
        Module htcmodule = new Module("KNHHTC", "H Testing", "HTS");
        htcmodule.addEncounterType(new EncounterType("KNHHTCFomr", "HTS Form", "HFRM"));
        Module savedModule = repository.save(htcmodule);
        testmoduleIds.add(savedModule.getId());

        Assert.assertEquals("HTS", savedModule.getDisplayshort());
        Assert.assertEquals("HFRM", savedModule.getEncounterTypesList().get(0).getDisplayshort());

        System.out.println("Module:" + savedModule.toString());
        System.out.println("-------------------------------------------------");
        for (EncounterType e : savedModule.getEncounterTypes()) {
            System.out.println("    Encounter:" + e.toString());
        }
    }

    @Test
    public void should_get_modules_with_encounter_types() throws SQLException {
        List<Module> modules = repository.getAll();
        Assert.assertTrue(modules.size() > 0);
        Assert.assertTrue(modules.get(0).getEncounterTypes().size() > 0);

        System.out.println("Total Modules:" + modules.size());

        for (Module m : modules) {
            System.out.println("Module:" + m.toString());
            System.out.println("-------------------------------------------------");
            for (EncounterType e : m.getEncounterTypes()) {
                System.out.println("    Encounter:" + e.toString());
            }
            System.out.println("__________________________________________________");
        }
    }

    @Test
    public void should_find_name() throws SQLException {
        Module module = repository.findByName("TestM1");
        Assert.assertEquals("TA", module.getDisplayshort());
        System.out.println("Module:" + module.toString());
        List<EncounterType> encounterTypes = new ArrayList<>(module.getEncounterTypes());
        EncounterType encounterType = encounterTypes.get(0);
        Assert.assertEquals("WS", encounterType.getDisplayshort());
        System.out.println("Encounter:" + encounterType.toString());
    }

    @Test
    public void should_getdefualt() throws SQLException {
        Module module = repository.findByName("TestM1");
        Assert.assertEquals("TA", module.getDisplayshort());
        System.out.println("Module:" + module.toString());
        List<EncounterType> encounterTypes = new ArrayList<>(module.getEncounterTypes());
        EncounterType encounterType = encounterTypes.get(0);
        Assert.assertEquals("WS", encounterType.getDisplayshort());
        System.out.println("Encounter:" + encounterType.toString());
    }


    @After
    public void tearDown() throws SQLException {
        for (int moduleid : testmoduleIds) {
            repository.delete(moduleid);
        }
    }
}
