package general;

import model.interfaces.Event;
import model.interfaces.User;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserAndEventHolder {
    private User defaultUser;
    private Event defaultEvent;

    public User getDefaultUser() {
        return defaultUser;
    }

    public void setDefaultUser(User defaultUser) {
        this.defaultUser = defaultUser;
    }

    public Event getDefaultEvent() {
        return defaultEvent;
    }

    public void setDefaultEvent(Event defaultEvent) {
        this.defaultEvent = defaultEvent;
    }
}
