package com.thepalladiumgroup.iqm.core.data.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IUserRepository;
import com.thepalladiumgroup.iqm.core.data.impl.UserRepository;
import com.thepalladiumgroup.iqm.core.model.User;
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

public final class UserRepositoryTest extends BaseCoreTest {

    private IUserRepository repository;
    private List<Integer> testuserIds;

    @Before
    public void setUp() throws SQLException {
        testuserIds = new ArrayList<>();
        repository = new UserRepository(applicationManager);
        for (User user : TestData.getTestUsers()) {
            User newuser = repository.save(user);
            testuserIds.add(newuser.getId());
        }
    }

    @Test
    public void should_find_byusername() throws SQLException {
        User user = repository.findbyUsername("iq-maun-02");
        Assert.assertEquals("pass23", user.getPassword());
        System.out.println("User:" + user.toString());
    }

    @Test
    public void should_delete_all() throws SQLException {
        List<User> users = repository.getAll();
        Assert.assertTrue(users.size() > 1);
        repository.deleteAll();
        users = repository.getAll();
        Assert.assertTrue(users.size() == 1);
        User user = users.get(0);
        Assert.assertEquals("android-admin", user.getUsername());
        System.out.println("User:" + user.toString());
    }

    @After
    public void tearDown() throws SQLException {
        for (int userid : testuserIds) {
            repository.delete(userid);
        }
    }
}
