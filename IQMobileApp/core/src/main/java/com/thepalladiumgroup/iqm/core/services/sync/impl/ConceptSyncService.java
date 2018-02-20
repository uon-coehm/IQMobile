package com.thepalladiumgroup.iqm.core.services.sync.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.services.sync.IConceptSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.endpoints.IConceptWapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public class ConceptSyncService extends SyncService implements IConceptSyncService {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(ConceptSyncService.class);
    private IConceptWapi conceptWapi;
    private List<MConcept> concepts = new ArrayList<>();
    private String errorResponse;

    public ConceptSyncService(IApplicationManager applicationManager) {
        super(applicationManager);
    }

    @Override
    public IConceptWapi getConceptWapi() {
        return this.conceptWapi;
    }

    @Override
    public String getError() {
        return errorResponse;
    }

    @Override
    public void initialze() {
        super.initialze();
        if (conceptWapi == null) {
            conceptWapi = super.createService(IConceptWapi.class);
        }
    }

    @Override
    public List<MConcept> readAllConcepts(int encounterTypeId) throws IOException {
        initialze();
        SLF_LOGGER.debug("sending ConceptWapi GET request...");
        Call<List<MConcept>> call = getConceptWapi().getConcepts(encounterTypeId);
        concepts = call.execute().body();
        SLF_LOGGER.debug("ConceptWapi request executed state:" + call.isExecuted());
        return concepts;
    }

}

