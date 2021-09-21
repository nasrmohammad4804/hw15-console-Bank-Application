package service;

import base.service.BaseService;
import domain.Account;
import domain.Customer;

import java.util.List;

public interface CustomerService extends BaseService<Customer,Long> {
    List<Account> allAccountInBankForCurrentUserExists(String bankName);


    void register() throws Exception;

    void login() throws Exception;

    void cartToCart() ;

    void showAllInventoryOfAccount() throws Exception;

}
