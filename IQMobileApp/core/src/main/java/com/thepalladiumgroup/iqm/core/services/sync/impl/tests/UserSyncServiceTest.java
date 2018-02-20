package com.thepalladiumgroup.iqm.core.services.sync.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IUserRepository;
import com.thepalladiumgroup.iqm.core.data.impl.UserRepository;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.core.services.IUserService;
import com.thepalladiumgroup.iqm.core.services.impl.UserService;
import com.thepalladiumgroup.iqm.core.services.sync.IUserSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.impl.UserSyncService;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public class UserSyncServiceTest extends BaseCoreTest {

    private IUserSyncService userSyncService;
    private IUserRepository userRepository;
    private IUserService userService;

    @Before
    public void setUp() throws SQLException {
        userService = new UserService(applicationManager);
        userSyncService = new UserSyncService(applicationManager);
        userRepository = new UserRepository(applicationManager);
    }

    @Test
    public void should_initialize() throws Exception {
        userSyncService.initialze();
        Assert.assertNotNull(userSyncService.getServer());
        Assert.assertNotNull(userSyncService.getAdapter());

        System.out.println(userSyncService.getServer());
    }

    @Test
    public void should_read_all_users() throws Exception {
        userSyncService.initialze();
        List<User> users = userSyncService.readAllUsers();
        Assert.assertNotNull(users);
        for (User u : users) {
            System.out.println(u.getUsername() + "|" + u.getPassword());
        }
    }

    @Test
    public void should_sync_all_users() throws Exception {
        userSyncService.initialze();
        List<User> users = userSyncService.readAllUsers();
        Assert.assertNotNull(users);
        userService.ClearAll();
        userService.loadUsers();
        for (User user : users) {
            userService.syncUser(user);
        }

        User ndolo = userService.findByIQcareId(44);
        Assert.assertNotNull(ndolo);
        Assert.assertEquals("ndolo", ndolo.getUsername());
        System.out.println(ndolo);
    }
}