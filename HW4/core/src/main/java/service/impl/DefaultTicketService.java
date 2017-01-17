package service.impl;

import dao.interfaces.EventDao;
import dao.interfaces.TicketDao;
import dao.interfaces.UserAccountDao;
import model.impl.Tickets;
import model.impl.mongo.UserAcc;
import model.interfaces.Event;
import model.interfaces.Ticket;
import model.interfaces.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import service.interfaces.TicketService;
import service.interfaces.UserAccountService;

import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Service
public class DefaultTicketService implements TicketService {
    private static final Logger LOG = Logger.getLogger(DefaultTicketService.class);
    @Value("${xml.files.base.dir}")
    private String baseDirForXmlToUnMarchale;
    @Autowired
    private TicketDao ticketDao;
    @Autowired
    private UserAccountDao userAccountDao;
    @Autowired
    private EventDao eventDao;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private Unmarshaller unmarshaller;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        UserAcc userAccount = userAccountDao.getUserAccountByUserId(userId);
        Event event = eventDao.getEventById(eventId);
        if (Objects.nonNull(event) && doesUserHaveManyToBookTicket(userAccount, event)) {
            Ticket bookedTicket = ticketDao.bookTicket(userId, eventId, place, category);
            subtractManyFromUserAccount(userAccount, event);
            return bookedTicket;
        }
        LOG.info(MessageFormat.format("ticket with user id:[{0}] and event id:[{1}] cannot be booked", userId, eventId));
        return null;
    }

    @Override
    public void loadTicketsFromXml(String fileName) {
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(baseDirForXmlToUnMarchale + fileName)) {
                    Tickets tickets = (Tickets) unmarshaller.unmarshal(new StreamSource(is));
                    tickets.getTicketEntries().forEach(ticketEntry -> ticketDao.bookTicket(ticketEntry.getUserId(), ticketEntry.getEventId(), ticketEntry.getPlace(), ticketEntry.getCategory()));
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    LOG.error(e);
                }
            }
        });
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        return ticketDao.getBookedTickets(user, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        return ticketDao.getBookedTickets(event, pageSize, pageNum);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return ticketDao.cancelTicket(ticketId);
    }

    private void subtractManyFromUserAccount(UserAcc userAccount, Event event) {
        userAccount.setPrePaidAmount(userAccount.getPrePaidAmount().subtract(event.getTicketPrice()));
        userAccountService.refillingAccount(userAccount);
    }

    private boolean doesUserHaveManyToBookTicket(UserAcc userAccount, Event event) {
        return event.getTicketPrice().compareTo(userAccount.getPrePaidAmount()) <= 0;
    }

}
