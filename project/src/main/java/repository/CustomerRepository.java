package repository;

import base.repository.BaseRepository;
import domain.Account;
import domain.Customer;
import domain.User;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository  extends BaseRepository<Customer,Long> {

     List<Account> allAccountInBankForCurrentUserExists(String bankName);

    List<String> allNationalCode();

    Optional<Customer> findByNationalCode(String nationalCode);

}
