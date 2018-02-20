package com.thepalladiumgroup.iqm.core.services;

import com.thepalladiumgroup.iqm.core.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/12]
 */
public interface IUserService {
    List<User> loadUsers() throws SQLException;

    void ClearAll() throws SQLException;
    void syncUser(User user) throws SQLException;

    User validateUser(User loginUser) throws SQLException;

    User findBySyncId(String uuid) throws SQLException;

    User findByIQcareId(int id) throws SQLException;
}
