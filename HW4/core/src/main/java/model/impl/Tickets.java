package model.impl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "tickets")
public class Tickets {

    private List<TicketEntry> tt;

    public List<TicketEntry> getTicketEntries() {
        return tt;
    }

    @XmlElement(name = "ticket")
    public void setTicketEntries(List<TicketEntry> t) {
        this.tt = t;
    }
}
