package general;

import model.interfaces.Event;
import model.interfaces.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public class CustomMapSqlParameterSource extends MapSqlParameterSource {
    private static final String USER_ID = "uid";
    private static final String EVENT_ID = "eid";
    private DefaultUserAndEventHolder defaultUserAndEventHolder;

    public CustomMapSqlParameterSource(DefaultUserAndEventHolder defaultUserAndEventHolder) {
        this.defaultUserAndEventHolder = defaultUserAndEventHolder;
    }

    @Override
    public Object getValue(String paramName) {
        if (EVENT_ID.equals(paramName)) {
            Event defaultEvent = defaultUserAndEventHolder.getDefaultEvent();
            if (defaultEvent != null) {
                return defaultEvent.getId();
            }
        }
        if (USER_ID.equals(paramName)) {
            User defaultUser = defaultUserAndEventHolder.getDefaultUser();
            if (defaultUser != null) {
                return defaultUser.getId();
            }
        }
        return super.getValue(paramName);
    }
}
