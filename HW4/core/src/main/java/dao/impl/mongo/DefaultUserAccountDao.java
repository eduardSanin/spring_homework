package dao.impl.mongo;

import dao.interfaces.UserAccountDao;
import model.impl.mongo.UserAcc;
import model.impl.mongo.UserAccount;
import model.impl.mongo.UserEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
@Profile("mongo")
public class DefaultUserAccountDao implements UserAccountDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public UserAcc createUserAccount(UserAcc userAccount) {
        mongoTemplate.insert(userAccount);
        return userAccount;
    }

    @Override
    public UserAcc getUserAccountByUserId(long userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserAcc getUserAccountByUserId(BigInteger userId) {
        UserEntry userEntry = new UserEntry();
        userEntry.setUserId(userId);
        UserAccount userAccount = new UserAccount();
        userAccount.setUser(userEntry);
        Query query = new Query();
        query.addCriteria(Criteria.byExample(userAccount));
        return mongoTemplate.findOne(query, UserAccount.class);
    }

    @Override
    public boolean updateUserAccount(UserAcc userAccount) {
        mongoTemplate.save(userAccount);
        return true;
    }
}
