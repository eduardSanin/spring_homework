package model.impl.mongo;

import model.interfaces.User;

import java.math.BigDecimal;

public interface UserAcc {

    long getId();

    void setId(long id);

    BigDecimal getPrePaidAmount();

    void setPrePaidAmount(BigDecimal prePaidAmount);

    long getUserId();

    void setUserId(long user);

    default void setUser(User user) {
        throw new UnsupportedOperationException();
    }
}
