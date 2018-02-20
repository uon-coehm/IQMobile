package com.thepalladiumgroup.iqm.core.data;

import com.thepalladiumgroup.iqm.core.model.EncounterType;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/25]
 */
public interface IEncounterTypeRepository extends IRepository<EncounterType> {
    List<EncounterType> loadAllEncounterTypes(int moduleid) throws SQLException;
    List<EncounterType> getAllByModule(int id) throws SQLException;

    EncounterType getByModule(int id) throws SQLException;

    EncounterType getInfo(int id) throws SQLException;

    void updateInfo(EncounterType encounterType) throws SQLException;
}
