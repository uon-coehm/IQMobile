package com.thepalladiumgroup.iqm.core.services;

import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.Observation;

import java.sql.SQLException;

/**
 * Created by Koske Kimutai [2016/04/26]
 */
public interface IObservationService {
    Observation getById(int id) throws SQLException;

    Observation getByEncounterAndConcept(Encounter encounter, MConcept concept) throws SQLException;

    Observation save(Observation observation) throws SQLException;

    void clearDirtyObs(Observation obs) throws SQLException;
}
