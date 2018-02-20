package com.thepalladiumgroup.iqm.core.data.impl;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IUserRepository;
import com.thepalladiumgroup.iqm.core.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends BaseRepository<User> implements IUserRepository {

    public UserRepository(IApplicationManager applicationManager) {
        super(applicationManager);
    }

    @Override
    public List<User> getAllUsers() throws SQLException {

        SLF_LOGGER.debug(">> Load Users with iterator >>");

        List<User> users = new ArrayList<>();


        List<String> cols = new ArrayList<>();

        cols.add("counsellorcode");
        cols.add("password");
        cols.add("username");
        cols.add("updatedate");
        cols.add("uuid");
        cols.add("id");
        cols.add("iqcareid");
        cols.add("syncstate");
        cols.add("userid");

        QueryBuilder<User, Integer> qb = entityDao.queryBuilder()
                .selectColumns(cols);

        CloseableIterator<User> iterator = entityDao.iterator(qb.prepare());
        try {
            while (iterator.hasNext()) {
                User account = iterator.next();
                users.add(account);
            }
        } finally {
            // close it at the end to close underlying SQL statement
            iterator.close();
        }

        return users;

    }

    @Override
    public User findbyUsername(String username) throws SQLException {
        // query for all orders that match a certain account
        List<User> results =entityDao.queryForEq("username",username);

        if(results.size()>0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public void deleteAll() throws SQLException {
        DeleteBuilder<User, Integer> deleteBuilder = entityDao.deleteBuilder();
        deleteBuilder.where().ge("id", 0);
        deleteBuilder.delete();
    }

    @Override
    public void deleteAll(List<User> entities) throws SQLException {
        entityDao.delete(entities);
    }
}
