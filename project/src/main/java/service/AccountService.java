package service;

import base.service.BaseService;
import domain.Account;

public interface AccountService extends BaseService<Account,Long> {

    Account openAccount() throws Exception;
    void showCurrentAccountOfUserWithNationalCode(String nationalCode);
    void withdrawal() throws Exception;
    void showAllTransactionFromSpecifiedTime()throws Exception ;


}
