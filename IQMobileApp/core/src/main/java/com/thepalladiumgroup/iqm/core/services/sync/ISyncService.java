package com.thepalladiumgroup.iqm.core.services.sync;

import com.thepalladiumgroup.iqm.core.model.Server;

import java.sql.SQLException;

import retrofit2.Retrofit;

/**
 * Created by Koske Kimutai [2016/05/11]
 */
public interface ISyncService {
    Server getServer();

    Retrofit getAdapter();

    String getError();

    void initialze() throws SQLException;
}
