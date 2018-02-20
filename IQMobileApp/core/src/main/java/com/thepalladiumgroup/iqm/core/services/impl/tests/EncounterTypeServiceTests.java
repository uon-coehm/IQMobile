package com.thepalladiumgroup.iqm.core.services.impl.tests;

import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.services.IEncounterTypeService;
import com.thepalladiumgroup.iqm.core.services.IModuleService;
import com.thepalladiumgroup.iqm.core.services.impl.EncounterTypeService;
import com.thepalladiumgroup.iqm.core.services.impl.ModuleService;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/27]
 */
public class EncounterTypeServiceTests extends BaseCoreTest {
    private EncounterType defEncounterType;
    private IEncounterTypeService encounterTypeService;

    @Before
    public void setUp() throws SQLException {
        encounterTypeService = new EncounterTypeService(applicationManager);
        IModuleService service = new ModuleService(applicationManager);
        defEncounterType = service.getDefault().getEncounterTypesList().get(0);

    }

    @Test
    public void should_get_by_id() throws SQLException {
        EncounterType encounterType = encounterTypeService.getById(defEncounterType.getId());
        Assert.assertNotNull(encounterType);
        System.out.println(encounterType);
    }

    @Test
    public void should_load_Concepts_With_lookups() throws SQLException {
        MConcept conceptLookup = null;
        List<MConcept> concepts = encounterTypeService.getConceptsByEncounterType(defEncounterType.getId());
        for (MConcept c : concepts) {
            if (c.hasLookups()) {
                conceptLookup = c;
                break;
            }

        }
        Assert.assertTrue(concepts.size() > 0);
        Assert.assertTrue(conceptLookup.getConceptLookups().size() > 0);
        System.out.println(conceptLookup);
        for (Lookup l : conceptLookup.getConceptLookups()) {
            System.out.println(l.printInfo());
        }
    }
}
