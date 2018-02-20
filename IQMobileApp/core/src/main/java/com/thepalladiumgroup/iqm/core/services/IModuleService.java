package com.thepalladiumgroup.iqm.core.services;

import com.thepalladiumgroup.iqm.core.model.Module;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/26]
 */
public interface IModuleService {

    Module getDefault() throws SQLException;
    Module getByName(String name) throws SQLException;
    Module findByIQCareId(int id) throws SQLException;
    void syncModule(Module module) throws SQLException;
    List<Module> getAll() throws SQLException;

    List<Module> loadModules() throws SQLException;
}
