package com.thepalladiumgroup.iqm.core.services.sync.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.core.services.IUserService;
import com.thepalladiumgroup.iqm.core.services.impl.UserService;
import com.thepalladiumgroup.iqm.core.services.sync.IUserSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.endpoints.IUserWapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public class UserSyncService extends SyncService implements IUserSyncService {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(UserSyncService.class);
    private final IUserService userService;
    private IUserWapi userWapi;
    private List<User> users = new ArrayList<>();
    private String errorResponse;

    public UserSyncService(IApplicationManager applicationManager) {
        super(applicationManager);
        userService = new UserService(applicationManager);
    }

    @Override
    public IUserWapi getUserWapi() {
        return this.userWapi;
    }

    @Override
    public String getError() {
        return errorResponse;
    }

    @Override
    public void initialze() {
        super.initialze();
        if (userWapi == null) {
            userWapi = super.createService(IUserWapi.class);
        }
    }

    @Override
    public List<User> readAllUsers() throws IOException {
        initialze();
        SLF_LOGGER.debug("sending UserWAPI GET request...");
        Call<List<User>> call = getUserWapi().getUsers();
        users = call.execute().body();
        SLF_LOGGER.debug("UserWAPI request executed state:" + call.isExecuted());
        return users;
    }
}

