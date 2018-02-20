package com.thepalladiumgroup.iqm.core.services.sync.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.services.sync.ILookupSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.endpoints.ILookupWapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public class LookupSyncService extends SyncService implements ILookupSyncService {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(LookupSyncService.class);
    private ILookupWapi lookupWapi;
    private List<Lookup> lookups = new ArrayList<>();
    private String errorResponse;

    public LookupSyncService(IApplicationManager applicationManager) {
        super(applicationManager);
    }

    @Override
    public ILookupWapi getLookupWapi() {
        return this.lookupWapi;
    }

    @Override
    public String getError() {
        return errorResponse;
    }

    @Override
    public void initialze() {
        super.initialze();
        if (lookupWapi == null) {
            lookupWapi = super.createService(ILookupWapi.class);
        }
    }

    @Override
    public List<Lookup> readAllLookups() throws IOException {
        initialze();
        SLF_LOGGER.debug("sending LookupWapi GET request...");
        Call<List<Lookup>> call = getLookupWapi().getLookups();
        lookups = call.execute().body();
        SLF_LOGGER.debug("LookupWapi request executed state:" + call.isExecuted());
        return lookups;
    }
}

