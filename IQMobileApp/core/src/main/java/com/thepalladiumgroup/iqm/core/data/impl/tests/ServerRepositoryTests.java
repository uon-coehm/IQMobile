package com.thepalladiumgroup.iqm.core.data.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IServerRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ServerRepository;
import com.thepalladiumgroup.iqm.core.model.Server;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by Koske Kimutai [2016/04/22]
 */
public class ServerRepositoryTests extends BaseCoreTest {

    private IServerRepository repository;

    @Before
    public void setUp() throws SQLException {
        repository = new ServerRepository(applicationManager);
    }

    @Test
    public void should_getdefualt() throws SQLException {
        Server server = repository.getDefault();
        Assert.assertNotNull(server);
        System.out.println(server);
    }

    @After
    public void tearDown() throws SQLException {
    }
}
