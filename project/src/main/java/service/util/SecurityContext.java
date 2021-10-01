package service.util;

import domain.Customer;

public class SecurityContext {

    private static Customer currentUser; 

    public static void login(Customer user)  {

        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static Customer getCurrentUser()  {
        return currentUser;
    }
}
