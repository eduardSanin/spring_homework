package model.impl.mongo;

import model.interfaces.User;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.BigInteger;

@Document(collection = "userAcc")
public class UserAccount implements UserAcc {
    private BigInteger id;
    private BigDecimal prePaidAmount;
    @DBRef
    private User user;


    public BigInteger getUserAccountId() {
        return id;
    }

    public void setUserAccountId(BigInteger id) {
        this.id = id;
    }

    @Override
    public long getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setId(long id) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void setUserId(long id) {
        throw new UnsupportedOperationException();
    }

    public UserEntry getUser() {
        return (UserEntry) user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccount that = (UserAccount) o;

        if (id.compareTo(that.id) != 0) return false;
        if (prePaidAmount != null ? !(prePaidAmount.compareTo(that.prePaidAmount) == 0) : that.prePaidAmount != null)
            return false;
        return user != null ? user.equals(that.user) : that.user == null;

    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (prePaidAmount != null ? prePaidAmount.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", prePaidAmount=" + prePaidAmount +
                ", user=" + user +
                '}';
    }
}
