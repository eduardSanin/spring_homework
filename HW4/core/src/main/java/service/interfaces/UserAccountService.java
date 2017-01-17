package service.interfaces;

import model.impl.mongo.UserAcc;

public interface UserAccountService {
    UserAcc getUserAccountByUserId(long userId);

    boolean updateUserAccount(UserAcc userAccount);

    UserAcc createUserAccount(UserAcc userAccount);

    void refillingAccount(UserAcc userAccount);
}
