package info.ryandorman.simplescheduler.common;

import java.time.ZoneId;

public class UserSession {
    private static UserSession session;
    private long userId;
    private String userName;
    private ZoneId userZone;
    private boolean loggedIn = false;

    private UserSession() {
    }

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

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
