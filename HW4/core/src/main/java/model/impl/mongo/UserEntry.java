package model.impl.mongo;

import model.interfaces.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Document(collection = "user")
public class UserEntry implements User {
    @Id
    private BigInteger userId;
    private String name;
    private String email;

    @Override
    public long getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setId(long id) {
        throw new UnsupportedOperationException();
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger id) {
        this.userId = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntry userEntry = (UserEntry) o;

        if (userId.compareTo(userEntry.userId) != 0) return false;
        if (name != null ? !name.equals(userEntry.name) : userEntry.name != null) return false;
        return email != null ? email.equals(userEntry.email) : userEntry.email == null;

    }

    @Override
    public int hashCode() {
        int result = userId.intValue();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "UserEntry{" +
                "id=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
