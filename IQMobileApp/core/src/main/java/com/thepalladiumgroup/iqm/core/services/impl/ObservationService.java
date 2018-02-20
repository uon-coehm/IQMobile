package com.thepalladiumgroup.iqm.core.services.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IObservationRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ObsevationRepository;
import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.Observation;
import com.thepalladiumgroup.iqm.core.services.IEncounterService;
import com.thepalladiumgroup.iqm.core.services.IObservationService;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/27]
 */
public class ObservationService implements IObservationService {
    private final IObservationRepository observationRepository;
    private final IApplicationManager applicationManager;
    private IEncounterService encounterService;

    public ObservationService(IApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
        this.observationRepository = new ObsevationRepository(applicationManager);
        encounterService = new EncounterService(applicationManager);
    }

    @Override
    public Observation getById(int id) throws SQLException {
        return observationRepository.find(id);
    }

    @Override
    public Observation getByEncounterAndConcept(Encounter encounter, MConcept concept) throws SQLException {
        return observationRepository.getByEncounterAndConcept(encounter, concept);
    }

    @Override
    public Observation save(Observation observation) throws SQLException {
        Observation obs = getById(observation.getId());
        if (obs == null) {
            obs = observationRepository.save(observation);
        } else {
            if (!obs.getObsvalueString().equals(observation.getObsvalueString())) {
                clearDirtyObs(obs);
                observation.prepareUpdate();
                obs = observationRepository.update(observation);
            }
        }
        return obs;
    }

    @Override
    public void clearDirtyObs(Observation obs) throws SQLException {

        if (obs.getmConcept().isParent() || obs.getmConcept().hasJumpSkip()) {
            int count = 0;
            Encounter encounter = obs.getEncounter();
            List<Observation> savedObs = encounter.getDirtyObs(obs, encounter);
            for (Observation o : savedObs) {
                observationRepository.delete(o.getId());
                count++;
            }
            if (count > 0) {
                encounterService.markAsComplete(encounter, false);
            }
        }

    }
}
