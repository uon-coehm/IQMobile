package com.thepalladiumgroup.iqm.core.data;

import com.thepalladiumgroup.iqm.core.model.Module;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/22]
 */
public interface IModuleRepository extends IRepository<Module> {
    Module findByName(String name) throws SQLException;
    Module getModuleInfo(String name) throws SQLException;
    List<Module> getAllModules() throws SQLException;
}
