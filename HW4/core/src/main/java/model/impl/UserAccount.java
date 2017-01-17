package model.impl;

import model.impl.mongo.UserAcc;

import java.math.BigDecimal;

public class UserAccount implements UserAcc {
    private long id;
    private BigDecimal prePaidAmount;
    private long userId;

    public UserAccount() {
    }

    public UserAccount(long id, BigDecimal prePaidAmount, long userId) {
        this.id = id;
        this.prePaidAmount = prePaidAmount;
        this.userId = userId;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public BigDecimal getPrePaidAmount() {
        return prePaidAmount;
    }

    @Override
    public void setPrePaidAmount(BigDecimal prePaidAmount) {
        this.prePaidAmount = prePaidAmount;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccount that = (UserAccount) o;

        if (userId != that.userId) return false;
        if (id != that.id) return false;
        return prePaidAmount != null ? prePaidAmount.compareTo(that.getPrePaidAmount()) == 0 : that.prePaidAmount == null;

    }

    @Override
    public int hashCode() {
        int result = (int) id;
        result = 31 * result + (prePaidAmount != null ? prePaidAmount.hashCode() : 0);
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", prePaidAmount=" + prePaidAmount +
                ", userId=" + userId +
                '}';
    }
}
