package service;

import domain.Bank;

import java.util.List;

public interface BankService {

     List<Bank> findAllBank(String bankName);
     void addDefaultBank();
     Long countAll();

}
