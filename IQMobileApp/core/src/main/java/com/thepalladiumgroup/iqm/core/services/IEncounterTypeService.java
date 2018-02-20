package com.thepalladiumgroup.iqm.core.services;

import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.MConcept;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/26]
 */
public interface IEncounterTypeService {
    List<EncounterType> loadEncounterTypes(int moduleid) throws SQLException;
    EncounterType getById(int id) throws SQLException;

    EncounterType getInfo(int id) throws SQLException;

    EncounterType getByModule(int id) throws SQLException;
    List<EncounterType> getByModuleId(int id) throws SQLException;
    void syncEncounterType(EncounterType encounterType) throws SQLException;
    List<MConcept> getConceptsByEncounterType(int encounterTypeid) throws SQLException;
}
