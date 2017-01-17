package service.impl;

import dao.interfaces.UserAccountDao;
import model.impl.mongo.UserAcc;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.interfaces.UserAccountService;

import java.text.MessageFormat;
import java.util.Objects;

@Service
public class DefaultUserAccountService implements UserAccountService {
    private static final Logger LOG = Logger.getLogger(DefaultUserAccountService.class);
    @Autowired
    private UserAccountDao userAccountDao;

    @Override
    public UserAcc getUserAccountByUserId(long userId) {
        return userAccountDao.getUserAccountByUserId(userId);
    }

    @Override
    public boolean updateUserAccount(UserAcc userAccount) {
        return userAccountDao.updateUserAccount(userAccount);
    }

    @Override
    public UserAcc createUserAccount(UserAcc userAccount) {
        return userAccountDao.createUserAccount(userAccount);
    }

    @Override
    public void refillingAccount(UserAcc userAccount) {
        boolean transactionResult;
        if (Objects.nonNull(getUserAccountByUserId(userAccount.getId()))) {
            transactionResult = updateUserAccount(userAccount);
            LOG.info(MessageFormat.format("updating an existing userAccount with user id:[{0}] set prePaidAmount to: [{1}]", userAccount.getUserId(), userAccount.getPrePaidAmount()));
        } else {
            transactionResult = createUserAccount(userAccount) != null;
            LOG.info(MessageFormat.format("create a new userAccount with user id:[{0}] and prePaidAmount : [{1}]", userAccount.getUserId(), userAccount.getPrePaidAmount()));
        }
        if (!transactionResult) {
            throw new IllegalArgumentException("cannot refillingUserAccount");
        }
    }
}
