package com.thepalladiumgroup.iqm.core.data;

import com.thepalladiumgroup.iqm.core.model.MConcept;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/25]
 */
public interface IConceptRepository extends IRepository<MConcept> {
    List<MConcept> getAllIorder(int encountertypeId);

    List<MConcept> getByEnctounterType(int encountertypeId) throws SQLException;
    List<MConcept> getAllConcepts() throws SQLException;
}

