package com.thepalladiumgroup.iqm.core.services.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IModuleRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ModuleRepository;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Module;
import com.thepalladiumgroup.iqm.core.services.IEncounterTypeService;
import com.thepalladiumgroup.iqm.core.services.IModuleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/26]
 */
public class ModuleService implements IModuleService {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleService.class);
    private final IApplicationManager applicationManager;
    private final IModuleRepository moduleRepository;
    private final IEncounterTypeService encounterTypeService;
    private List<Module> exisitngModules = new ArrayList<>();

    public ModuleService(IApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
        this.moduleRepository = new ModuleRepository(applicationManager);
        encounterTypeService = new EncounterTypeService(applicationManager);
    }

    @Override
    public Module getDefault() throws SQLException {
        return moduleRepository.getAll().get(0);
    }

    @Override
    public Module getByName(String name) throws SQLException {
        return moduleRepository.getModuleInfo(name);
    }

    @Override
    public Module findByIQCareId(int id) throws SQLException {
        return moduleRepository.findbyfield("iqcareid", id);
    }

    @Override
    public void syncModule(Module module) throws SQLException {
        LOG.debug("syncing module");
        Module subject = checkModuleExsist(module);
        if (module.equals(subject)) {
            module.setId(subject.getId());
            moduleRepository.updateOnly(module);
            LOG.debug("syncing module-encounters");
            encounterTypeService.loadEncounterTypes(module.getId());
            LOG.debug("loaded module-encounters");
            for (EncounterType e : module.getEncounterTypes()) {
                e.setModule(module);
                LOG.debug("syncing module.encounter");
                encounterTypeService.syncEncounterType(e);
            }
        } else {
            moduleRepository.save(module);
        }
    }

    @Override
    public List<Module> getAll() throws SQLException {
        return moduleRepository.getAll();
    }

    @Override
    public List<Module> loadModules() throws SQLException {
        exisitngModules = moduleRepository.getAllModules();
        return exisitngModules;
    }

    private Module checkModuleExsist(Module find) {
        Module found = null;
        for (Module u : exisitngModules) {
            if (find.equals(u)) {
                found = u;
                break;
            }
        }
        return found;
    }
}
