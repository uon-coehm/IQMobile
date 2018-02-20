package com.thepalladiumgroup.iqm.core.data;

import com.thepalladiumgroup.iqm.core.model.Lookup;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/19]
 */
public interface ILookupRepository extends IRepository<Lookup> {
    Lookup getByCode(int iqcareid) throws SQLException;

    List<Lookup> getAllLookups() throws SQLException;
    List<Lookup> getAllByCode(int code) throws SQLException;
}
