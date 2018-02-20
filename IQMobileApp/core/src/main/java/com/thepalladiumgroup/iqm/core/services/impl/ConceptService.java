package com.thepalladiumgroup.iqm.core.services.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IConceptRepository;
import com.thepalladiumgroup.iqm.core.data.IEncounterTypeRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ConceptRepository;
import com.thepalladiumgroup.iqm.core.data.impl.EncounterTypeRepository;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.services.IConceptService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/27]
 */
public class ConceptService implements IConceptService {

    private final IConceptRepository conceptRepository;
    private final IEncounterTypeRepository encounterTypeRepository;
    private final IApplicationManager applicationManager;
    private List<MConcept> exisitingConcepts = new ArrayList<>();

    public ConceptService(IApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
        this.conceptRepository = new ConceptRepository(applicationManager);
        encounterTypeRepository = new EncounterTypeRepository(applicationManager);
    }

    @Override
    public void ClearAll() throws SQLException {
        conceptRepository.deleteAll();
    }

    @Override
    public List<MConcept> loadConcepts(int encounterTypeId) throws SQLException {
        exisitingConcepts = conceptRepository.getByEnctounterType(encounterTypeId);
        return exisitingConcepts;
    }

    @Override
    public void syncConcept(MConcept concept) throws SQLException {
        MConcept subject = checkMConceptExsist(concept);
        if (subject != null) {
            if (concept.getUuid().equalsIgnoreCase(subject.getUuid())) {
                concept.setId(subject.getId());
                conceptRepository.updateOnly(concept);

            } else {
                conceptRepository.saveOnly(concept);
            }

        } else {
            conceptRepository.saveOnly(concept);

            //conceptRepository.updateOnly(concept);
        }
    }

    @Override
    public MConcept findByIQCareId(int id) throws SQLException {
        return conceptRepository.findbyfield("iqcareid", id);
    }

    @Override
    public List<MConcept> getByEncounterTypeId(int id) throws SQLException {
        List<MConcept> concepts = null;
        concepts = conceptRepository.getByEnctounterType(id);
        Collections.sort(concepts);
        return concepts;
    }

    private MConcept checkMConceptExsist(MConcept find) {
        MConcept found = null;
        for (MConcept u : exisitingConcepts) {
            if (find.equals(u)) {
                found = u;
                break;
            }
        }
        return found;
    }

    public List<MConcept> getAllConcepts() throws SQLException {
        List<MConcept> concepts = null;
        concepts = conceptRepository.getAllConcepts();
        Collections.sort(concepts);
        return concepts;
    }
}