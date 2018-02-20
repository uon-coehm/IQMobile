package com.thepalladiumgroup.iqm.core.services;

import com.thepalladiumgroup.iqm.core.model.Lookup;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/18]
 */
public interface ILookupService {
    void ClearAll() throws SQLException;

    Lookup getLookupByCode(int codeid) throws SQLException;

    Lookup getLookupIQcareId(int id) throws SQLException;
    void syncLookup(Lookup lookup) throws SQLException;

    List<Lookup> getAllLookups() throws SQLException;
    List<Lookup> getLookupsByCodeId(int codeid) throws SQLException;
}
