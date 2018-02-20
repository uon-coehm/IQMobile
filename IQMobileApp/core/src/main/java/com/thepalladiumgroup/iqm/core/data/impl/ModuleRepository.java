package com.thepalladiumgroup.iqm.core.data.impl;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.QueryBuilder;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IEncounterTypeRepository;
import com.thepalladiumgroup.iqm.core.data.IModuleRepository;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Module;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/22]
 */
public class ModuleRepository extends BaseRepository<Module> implements IModuleRepository {

    private final IEncounterTypeRepository encounterTypeRepository;

    public ModuleRepository(IApplicationManager applicationManager) {
        super(applicationManager);
        encounterTypeRepository = new EncounterTypeRepository(applicationManager);
    }

    @Override
    public Module save(Module entity) throws SQLException {
        Boolean needsRefersh = true;
        Module module = super.save(entity);

        for (EncounterType e : entity.getNewEncounterTypes()) {
            needsRefersh = true;
            e.setModule(module);
            encounterTypeRepository.save(e);

        }
        if (needsRefersh) {
            entityDao.refresh(module);
        }
        return module;
    }

    @Override
    public Module update(Module entity) throws SQLException {
        Boolean needsRefersh = true;
        Module module = super.update(entity);

        for (EncounterType e : entity.getNewEncounterTypes()) {
            needsRefersh = true;
            e.setModule(module);
            encounterTypeRepository.update(e);
        }
        if (needsRefersh) {
            entityDao.refresh(module);
        }
        return module;
    }

    @Override
    public void delete(int id) throws SQLException {
        Module module = find(id);
        module.getEncounterTypes().clear();
        delete(module);
    }

    @Override
    public Module findByName(String name) throws SQLException {
        return findbyfield("name", name);
    }

    @Override
    public Module getModuleInfo(String name) throws SQLException {
        SLF_LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> with iterator >>>>>>>>>>>>>>>>>>>");

        List<Module> modules = new ArrayList<>();

        List<String> cols = new ArrayList<>();

        cols.add("id");
        cols.add("display");
        cols.add("displayshort");
        cols.add("icon");
        cols.add("name");


        QueryBuilder<Module, Integer> qb = entityDao.queryBuilder()
                .selectColumns(cols);


        qb.where().eq("name", name);


        CloseableIterator<Module> iterator = entityDao.iterator(qb.prepare());
        try {
            while (iterator.hasNext()) {
                Module account = iterator.next();
                modules.add(account);
            }
        } finally {
            // close it at the end to close underlying SQL statement
            iterator.close();
        }

        if (modules.size() > 0) {
            return modules.get(0);
        }


        return null;
    }

    @Override
    public List<Module> getAllModules() throws SQLException {

        SLF_LOGGER.debug(">> Modules with iterator >>>");

        List<Module> modules = new ArrayList<>();

        QueryBuilder<Module, Integer> qb = entityDao.queryBuilder();


        CloseableIterator<Module> iterator = entityDao.iterator(qb.prepare());
        try {
            while (iterator.hasNext()) {
                Module account = iterator.next();
                modules.add(account);
            }
        } finally {
            // close it at the end to close underlying SQL statement
            iterator.close();
        }


        return modules;
    }
}
