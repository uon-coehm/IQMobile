package com.thepalladiumgroup.iqm.core.services.sync;

import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.services.sync.endpoints.IConceptWapi;

import java.io.IOException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public interface IConceptSyncService extends ISyncService {
    IConceptWapi getConceptWapi();

    List<MConcept> readAllConcepts(int encounterTypeId) throws IOException;
}
