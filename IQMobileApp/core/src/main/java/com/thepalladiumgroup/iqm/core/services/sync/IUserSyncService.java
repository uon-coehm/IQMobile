package com.thepalladiumgroup.iqm.core.services.sync;

import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.core.services.sync.endpoints.IUserWapi;

import java.io.IOException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public interface IUserSyncService extends ISyncService {
    IUserWapi getUserWapi();
    List<User> readAllUsers() throws IOException;
}
