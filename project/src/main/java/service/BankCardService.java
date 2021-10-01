package service;

import base.service.BaseService;
import domain.BankCard;
import domain.User;

public interface BankCardService extends BaseService<BankCard,Long> {
    BankCard findBankCardOfUserWithBankNameAndNationalCode(String bankName,String nationalCode);
    BankCard findBankCardWithCardNumber(String cardNumber);
    void saveOrChangePassword(User user) throws Exception;
}
