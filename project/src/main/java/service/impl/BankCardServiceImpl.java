package service.impl;

import base.exception.PasswordNotValidException;
import base.service.impl.BaseServiceImpl;
import domain.BankCard;
import domain.User;
import repository.impl.BankCardRepositoryImpl;
import service.BankCardService;
import service.util.ApplicationContext;
import service.util.SecurityContext;

public class BankCardServiceImpl extends BaseServiceImpl<BankCard, Long, BankCardRepositoryImpl>
        implements BankCardService {

    private CustomerServiceImpl customerService;

    public void initialize() {
        customerService = ApplicationContext.getCustomerService();
    }

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
    public void saveOrChangePassword(User user) throws Exception {

        String password = user.getBankCards().get(0).getPassword();

        if (password == null)
            setNewPassword(user);

        else changeOldPassword(user);
    }

    private void changeOldPassword(User user) throws Exception {
        System.out.println("1.change password");
        System.out.println("2.back");

        switch (ApplicationContext.getScannerForInteger().nextInt()) {

            case 1 -> {
                BankCard bankCard = user.getBankCards().get(0);
                System.out.println("enter old password");
                String oldPassword = ApplicationContext.getScannerForString().nextLine();

                if (!bankCard.getPassword().equals(oldPassword))
                    throw new PasswordNotValidException("password not valid ");

                System.out.println("enter newPassword");
                String newPassword = ApplicationContext.getScannerForString().nextLine();
                bankCard.setPassword(newPassword);
                update(bankCard);
            }
            case 2 -> System.out.println("operation of change password is canceled");
        }
    }

    private void setNewPassword(User user) {
        System.out.println("1.set new password");
        System.out.println("2.back");

        switch (ApplicationContext.getScannerForInteger().nextInt()) {

            case 1 -> {
                System.out.println("enter new Password");
                String newPassword = ApplicationContext.getScannerForString().nextLine();
                BankCard bankCard = user.getBankCards().get(0);
                bankCard.setPassword(newPassword);
                update(bankCard);
                SecurityContext.getCurrentUser().getBankCards().get(0).setPassword(newPassword);
            }
            case 2 -> System.out.println("operation of new password canceled");
        }
    }
}
