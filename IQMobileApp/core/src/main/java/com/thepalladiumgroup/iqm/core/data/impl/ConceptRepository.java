package com.thepalladiumgroup.iqm.core.data.impl;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.QueryBuilder;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IConceptRepository;
import com.thepalladiumgroup.iqm.core.data.IDataTypeMapRepository;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.MDataTypeMap;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/25]
 */
public class ConceptRepository extends BaseRepository<MConcept> implements IConceptRepository {
    private final IDataTypeMapRepository dataTypeMapRepository;


    public ConceptRepository(IApplicationManager applicationManager) {
        super(applicationManager);
        dataTypeMapRepository = new DataTypeMapRepository(applicationManager);
    }

    @Override
    public MConcept save(MConcept entity) throws SQLException {

        MConcept concept = null;
        if (entity.getDataType() != null) {
            MDataTypeMap map = dataTypeMapRepository.getByDataType(entity.getDataType());
            entity.setDataTypeMap(map);
        }

        if (entity.getIqDataType() != null) {
            if (entity.getIqDataType().length() > 0) {
                MDataTypeMap map = dataTypeMapRepository.getByIQDataType(entity.getIqDataType());
                entity.setDataTypeMap(map);
            }
        }

        concept = super.save(entity);
        updateParent(concept);
        return concept;
    }

    private void updateParent(MConcept concept) {
        MConcept parent = null;
        if (concept.getParentIqcareId() > 0) {
            if (concept.getParent() == null) {
                try {
                    parent = findbyfield("iqcareid", concept.getParentIqcareId());
                    if (parent != null) {
                        concept.setParent(parent);
                        update(concept);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<MConcept> getAllIorder(int encountertypeId) {
        return null;
    }
    @Override
    public List<MConcept> getAllConcepts() throws SQLException {
        List<MConcept> concepts = new ArrayList<>();

        QueryBuilder<MConcept, Integer> qb = entityDao.queryBuilder();
        CloseableIterator<MConcept> iterator = entityDao.iterator(qb.prepare());

        try {
            while (iterator.hasNext()) {
                MConcept account = iterator.next();
                concepts.add(account);
            }
        } finally {
            // close it at the end to close underlying SQL statement
            iterator.close();
        }

        return concepts;
    }

    @Override
    public List<MConcept> getByEnctounterType(int encountertypeId) throws SQLException {

        SLF_LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> CONCEPTS >>>>>>>>>>>>>>>>>>>");

        List<String> cols = new ArrayList<>();
        List<MConcept> concepts = new ArrayList<>();

        QueryBuilder<MDataTypeMap, Integer> qbd = dataTypeMapRepository.buidlerMDataTypeMap();

        cols.add("dataTypeMap_id");
        cols.add("description");
        cols.add("display");
        cols.add("parent_id");
        cols.add("parentcondition");
        cols.add("rank");
        cols.add("lookupcodeid");
        cols.add("parentIqcareId");
        cols.add("patientgender");
        cols.add("required");
        cols.add("uuid");
        cols.add("id");
        cols.add("iqcareid");
        cols.add("autcompute");
        cols.add("autcomputelogic");
        cols.add("skipto");
        cols.add("skiptocondition");
        cols.add("parentconditionchildren");
        cols.add("autcomputeparentchildren");
        cols.add("skiptoparent");
        cols.add("skiptoparentcondition");


        QueryBuilder<MConcept, Integer> qb = entityDao.queryBuilder()
                .selectColumns(cols);
        qb.where().eq("encounterType_id", encountertypeId);


        CloseableIterator<MConcept> iterator = entityDao.iterator(qb.join(qbd).prepare());
        try {
            while (iterator.hasNext()) {
                MConcept account = iterator.next();
                concepts.add(account);
            }
        } finally {
            // close it at the end to close underlying SQL statement
            iterator.close();
        }
        return concepts;
    }

}