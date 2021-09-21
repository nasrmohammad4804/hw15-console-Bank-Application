package service;

import base.service.BaseService;
import domain.BankCard;

public interface BankCardService extends BaseService<BankCard,Long> {
    BankCard findBankCardOfUserWithBankNameAndNationalCode(String bankName,String nationalCode);
    BankCard findBankCardWithCardNumber(String cardNumber);
    void saveOrChangePassword() throws Exception;
}
