package service.util;

import repository.impl.AccountRepositoryImpl;
import repository.impl.BankCardRepositoryImpl;
import repository.impl.BankRepositoryImpl;
import repository.impl.CustomerRepositoryImpl;
import service.impl.AccountServiceImpl;
import service.impl.BankCardServiceImpl;
import service.impl.BankServiceImpl;
import service.impl.CustomerServiceImpl;

import javax.persistence.EntityManager;
import java.util.Scanner;

public class ApplicationContext {

    private static final Scanner scannerForString;
    private static final Scanner scannerForInteger;
    private static final CustomerRepositoryImpl customerRepository;
    private static final CustomerServiceImpl customerService;
    private static final AccountServiceImpl accountService;
    private static final BankCardRepositoryImpl bankcardRepository;
    private static final BankCardServiceImpl bankcardService;
    private static final BankRepositoryImpl bankRepository;
    private static final BankServiceImpl bankService;


    private static final AccountRepositoryImpl accountRepository;

    private static final EntityManager entityManger;

    static {

        scannerForString=new Scanner(System.in);
        scannerForInteger=new Scanner(System.in);
        entityManger=HibernateUtil.getEntityManagerFactory().createEntityManager();
        customerRepository=new CustomerRepositoryImpl(entityManger);
        customerService=new CustomerServiceImpl(customerRepository);
        accountRepository=new AccountRepositoryImpl(entityManger);
        accountService=new AccountServiceImpl(accountRepository);
        bankcardRepository=new BankCardRepositoryImpl(entityManger);
        bankcardService=new BankCardServiceImpl(bankcardRepository);
        bankRepository=new BankRepositoryImpl(entityManger);
        bankService=new BankServiceImpl(bankRepository);
    }

    public static Scanner getScannerForString() {
        return scannerForString;
    }

    public static Scanner getScannerForInteger() {
        return scannerForInteger;
    }

    public static CustomerRepositoryImpl getCustomerRepository() {
        return customerRepository;
    }

    public static CustomerServiceImpl getCustomerService() {
        return customerService;
    }

    public static AccountServiceImpl getAccountService() {
        return accountService;
    }

    public static AccountRepositoryImpl getAccountRepository() {
        return accountRepository;
    }

    public static BankCardRepositoryImpl getBankcardRepository() {
        return bankcardRepository;
    }

    public static BankCardServiceImpl getBankcardService() {
        return bankcardService;
    }

    public static BankRepositoryImpl getBankRepository() {
        return bankRepository;
    }

    public static BankServiceImpl getBankService() {
        return bankService;
    }

    public static EntityManager getEntityManger() {
        return entityManger;
    }
}
