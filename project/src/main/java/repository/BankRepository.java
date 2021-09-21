package repository;

import domain.Bank;

import java.math.BigInteger;
import java.util.List;

public interface BankRepository  {

     List<Bank> findAllBank(String bankName);

     BigInteger countAll();

}
