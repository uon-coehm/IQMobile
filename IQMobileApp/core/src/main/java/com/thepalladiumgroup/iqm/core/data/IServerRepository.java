package com.thepalladiumgroup.iqm.core.data;

import com.thepalladiumgroup.iqm.core.model.Server;

import java.sql.SQLException;

/**
 * Created by Koske Kimutai [2016/04/22]
 */
public interface IServerRepository extends IRepository<Server> {
    Server getDefault() throws SQLException;
}
