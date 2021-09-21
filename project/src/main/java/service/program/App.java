package service.program;

import service.impl.AccountServiceImpl;
import service.impl.BankCardServiceImpl;
import service.impl.BankServiceImpl;
import service.impl.CustomerServiceImpl;
import service.util.ApplicationContext;
import service.util.HibernateUtil;
import service.util.SecurityContext;

public class App {

    private CustomerServiceImpl customerService;
    private AccountServiceImpl accountService;
    private BankCardServiceImpl bankCardService;
    private BankServiceImpl bankService;

    public void initialize() {
        customerService = ApplicationContext.getCustomerService();
        accountService = ApplicationContext.getAccountService();
        bankCardService = ApplicationContext.getBankcardService();
        bankService=ApplicationContext.getBankService();

        customerService.initialize();
        accountService.initialize();

        if(bankService.countAll()==0)
            bankService.addDefaultBank();

    }

    public void start() {

        System.out.println("1.register");
        System.out.println("2.login");
        System.out.println("3.show all current account of user");
        System.out.println("4.exit");

        switch (ApplicationContext.getScannerForInteger().nextInt()) {

            case 1 -> {

                try {
                    customerService.register();
                    userPanel();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            case 2 -> {

                try {
                    customerService.login();
                    userPanel();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
//                    CustomerServiceImpl.NUMBER_ALLOWED_LOGIN++;
                }
            }
            case 3 -> {
                accountService.showCurrentAccountOfUserWithNationalCode(getNationalCode());
                start();
            }

            case 4 -> {
                ApplicationContext.getEntityManger().close();
                HibernateUtil.getEntityManagerFactory().close();
                System.out.println("have nice day ###");
                System.exit(0);
            }
            default -> {
                System.out.println("invalid input ");
                start();
            }
        }
    }

    private void userPanel() {

        System.out.println("1.open account");
        System.out.println("2.cart to cart");
        System.out.println("3.withdrawal");
        System.out.println("4.see inventory");
        System.out.println("5.see all transaction of my account");
        System.out.println("6.set new  password or changed old password");
        System.out.println("7.logout");

        switch (ApplicationContext.getScannerForInteger().nextInt()) {

            case 1 -> {
                try {
                    accountService.openAccountWhenUserRegistered();


                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                userPanel();
            }
            case 2 -> {
                try {

                    customerService.cartToCart();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                userPanel();
            }
            case 3 -> {

                try {
                    accountService.withdrawal();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                userPanel();
            }
            case 4 -> {

                try {
                    customerService.showAllInventoryOfAccount();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                userPanel();
            }
            case 5 -> {
                try {

                    accountService.showAllTransactionFromSpecifiedTime();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                userPanel();
            }
            case 6 -> {
                try {

                    bankCardService.saveOrChangePassword();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                userPanel();
            }
            case 7 -> {
                SecurityContext.logout();
                start();
            }
        }
    }

    private String getNationalCode() {
        System.out.println("enter your nationalCode");
        return ApplicationContext.getScannerForString().nextLine();
    }
}
