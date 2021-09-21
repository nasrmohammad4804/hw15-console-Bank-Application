package service.impl;

import base.exception.PasswordNotValidException;
import base.service.impl.BaseServiceImpl;
import domain.BankCard;
import repository.impl.BankCardRepositoryImpl;
import service.BankCardService;
import service.util.ApplicationContext;
import service.util.SecurityContext;

public class BankCardServiceImpl extends BaseServiceImpl<BankCard, Long, BankCardRepositoryImpl>
        implements BankCardService {

    public BankCardServiceImpl(BankCardRepositoryImpl repository) {
        super(repository);
    }

    @Override
    public BankCard findBankCardOfUserWithBankNameAndNationalCode(String bankName, String nationalCode) {
        return repository.findBankCardOfUserWithBankNameAndNationalCode(bankName, nationalCode);
    }

    @Override
    public BankCard findBankCardWithCardNumber(String cardNumber) {
        return repository.findBankCardWithCardNumber(cardNumber);
    }

    @Override
    public void saveOrChangePassword() throws Exception {

        String password = SecurityContext.getCurrentUser().getBankCards().get(0).getPassword();

        if (password == null)
            setNewPassword();

        else changeOldPassword();

        assert password != null;
        if (!password.equals(SecurityContext.getCurrentUser().getBankCards().get(0).getPassword()))
            super.update(SecurityContext.getCurrentUser().getBankCards().get(0));
    }

    private void changeOldPassword() throws Exception {
        System.out.println("1.change password");
        System.out.println("2.back");

        switch (ApplicationContext.getScannerForInteger().nextInt()) {

            case 1 -> {
                System.out.println("enter new password");
                String oldPassword = ApplicationContext.getScannerForString().nextLine();

                if (!SecurityContext.getCurrentUser().getBankCards().get(0).getPassword().equals(oldPassword))
                    throw new PasswordNotValidException("password not valid ");

                System.out.println("enter newPassword");
                String newPassword = ApplicationContext.getScannerForString().nextLine();

                SecurityContext.getCurrentUser().getBankCards().get(0).setPassword(newPassword);
            }
            case 2 -> System.out.println("operation of change password is canceled");
        }
    }

    private void setNewPassword() {
        System.out.println("1.set new password");
        System.out.println("2.back");

        switch (ApplicationContext.getScannerForInteger().nextInt()) {

            case 1 -> {
                System.out.println("enter new Password");
                String newPassword = ApplicationContext.getScannerForString().nextLine();
                SecurityContext.getCurrentUser().getBankCards().get(0).setPassword(newPassword);
            }
            case 2 -> System.out.println("operation of new password canceled");
        }
    }
}
