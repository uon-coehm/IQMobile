package com.thepalladiumgroup.iqm.core.data.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IConceptRepository;
import com.thepalladiumgroup.iqm.core.data.IEncounterTypeRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ConceptRepository;
import com.thepalladiumgroup.iqm.core.data.impl.EncounterTypeRepository;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.MConcept;
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
 * Created by Koske Kimutai [2016/04/25]
 */
public class ConceptRepositoryTests extends BaseCoreTest {
    private EncounterType encounterType;
    private IConceptRepository repository;
    private List<Integer> testConceptIds = new ArrayList<>();


    @Before
    public void setUp() throws SQLException {
        IEncounterTypeRepository encounterTypeRepository = new EncounterTypeRepository(applicationManager);

        encounterType = encounterTypeRepository.save(TestData.getEncounterTypesA().get(0));
        repository = new ConceptRepository(applicationManager);
        for (MConcept c : TestData.getConcepts()) {
            c.setEncounterType(encounterType);
            MConcept saved = repository.save(c);
            testConceptIds.add(saved.getId());
        }
    }

    @Test
    public void should_get_children() throws SQLException {
        MConcept concept = repository.findbyfield("iqcareid", "2");
        Assert.assertNotNull(concept);
        Assert.assertTrue(concept.getChildrenConceptsList().size() > 0);
        System.out.println(concept.toString());

        List<MConcept> conceptList = concept.getChildrenConceptsList();
        for (MConcept c : conceptList) {
            System.out.println(" >" + c.toString() + " index:" + conceptList.indexOf(c));
        }
    }

    @Test
    public void should_get_with_concept_lookups() throws SQLException {
        MConcept concept = repository.findbyfield("iqcareid", "2");
        Assert.assertNotNull(concept);
        Assert.assertTrue(concept.getChildrenConceptsList().size() > 0);
        System.out.println(concept.toString());

        List<MConcept> conceptList = concept.getChildrenConceptsList();
        for (MConcept c : conceptList) {
            System.out.println(" >" + c.toString() + " index:" + conceptList.indexOf(c));
        }
    }

    @Test
    public void should_get_parent() throws SQLException {
        MConcept concept = repository.findbyfield("iqcareid", "3");
        Assert.assertNotNull(concept);
        Assert.assertNotNull(concept.getParent());
        System.out.println(concept.toString());
        for (MConcept c : concept.getChildrenConceptsList()) {
            System.out.println(" >" + c.toString());
        }
    }

    @Test
    public void should_save_with_mdatatype() throws SQLException {
        MConcept concept = TestData.getNewConcepts().get(0);
        MConcept saved = repository.save(concept);
        testConceptIds.add(saved.getId());
        Assert.assertTrue(saved.getDataTypeMap().getId() > 0);
        System.out.println("Saved new concept:" + saved.toString());
    }

    @Test
    public void should_save_with_iqdatatype() throws SQLException {
        MConcept concept = TestData.getNewConcepts().get(1);
        MConcept saved = repository.save(concept);
        testConceptIds.add(saved.getId());
        Assert.assertTrue(saved.getDataTypeMap().getId() > 0);
        System.out.println("Saved new concept:" + saved.toString());
    }

    @After
    public void tearDown() throws SQLException {
        new EncounterTypeRepository(applicationManager).delete(encounterType.getId());
        for (int userid : testConceptIds) {
            repository.delete(userid);
        }
    }
}
