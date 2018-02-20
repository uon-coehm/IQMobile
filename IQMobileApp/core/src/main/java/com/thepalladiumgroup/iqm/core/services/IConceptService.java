package com.thepalladiumgroup.iqm.core.services;

import com.thepalladiumgroup.iqm.core.model.MConcept;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/26]
 */
public interface IConceptService {
    void ClearAll() throws SQLException;

    List<MConcept> loadConcepts(int encounterTypeId) throws SQLException;
    void syncConcept(MConcept concept) throws SQLException;


    MConcept findByIQCareId(int id) throws SQLException;

    List<MConcept> getByEncounterTypeId(int id) throws SQLException;
    List<MConcept> getAllConcepts() throws SQLException;
}