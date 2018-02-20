package com.thepalladiumgroup.iqm.core.data.impl;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IEncounterTypeRepository;
import com.thepalladiumgroup.iqm.core.model.EncounterType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/25]
 */
public class EncounterTypeRepository extends BaseRepository<EncounterType> implements IEncounterTypeRepository {
    private static final Logger LOG = LoggerFactory.getLogger(EncounterTypeRepository.class);
    public EncounterTypeRepository(IApplicationManager applicationManager) {
        super(applicationManager);
    }

    @Override
    public List<EncounterType> loadAllEncounterTypes(int moduleid) throws SQLException {
        SLF_LOGGER.debug(">> EncounterTypes with iterator >>>");
        List<String> cols = new ArrayList<>();

        /*
encountertype.id,
encountertype.display,
encountertype.displayshort,
encountertype.iqcareid,
encountertype.name,
encountertype.updatedate,
encountertype.uuid,
encountertype.syncstate,
encountertype.userid
encountertype.module_id,
         */
        cols.add("id");
        cols.add("display");
        cols.add("displayshort");
        cols.add("iqcareid");
        cols.add("name");
        cols.add("updatedate");
        cols.add("uuid");
        cols.add("syncstate");
        cols.add("userid");


        List<EncounterType> encounterTypes = new ArrayList<>();

        QueryBuilder<EncounterType, Integer> qb = entityDao.queryBuilder();
        qb.selectColumns(cols);
        qb.where().eq("module_id", moduleid);


        CloseableIterator<EncounterType> iterator = entityDao.iterator(qb.prepare());
        try {
            while (iterator.hasNext()) {
                EncounterType account = iterator.next();
                encounterTypes.add(account);
            }
        } finally {
            // close it at the end to close underlying SQL statement
            iterator.close();
        }


        return encounterTypes;
    }

    @Override
    public List<EncounterType> getAllByModule(int id) throws SQLException {
     /*
SELECT
encountertype.id,
encountertype.display,
encountertype.displayshort,
encountertype.name,

encountertype.iqcareid

encountertype.module_id,
FROM
encountertype

      */

        SLF_LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> with iterator >>>>>>>>>>>>>>>>>>>");

        List<EncounterType> encounterTypes = new ArrayList<>();

        List<String> cols = new ArrayList<>();

        cols.add("id");
        cols.add("display");
        cols.add("displayshort");
        cols.add("iqcareid");
        cols.add("name");


        QueryBuilder<EncounterType, Integer> qb = entityDao.queryBuilder()
                .selectColumns(cols);

        qb.where().eq("module_id", id);


        CloseableIterator<EncounterType> iterator = entityDao.iterator(qb.prepare());
        try {
            while (iterator.hasNext()) {
                EncounterType account = iterator.next();
                encounterTypes.add(account);
            }
        } finally {
            // close it at the end to close underlying SQL statement
            iterator.close();
        }


        return encounterTypes;

    }

    @Override
    public EncounterType getByModule(int id) throws SQLException {
        List<EncounterType> encounterTypes = getAllByModule(id);
        if (encounterTypes.size() > 0) {
            return encounterTypes.get(0);
        }
        return null;
    }

    @Override
    public EncounterType getInfo(int id) throws SQLException {
        SLF_LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> with iterator >>>>>>>>>>>>>>>>>>>");

        List<EncounterType> encounterTypes = new ArrayList<>();

        List<String> cols = new ArrayList<>();

        cols.add("id");
        cols.add("display");
        cols.add("displayshort");
        cols.add("iqcareid");
        cols.add("name");


        QueryBuilder<EncounterType, Integer> qb = entityDao.queryBuilder()
                .selectColumns(cols);

        qb.where().eq("id", id);


        CloseableIterator<EncounterType> iterator = entityDao.iterator(qb.prepare());
        try {
            while (iterator.hasNext()) {
                EncounterType account = iterator.next();
                encounterTypes.add(account);
            }
        } finally {
            // close it at the end to close underlying SQL statement
            iterator.close();
        }

        if (encounterTypes.size() > 0) {
            return encounterTypes.get(0);
        }
        return null;
    }

    @Override
    public void updateInfo(EncounterType encounterType) throws SQLException {

        /*
        SELECT
encountertype.display,
encountertype.displayshort,
encountertype.module_id,
encountertype.name,
encountertype.updatedate,
encountertype.uuid,
encountertype.id,
encountertype.iqcareid,
encountertype.syncstate,
encountertype.userid
FROM
encountertype

         */
        UpdateBuilder<EncounterType, Integer> updateBuilder =
                entityDao.updateBuilder();

        updateBuilder.updateColumnValue("display", encounterType.getDisplay());
        updateBuilder.updateColumnValue("displayshort", encounterType.getDisplayshort());
        updateBuilder.updateColumnValue("name", encounterType.getName());
        updateBuilder.updateColumnValue("updatedate", encounterType.getUpdatedate());
        updateBuilder.updateColumnValue("iqcareid", encounterType.getIqcareid());
        updateBuilder.updateColumnValue("syncstate", encounterType.getSyncstate());
        updateBuilder.updateColumnValue("userid", encounterType.getUserid());
        updateBuilder.updateColumnValue("module_id", encounterType.getModule().getId());
        //updateBuilder.updateColumnValue("uuid", "none");
        updateBuilder.where().eq("id", encounterType.getId());

        LOG.debug("running module.encounterType UPDATE...");
        updateBuilder.update();
        LOG.debug("module.encounterType UPDATED!");
    }
}
