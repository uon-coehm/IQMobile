package com.thepalladiumgroup.iqm.core.services.sync;

import com.thepalladiumgroup.iqm.core.model.Module;
import com.thepalladiumgroup.iqm.core.services.sync.endpoints.IModuleWapi;

import java.io.IOException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public interface IModuleSyncService extends ISyncService {
    IModuleWapi getModuleWapi();

    List<Module> readAllModules() throws IOException;
}
