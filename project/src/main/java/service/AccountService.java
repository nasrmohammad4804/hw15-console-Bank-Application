package service;

import base.service.BaseService;
import domain.Account;
import domain.User;

public interface AccountService extends BaseService<Account,Long> {

    Account openAccount() throws Exception;
    void showCurrentAccountOfUserWithNationalCode(String nationalCode);
    void withdrawal(User user) throws Exception;
    void showAllTransactionFromSpecifiedTime()throws Exception ;


}
