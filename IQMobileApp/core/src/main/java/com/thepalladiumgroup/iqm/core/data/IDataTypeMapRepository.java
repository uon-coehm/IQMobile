package com.thepalladiumgroup.iqm.core.data;

import com.j256.ormlite.stmt.QueryBuilder;
import com.thepalladiumgroup.iqm.core.model.MDataType;
import com.thepalladiumgroup.iqm.core.model.MDataTypeMap;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/25]
 */
public interface IDataTypeMapRepository extends IRepository<MDataTypeMap> {
    MDataTypeMap getByDataType(MDataType datatype) throws SQLException;

    MDataTypeMap getByIQDataType(String iqdatatype) throws SQLException;

    List<MDataTypeMap> getAllInfo() throws SQLException;

    QueryBuilder<MDataTypeMap, Integer> buidlerMDataTypeMap();
}
