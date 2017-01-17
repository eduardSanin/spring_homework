package service.interfaces;

import general.CrudInterface;
import model.interfaces.User;

import java.util.List;

public interface UserService extends CrudInterface<User, Long> {
    User getUserById(long userId);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    User updateUser(User user);
}
