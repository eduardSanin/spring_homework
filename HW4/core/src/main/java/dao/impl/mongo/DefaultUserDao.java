package dao.impl.mongo;

import dao.interfaces.UserDao;
import model.impl.mongo.UserEntry;
import model.interfaces.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
@Profile("mongo")
public class DefaultUserDao implements UserDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public User getUserById(long userId) {
        return null;
    }

    @Override
    public User getUserById(BigInteger userId) {
        return mongoTemplate.findOne(new Query(Criteria.where("id").is(userId)), User.class);
    }

    @Override
    public User getUserByEmail(String email) {
        return mongoTemplate.findOne(new Query(Criteria.where("email").is(email)), User.class);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return mongoTemplate.find(new Query(Criteria.where("name").is(name)).skip(pageNum).limit(pageSize), User.class);
    }

    @Override
    public User updateUser(User user) {
        mongoTemplate.save(user);
        return user;
    }

    @Override
    public User create(User user) {
        mongoTemplate.insert(user);
        return user;
    }

    @Override
    public boolean remove(Long userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(BigInteger id) {
        UserEntry user = new UserEntry();
        user.setUserId(id);
        return mongoTemplate.remove(user).isUpdateOfExisting();
    }
}
