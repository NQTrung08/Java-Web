package main.java.com.TLU.studentmanagement.session;

import main.java.com.TLU.studentmanagement.model.User;

public class UserSession {
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserSession.user = user;
    }

    public static void clear() {
        user = null;
    }
}

