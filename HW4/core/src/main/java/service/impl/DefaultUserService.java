package service.impl;

import dao.interfaces.UserDao;
import model.interfaces.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import service.interfaces.UserService;

import java.util.List;

@Service
public class DefaultUserService implements UserService {
    private UserDao userDao;

    @Override
    public User getUserById(long userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userDao.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public User create(User user) {
        return userDao.create(user);
    }

    @Override
    public boolean remove(Long id) {
        return userDao.remove(id);
    }

    @Required
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
