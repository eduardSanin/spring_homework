package model.impl;

import model.interfaces.User;

public class UserEntry implements User {
    private long id;
    private String name;
    private String email;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
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

        if (id != userEntry.id) return false;
        if (name != null ? !name.equals(userEntry.name) : userEntry.name != null) return false;
        return email != null ? email.equals(userEntry.email) : userEntry.email == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "UserEntry{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
