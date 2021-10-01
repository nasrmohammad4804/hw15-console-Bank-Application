package service;

import base.service.BaseService;
import domain.Account;
import domain.Customer;
import domain.User;

import java.util.List;

public interface CustomerService extends BaseService<Customer,Long> {
    List<Account> allAccountInBankForCurrentUserExists(String bankName);


    User register() throws Exception;

    User login() throws Exception;

    void cartToCart(User user) ;

    void showAllInventoryOfAccount(User user) throws Exception;

}
