package com.thepalladiumgroup.iqm.core.data;

import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.Observation;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/27]
 */
public interface IObservationRepository extends IRepository<Observation> {

    Observation getByEncounterAndConcept(Encounter encounter, MConcept concept) throws SQLException;

    List<Observation> getByEncounterConcepts(int encounterId, List<Integer> conceptIds) throws SQLException;

    List<Observation> getByEncounter(int id) throws SQLException;


    void delete(List<Observation> observations);

}
