package com.thepalladiumgroup.iqm.core.services.sync.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.Module;
import com.thepalladiumgroup.iqm.core.services.IModuleService;
import com.thepalladiumgroup.iqm.core.services.impl.ModuleService;
import com.thepalladiumgroup.iqm.core.services.sync.IModuleSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.endpoints.IModuleWapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public class ModuleSyncService extends SyncService implements IModuleSyncService {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(ModuleSyncService.class);
    private final IModuleService userService;
    //private Retrofit retrofit;
    private IModuleWapi moduleWapi;
    private List<Module> modules = new ArrayList<>();
    private String errorResponse;

    public ModuleSyncService(IApplicationManager applicationManager) {
        super(applicationManager);
        userService = new ModuleService(applicationManager);
    }

    @Override
    public IModuleWapi getModuleWapi() {
        return this.moduleWapi;
    }

    @Override
    public String getError() {
        return errorResponse;
    }

    @Override
    public void initialze() {
        super.initialze();
        if (moduleWapi == null) {
            moduleWapi = super.createService(IModuleWapi.class);
        }
    }

    @Override
    public List<Module> readAllModules() throws IOException {
        initialze();
        SLF_LOGGER.debug("sending ModuleWapi GET request...");
        Call<List<Module>> call = getModuleWapi().getModules();
        modules = call.execute().body();
        SLF_LOGGER.debug("ModuleWapi request executed state:" + call.isExecuted());
        return modules;
    }

}

