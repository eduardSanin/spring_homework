package model.impl;

import model.interfaces.Event;

import java.math.BigDecimal;
import java.util.Date;

public class EventEntry implements Event {
    private long id;
    private String title;
    private Date date;
    private BigDecimal ticketPrice;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Date getDate() {
        return new Date(date.getTime());
    }

    @Override
    public void setDate(Date date) {
        this.date = new Date(date.getTime());
    }

    @Override
    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    @Override
    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventEntry that = (EventEntry) o;

        if (id != that.id) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return ticketPrice != null ? ticketPrice.compareTo(that.ticketPrice) == 0 : that.ticketPrice == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (ticketPrice != null ? ticketPrice.hashCode() : 0);
        return result;
    }
}
