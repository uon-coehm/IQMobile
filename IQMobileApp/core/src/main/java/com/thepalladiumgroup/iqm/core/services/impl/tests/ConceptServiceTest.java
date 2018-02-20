package com.thepalladiumgroup.iqm.core.services.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IConceptRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ConceptRepository;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.MDataType;
import com.thepalladiumgroup.iqm.core.services.IConceptService;
import com.thepalladiumgroup.iqm.core.services.impl.ConceptService;
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
public class ConceptServiceTest extends BaseCoreTest {

    private IConceptService conceptService;
    private MConcept newConcept = MConcept.CreateConcept("QXX", "QXX", MDataType.YESNO, 1, 0.8);
    private IConceptRepository conceptRepository;
    private List<Integer> testconceptsIds;

    @Before
    public void setUp() throws SQLException {
        testconceptsIds = new ArrayList<>();
        conceptService = new ConceptService(applicationManager);
        newConcept.setUuid("6488919e-a723-4ac0-a2ba-78fb1aadfadc");
        conceptRepository = new ConceptRepository(applicationManager);

        for (MConcept l : TestData.getConcepts()) {
            MConcept concept = conceptRepository.save(l);
            testconceptsIds.add(concept.getId());
        }
    }

    @Test
    public void should_Get_Concepts_ByEncounter_Id() throws Exception {
        List<MConcept> concepts = conceptService.getByEncounterTypeId(1);
        Assert.assertTrue(concepts.size() > 0);
        for (MConcept l : concepts) {
            System.out.println(l);
        }
    }


    @Test
    public void should_sync_Concept_new() throws SQLException {
        MConcept syncedNew = null;

        conceptService.syncConcept(newConcept);

        syncedNew = conceptRepository.findbySyncId(newConcept.getUuid());
        testconceptsIds.add(syncedNew.getId());
        Assert.assertNotNull(syncedNew);
        System.out.println(syncedNew);

    }


    @Test
    public void should_sync_Concept_existing() throws SQLException {
        String display = "FAKE LOOKUP";
        MConcept concept = conceptRepository.findbySyncId("ee33e09f-3d6b-48c9-8875-106cd16c5b6c");
        Assert.assertNotNull(concept);
        concept.setDisplay(display);

        conceptService.syncConcept(concept);

        MConcept syncedConcept = conceptRepository.findbySyncId(concept.getUuid());

        Assert.assertNotNull(syncedConcept);
        Assert.assertEquals("FAKE LOOKUP", syncedConcept.getDisplay());

        System.out.println(syncedConcept);
    }


    @After
    public void tearDown() throws SQLException {

        for (int cid : testconceptsIds) {
            conceptRepository.delete(cid);
        }
    }
}