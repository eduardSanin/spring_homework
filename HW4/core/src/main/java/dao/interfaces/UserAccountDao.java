package dao.interfaces;

import model.impl.mongo.UserAcc;

import java.math.BigInteger;

public interface UserAccountDao {
    UserAcc createUserAccount(UserAcc userAccount);

    UserAcc getUserAccountByUserId(long userId);

    default UserAcc getUserAccountByUserId(BigInteger userId) {
        throw new UnsupportedOperationException();
    }

    boolean updateUserAccount(UserAcc userAccount);
}
