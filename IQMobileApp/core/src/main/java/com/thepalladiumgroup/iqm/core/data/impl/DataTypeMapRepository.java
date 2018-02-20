package com.thepalladiumgroup.iqm.core.data.impl;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.QueryBuilder;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IDataTypeMapRepository;
import com.thepalladiumgroup.iqm.core.model.MDataType;
import com.thepalladiumgroup.iqm.core.model.MDataTypeMap;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/25]
 */
public class DataTypeMapRepository extends BaseRepository<MDataTypeMap> implements IDataTypeMapRepository {

    public DataTypeMapRepository(IApplicationManager applicationManager) {
        super(applicationManager);
    }

    @Override
    public MDataTypeMap getByDataType(MDataType datatype) throws SQLException {
        return findbyfield("dataType", datatype);
    }

    @Override
    public MDataTypeMap getByIQDataType(String iqType) throws SQLException {
        return findbyfield("iqType", iqType);
    }

    @Override
    public List<MDataTypeMap> getAllInfo() throws SQLException {

        SLF_LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> with iterator >>>>>>>>>>>>>>>>>>>");

        List<MDataTypeMap> mDataTypeMaps = new ArrayList<>();

        List<String> cols = new ArrayList<>();

        cols.add("id");
        cols.add("dataType");
        cols.add("iqType");
        cols.add("iqcareid");

        QueryBuilder<MDataTypeMap, Integer> qb = entityDao.queryBuilder()
                .selectColumns(cols);


        CloseableIterator<MDataTypeMap> iterator = entityDao.iterator(qb.prepare());
        try {
            while (iterator.hasNext()) {
                MDataTypeMap account = iterator.next();
                mDataTypeMaps.add(account);
            }
        } finally {
            // close it at the end to close underlying SQL statement
            iterator.close();
        }


        return mDataTypeMaps;

    }

    @Override
    public QueryBuilder<MDataTypeMap, Integer> buidlerMDataTypeMap() {
        List<String> cols = new ArrayList<>();

        cols.add("id");
        cols.add("dataType");
        cols.add("iqType");
        cols.add("iqcareid");

        QueryBuilder<MDataTypeMap, Integer> qb = entityDao.queryBuilder()
                .selectColumns(cols);

        return qb;
    }
}
