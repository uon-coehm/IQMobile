package com.thepalladiumgroup.iqm.core.data.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IRepository;
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
 * Created by Koske Kimutai [2016/04/14]
 */
public final class BaseRepositoryTest extends BaseCoreTest {

    private IRepository<User> repository;
    private List<Integer> testuserIds;
    private List<User> testUsers;

    @Before
    public void setUp() throws SQLException {
        testuserIds = new ArrayList<>();
        testUsers = TestData.getTestUsers();
        repository = new UserRepository(applicationManager);
        for (User user : testUsers) {
            User newuser=repository.save(user);
            testuserIds.add(newuser.getId());
        }
    }

    @Test
    public void  should_findbyId() throws  SQLException{
        User user =repository.find(testuserIds.get(0));
        Assert.assertNotNull(user);
        System.out.println("Found user:" + user.toString());
    }


    @Test
    public void should_findby_syncid() throws SQLException {
        User user = repository.findbySyncId(testUsers.get(0).getUuid());
        Assert.assertNotNull(user);
        System.out.println("Found user:"+user.toString());
    }

    @Test
    public void should_findbyfield_value() throws SQLException {
        User user = repository.findbyfield("username", "android-admin");
        Assert.assertNotNull(user);
        System.out.println("Found user:" + user.toString());
    }

    @Test
    public void  should_saveNew() throws  SQLException{
        User newuser=new User("XTREST","---");

        User saveduser =repository.save(newuser);

        Assert.assertNotNull(saveduser);
        testuserIds.add(saveduser.getId());
        System.out.println("Saved new user:"+saveduser.toString());


    }

    @Test
    public void  should_updateExisiting() throws  SQLException{
        User newuser=new User("XTREST","---");
        User saveduser =repository.save(newuser);
        Assert.assertNotNull(saveduser);
        testuserIds.add(saveduser.getId());

        saveduser.setPassword("maun");
        User updatedUser= repository.update(saveduser);
        Assert.assertTrue("maun".equals(updatedUser.getPassword()));
        System.out.println("Updated user:"+updatedUser.toString());
    }
    @Test
    public void  should_getAll() throws  SQLException{
        List<User> usersList =repository.getAll();
        Assert.assertTrue(usersList.size()>0);
        System.out.println("TEST DATA:users");
        for (User user:usersList             ) {
            System.out.println("user:"+user.toString());
        }
   }

    @Test
    public void should_getAll_byfield_value() throws SQLException {
        List<User> usersList = repository.getAllbyfield("username", "android-admin");
        Assert.assertTrue(usersList.size() > 0);
        System.out.println("TEST DATA:users");
        for (User user : usersList) {
            System.out.println("user:" + user.toString());
        }
    }

    @Test
    public void  should_delete() throws  SQLException{
        User newuser=new User("new-del-user","---");
        User saveduser =repository.save(newuser);
        Assert.assertNotNull(saveduser);

        repository.delete(saveduser.getId());
        User deletedUser=repository.find(saveduser.getId());
        Assert.assertNull(deletedUser);
        System.out.println("DELETED user:"+saveduser.toString());
    }

    @After
    public void tearDown() throws SQLException{

        for (int userid: testuserIds) {
                repository.delete(userid);
        }
    }

}
