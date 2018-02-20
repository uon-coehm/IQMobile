package com.thepalladiumgroup.iqm.core.data.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IServerRepository;
import com.thepalladiumgroup.iqm.core.model.Server;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/05/11]
 */
public class ServerRepository extends BaseRepository<Server> implements IServerRepository {
    public ServerRepository(IApplicationManager applicationManager) {
        super(applicationManager);
    }

    @Override
    public Server getDefault() throws SQLException {
        List<Server> results = getAll();
        if (results.size() > 0) {
            Collections.sort(results);
            return results.get(0);
        }
        return null;
    }
}
