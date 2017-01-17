package model.impl;

import model.interfaces.Ticket;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ticket")
public class TicketEntry implements Ticket {
    private long id;
    private long uid;
    private long eid;
    private Category category;
    private int place;

    @Override
    public long getId() {
        return id;
    }

    @Override
    @XmlAttribute(name = "id")
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getEventId() {
        return eid;
    }

    @Override
    @XmlAttribute(name = "eventId")
    public void setEventId(long eventId) {
        this.eid = eventId;
    }

    @Override
    public long getUserId() {
        return uid;
    }

    @Override
    @XmlAttribute(name = "userId")
    public void setUserId(long userId) {
        uid = userId;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    @XmlAttribute(name = "category")
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int getPlace() {
        return place;
    }

    @Override
    @XmlAttribute(name = "place")
    public void setPlace(int place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketEntry that = (TicketEntry) o;

        if (id != that.id) return false;
        if (uid != that.uid) return false;
        if (eid != that.eid) return false;
        if (place != that.place) return false;
        return category == that.category;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (uid ^ (uid >>> 32));
        result = 31 * result + (int) (eid ^ (eid >>> 32));
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + place;
        return result;
    }

    @Override
    public String toString() {
        return "TicketEntry{" +
                "id=" + id +
                ", uid=" + uid +
                ", eid=" + eid +
                ", category=" + category +
                ", place=" + place +
                '}';
    }
}
