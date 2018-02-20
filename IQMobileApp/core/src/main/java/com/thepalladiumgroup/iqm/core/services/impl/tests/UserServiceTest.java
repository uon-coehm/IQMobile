package com.thepalladiumgroup.iqm.core.services.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IUserRepository;
import com.thepalladiumgroup.iqm.core.data.impl.UserRepository;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.core.services.IUserService;
import com.thepalladiumgroup.iqm.core.services.impl.UserService;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;
import com.thepalladiumgroup.iqm.core.tests.TestData;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/13]
 */

public final class UserServiceTest extends BaseCoreTest {

    private IUserService userService;
    private List<Integer> testuserIds;
    private List<User> testUsers;
    private IUserRepository repository;
    private User newUser = new User("iq-kip-01", "passXXX", "cdf81e36-4850-4310-9b6e-12abd37b5b47");


    @Before
    public void setUp() throws SQLException {

        userService = new UserService(applicationManager);
        testuserIds = new ArrayList<>();
        testUsers = TestData.getTestUsers();
        repository = new UserRepository(applicationManager);
        for (User user : testUsers) {
            User newuser = repository.save(user);
            testuserIds.add(newuser.getId());
        }
    }

    @Test
    public void should_load_Users() throws SQLException {
        List<User> users = userService.loadUsers();
        Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void should_validate_User() throws SQLException {

        User user = userService.validateUser(new User("android-admin", "admin1"));
        Assert.assertNotNull(user);

        User invalidUser = userService.validateUser(new User("android-admin", "Admin1"));
        Assert.assertNull(invalidUser);
    }

    @Test
    public void should_sync_User_new() throws SQLException {
        User syncedNew = null;
        newUser.setCounsellorcode("1309");
        userService.syncUser(newUser);
        syncedNew = repository.findbySyncId(newUser.getUuid());
        testuserIds.add(syncedNew.getId());
        Assert.assertNotNull(syncedNew);

        for (User u : repository.getAll()) {
            System.out.println(u);
        }
    }

    @Test
    public void should_sync_User_existing() throws SQLException {
        String ccode = "7007";
        User user = repository.findbyUsername("iq-maun-01");
        user.setCounsellorcode(ccode);
        userService.syncUser(user);

        User syncedUser = repository.findbySyncId(user.getUuid());

        Assert.assertNotNull(syncedUser);
        Assert.assertEquals("7007", syncedUser.getCounsellorcode());

        for (User u : repository.getAll()) {
            System.out.println(u);
        }
    }


    @After
    public void tearDown() throws SQLException {

        for (int userid : testuserIds) {
            repository.delete(userid);
        }
    }
}