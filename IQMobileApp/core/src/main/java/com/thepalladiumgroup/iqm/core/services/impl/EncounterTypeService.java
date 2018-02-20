package com.thepalladiumgroup.iqm.core.services.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IConceptRepository;
import com.thepalladiumgroup.iqm.core.data.IEncounterTypeRepository;
import com.thepalladiumgroup.iqm.core.data.ILookupRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ConceptRepository;
import com.thepalladiumgroup.iqm.core.data.impl.EncounterTypeRepository;
import com.thepalladiumgroup.iqm.core.data.impl.LookupRepository;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.services.IEncounterTypeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/26]
 */
public class EncounterTypeService implements IEncounterTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(EncounterTypeService.class);
    private final IEncounterTypeRepository encounterTypeRepository;
    private final IConceptRepository conceptRepository;
    private final IApplicationManager applicationManager;
    private final ILookupRepository lookupRepository;
    private List<EncounterType> existingEncounterTypes = new ArrayList<>();

    public EncounterTypeService(IApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
        this.encounterTypeRepository = new EncounterTypeRepository(applicationManager);
        this.conceptRepository = new ConceptRepository(applicationManager);
        this.lookupRepository = new LookupRepository(applicationManager);
    }

    @Override
    public List<EncounterType> loadEncounterTypes(int moduleid) throws SQLException {
        existingEncounterTypes = encounterTypeRepository.loadAllEncounterTypes(moduleid);
        return existingEncounterTypes;
    }

    @Override
    public EncounterType getById(int id) throws SQLException {
        EncounterType encounterType = encounterTypeRepository.find(id);
        return encounterType;
    }

    @Override
    public EncounterType getInfo(int id) throws SQLException {
        EncounterType encounterType = encounterTypeRepository.getInfo(id);
        return encounterType;
    }

    @Override
    public EncounterType getByModule(int id) throws SQLException {
        return encounterTypeRepository.getByModule(id);
    }

    @Override
    public List<EncounterType> getByModuleId(int id) throws SQLException {
        return encounterTypeRepository.getAllByModule(id);
    }

    @Override
    public void syncEncounterType(EncounterType encounterType) throws SQLException {
        EncounterType subject = checkEncounterTypeExsist(encounterType);
        LOG.debug("searched module.encounterType!");
        if (encounterType.equals(subject)) {
            LOG.debug("updating module.encounterType...");
            encounterType.setId(subject.getId());
            LOG.debug("updating module.encounterType [Id-set]...");
            encounterTypeRepository.updateInfo(encounterType);
            LOG.debug("updated module.encounterType!");
        } else {
            encounterTypeRepository.save(encounterType);
            LOG.debug("new module.encounterType!");
        }
    }

    @Override
    public List<MConcept> getConceptsByEncounterType(int encounterTypeid) throws SQLException {
        List<MConcept> conceptList = new ArrayList<>();
        EncounterType encounterType = encounterTypeRepository.find(encounterTypeid);
        List<MConcept> concepts = encounterType.getConceptsList();

        for (MConcept concept : concepts) {
            if (concept.hasLookups()) {
                List<Lookup> lookups = lookupRepository.getAllbyfield("codeid", concept.getLookupcodeid());
                concept.addLookups(lookups);
            }
            conceptList.add(concept);
        }
        Collections.sort(conceptList);
        return conceptList;
    }

    private EncounterType checkEncounterTypeExsist(EncounterType find) {
        EncounterType found = null;
        for (EncounterType u : existingEncounterTypes) {
            if (find.equals(u)) {
                found = u;
                break;
            }
        }
        return found;
    }
}
