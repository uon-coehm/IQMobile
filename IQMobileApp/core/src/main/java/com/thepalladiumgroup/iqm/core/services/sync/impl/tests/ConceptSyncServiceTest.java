package com.thepalladiumgroup.iqm.core.services.sync.impl.tests;

import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.services.IConceptService;
import com.thepalladiumgroup.iqm.core.services.IModuleService;
import com.thepalladiumgroup.iqm.core.services.impl.ConceptService;
import com.thepalladiumgroup.iqm.core.services.impl.ModuleService;
import com.thepalladiumgroup.iqm.core.services.sync.IConceptSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.impl.ConceptSyncService;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public class ConceptSyncServiceTest extends BaseCoreTest {

    private IConceptSyncService conceptSyncService;
    private IConceptService conceptService;
    private IModuleService moduleService;
    private EncounterType encounterType;
    private int eid;

    @Before
    public void setUp() throws SQLException {
        conceptService = new ConceptService(applicationManager);
        conceptSyncService = new ConceptSyncService(applicationManager);
        moduleService = new ModuleService(applicationManager);
        encounterType = moduleService.getDefault().getEncounterTypesList().get(0);
        eid = encounterType.getId();
    }

    @Test
    public void should_initialize() throws Exception {
        conceptSyncService.initialze();
        Assert.assertNotNull(conceptSyncService.getServer());
        Assert.assertNotNull(conceptSyncService.getAdapter());
        System.out.println(conceptSyncService.getServer());
    }

    @Test
    public void should_read_all_concepts() throws Exception {
        Assert.assertTrue(eid > 0);
        conceptSyncService.initialze();
        List<MConcept> concepts = conceptSyncService.readAllConcepts(eid);
        Assert.assertNotNull(concepts);
        for (MConcept u : concepts) {
            System.out.println(u);
        }
    }

    @Test
    public void should_sync_all_concepts() throws Exception {

        Assert.assertTrue(eid > 0);
        conceptSyncService.initialze();
        List<MConcept> concepts = conceptSyncService.readAllConcepts(eid);
        Assert.assertNotNull(concepts);

        for (MConcept lookup : concepts) {
            lookup.setEncounterType(encounterType);
            conceptService.syncConcept(lookup);
        }

        MConcept knhstaff = conceptService.findByIQCareId(88881765);
        Assert.assertNotNull(knhstaff);
        Assert.assertNotNull(knhstaff.getEncounterType());
        Assert.assertEquals("KNH Staff?", knhstaff.getDisplay());
        Assert.assertEquals(849, knhstaff.getLookupcodeid());
        Assert.assertEquals(1, knhstaff.getEncounterType().getId());
        System.out.println(knhstaff);


        MConcept whendays = conceptService.findByIQCareId(88881688);
        Assert.assertNotNull(whendays);
        Assert.assertNotNull(whendays.getEncounterType());
        Assert.assertEquals("When ? (Days Ago)", whendays.getDisplay());
        Assert.assertEquals(0, whendays.getLookupcodeid());
        Assert.assertEquals("cea88ddf-080c-4733-ae46-fb938c42a2aa", whendays.getUuid());
        System.out.println(whendays);

        MConcept skiptocondition = conceptService.findByIQCareId(8888730);
        Assert.assertNotNull(skiptocondition);
        Assert.assertNotNull(skiptocondition.getEncounterType());
        Assert.assertEquals("4129", skiptocondition.getSkiptocondition());
        System.out.println(skiptocondition);

    }
}