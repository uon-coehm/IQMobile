package com.thepalladiumgroup.iqm.core.data.impl;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.QueryBuilder;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.ILookupRepository;
import com.thepalladiumgroup.iqm.core.model.Lookup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/18]
 */
public class LookupRepository extends BaseRepository<Lookup> implements ILookupRepository {

    public LookupRepository(IApplicationManager applicationManager) {
        super(applicationManager);
    }

    @Override
    public Lookup getByCode(int iqcareid) throws SQLException {
        List<Lookup> results = getAllbyfield("codeid", iqcareid);
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public List<Lookup> getAllLookups() throws SQLException {
        return getAllByCode(-1);
    }

    @Override
    public List<Lookup> getAllByCode(int code) throws SQLException {

        SLF_LOGGER.debug(">> Lookups with iterator >>>");

        List<Lookup> lookups = new ArrayList<>();

        QueryBuilder<Lookup, Integer> qb = entityDao.queryBuilder();

        if (code > -1) {
            qb.where().eq("codeid", code);
        }

        CloseableIterator<Lookup> iterator = entityDao.iterator(qb.prepare());
        try {
            while (iterator.hasNext()) {
                Lookup account = iterator.next();
                lookups.add(account);
            }
        } finally {
            // close it at the end to close underlying SQL statement
            iterator.close();
        }


        return lookups;


    }
}
