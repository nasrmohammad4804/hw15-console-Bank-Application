package repository;

import base.repository.BaseRepository;
import domain.BankCard;

public interface BankCardRepository extends BaseRepository<BankCard,Long> {

    BankCard findBankCardOfUserWithBankNameAndNationalCode(String bankName,String nationalCode);

    BankCard findBankCardWithCardNumber(String cardNumber);

}
