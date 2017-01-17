package dao.interfaces;

import general.CrudInterface;
import model.interfaces.User;

import java.math.BigInteger;
import java.util.List;

public interface UserDao extends CrudInterface<User, Long> {
    User getUserById(long userId);

    default User getUserById(BigInteger userId) {
        return null;
    }

    User getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    User updateUser(User user);

    default boolean remove(BigInteger id) {
        throw new UnsupportedOperationException();
    }
}
