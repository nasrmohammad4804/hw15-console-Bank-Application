package repository;

import base.repository.BaseRepository;
import domain.Account;

import java.util.List;

public interface AccountRepository extends BaseRepository<Account,Long> {

    List<Account> currentAccountOfUserWithNationalCode(String nationalCode);
    List<String> accountNumberOfBank(String bankName);
    Account findAccountWithAccountNumber(String accountNumber,String bankName);

}
