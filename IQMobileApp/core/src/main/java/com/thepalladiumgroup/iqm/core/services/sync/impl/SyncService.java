package com.thepalladiumgroup.iqm.core.services.sync.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IServerRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ServerRepository;
import com.thepalladiumgroup.iqm.core.model.Server;
import com.thepalladiumgroup.iqm.core.services.sync.ISyncService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Koske Kimutai [2016/05/11]
 */
public abstract class SyncService implements ISyncService {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(SyncService.class);
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private final IServerRepository serverRepository;
    private Gson gson;

    private Server server;
    private Retrofit retrofit;


    public SyncService(IApplicationManager applicationManager) {
        this.serverRepository = new ServerRepository(applicationManager);
    }

    @Override
    public Server getServer() {
        return this.server;
    }

    @Override
    public Retrofit getAdapter() {
        return retrofit;
    }

    @Override
    public void initialze() {
        SLF_LOGGER.debug("initializing sync...");

        try {


            gson = new GsonBuilder()
                    .setDateFormat(DATE_FORMAT)
                    .create();
            SLF_LOGGER.debug("initializing sync Gson dateformat:" + DATE_FORMAT);

            if (server == null) {
                server = serverRepository.getDefault();
            }


            retrofit = new Retrofit.Builder()
                    .baseUrl(server.getUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();


            SLF_LOGGER.debug("using server " + server.toString());
        } catch (Exception e) {
            SLF_LOGGER.debug("SERVER CONFIGURATION FAILED " + e.getMessage());
            throw new Error("Error initializing Sync Service");
        }
    }

    public <S> S createService(Class<S> serviceClass) {
        SLF_LOGGER.debug("creating wapi service:" + serviceClass.getSimpleName());
        return retrofit.create(serviceClass);
    }
}
