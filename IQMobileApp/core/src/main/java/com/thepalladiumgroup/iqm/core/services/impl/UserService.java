package com.thepalladiumgroup.iqm.core.services.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IUserRepository;
import com.thepalladiumgroup.iqm.core.data.impl.UserRepository;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.core.services.IUserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/12]
 */

public class UserService implements IUserService {

    private final IApplicationManager applicationManager;
    private final IUserRepository userRepository;
    private List<User> exisitingUsers = new ArrayList<>();

    public UserService(IApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
        this.userRepository = new UserRepository(applicationManager);
    }

    @Override
    public List<User> loadUsers() throws SQLException {
        exisitingUsers = userRepository.getAllUsers();
        return exisitingUsers;
    }

    @Override
    public void ClearAll() throws SQLException {
        userRepository.deleteAll();
    }

    @Override
    public void syncUser(User user) throws SQLException {

        if (checkUserExsist(user)) {
            userRepository.updateOnly(user);
        } else {
            userRepository.saveOnly(user);
            //exisitingUsers.add(user);
        }
    }

    @Override
    public User validateUser(User loginUser) throws SQLException {
        User user = userRepository.findbyUsername(loginUser.getUsername());

        if (user != null) {
            if (user.getPassword().equals(loginUser.getPassword())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findBySyncId(String uuid) throws SQLException {
        return userRepository.findbySyncId(uuid);
    }

    @Override
    public User findByIQcareId(int id) throws SQLException {
        return userRepository.findbyfield("iqcareid", id);
    }

    private boolean checkUserExsist(User find) {
        boolean found = false;
        for (User u : exisitingUsers) {
            if (find.equals(u)) {
                found = true;
                break;
            }
        }
        return found;
    }
}
