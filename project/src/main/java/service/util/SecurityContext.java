package service.util;

import domain.User;

public class SecurityContext {

    private static User currentUser;

    public static void login(User user)  {

        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser()  {
        return currentUser;
    }
}