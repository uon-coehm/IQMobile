package com.thepalladiumgroup.iqm.core.data;

/**
 * Created by Koske Kimutai [2016/04/19]
 */

import com.thepalladiumgroup.iqm.core.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/12]
 */
public interface IUserRepository extends IRepository<User> {
    List<User> getAllUsers() throws SQLException;
    User findbyUsername(String username) throws SQLException;

}