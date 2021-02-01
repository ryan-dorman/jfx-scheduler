package info.ryandorman.simplescheduler.common;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * A singleton object used for storing the current user's session in the scheduling application. It stores information
 * useful for tracking the current user such as their username and the location and time their session started from.
 */
public class UserSession {
    private static UserSession session;
    private long userId;
    private String userName;
    private ZoneId userZone;
    private ZonedDateTime sessionStart;

    /**
     * Private constructor cannot be called externally. This singleton requires Session initialization via
     * <code>getInstance()</code>.
     */
    private UserSession() {
    }

    /**
     *
     * @return
     */
    public static UserSession getInstance () {
        if (session == null) {
            return new UserSession();
        } else {
            return session;
        }
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ZoneId getUserZone() {
        return userZone;
    }

    public void setUserZone(ZoneId userZone) {
        this.userZone = userZone;
    }

    public ZonedDateTime getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(ZonedDateTime sessionStart) {
        this.sessionStart = sessionStart;
    }
}
