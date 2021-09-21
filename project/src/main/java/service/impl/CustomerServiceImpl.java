package service.impl;

import base.exception.BankNotFoundException;
import base.exception.PasswordNotValidException;
import base.exception.UserNotFoundException;
import base.service.impl.BaseServiceImpl;
import domain.Account;
import domain.BankCard;
import domain.Customer;
import domain.User;
import domain.embeddable.Transaction;
import domain.enumeration.AccountType;
import domain.enumeration.TransactionType;
import domain.enumeration.UserType;
import repository.impl.CustomerRepositoryImpl;
import service.CustomerService;
import service.util.ApplicationContext;
import service.util.SecurityContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long, CustomerRepositoryImpl>
        implements CustomerService {

    private AccountServiceImpl accountService;
    private BankCardServiceImpl bankCardService;
    private BankServiceImpl bankService;
   /* public static int NUMBER_ALLOWED_LOGIN;
    public static final int MAX_NUMBER_ALLOWED_LOGIN=3;*/

    public CustomerServiceImpl(CustomerRepositoryImpl repository) {
        super(repository);

    }
    public void initialize(){
        accountService = ApplicationContext.getAccountService();
        bankCardService = ApplicationContext.getBankcardService();
        bankService = ApplicationContext.getBankService();
    }

    @Override
    public List<Account> allAccountInBankForCurrentUserExists(String bankName) {
        return repository.allAccountInBankForCurrentUserExists(bankName);
    }

    @Override
    public void register() throws Exception {
        System.out.println("enter firstName");
        String firstName = ApplicationContext.getScannerForString().nextLine();

        System.out.println("enter  lastName");
        String lastName = ApplicationContext.getScannerForString().nextLine();

        System.out.println("enter your birthDay");
        LocalDate birthDay = LocalDate.parse(ApplicationContext.getScannerForString().nextLine());

        System.out.println("enter your nationalCode");
        String nationalCode = ApplicationContext.getScannerForString().nextLine();

        if (repository.allNationalCode().contains(nationalCode))
            throw new Exception("this nationalCode already exists");

        Customer customer = Customer.builder().firstName(firstName).lastName(lastName).birthDay(birthDay)
                .userType(UserType.CUSTOMER).nationalCode(nationalCode).build();


        entityManager.getTransaction().begin();
//        Account account = accountService.openAccount();

        entityManager.persist(customer);
//        entityManager.persist(account);
        entityManager.getTransaction().commit();

        //new
        entityManager.refresh(customer);
        SecurityContext.login(customer);
        accountService.openAccountWhenUserRegistered();
        //new
    }

    @Override
    public void login() throws Exception {

        System.out.println("enter nationalCode");
        String nationalCode = ApplicationContext.getScannerForString().nextLine();

        if (!repository.allNationalCode().contains(nationalCode))
            throw new UserNotFoundException("user not found");



        System.out.println("enter your bankName");
        String bankName = ApplicationContext.getScannerForString().nextLine();

        if (!bankService.findAllBank(bankName).stream().map
                (x -> x.getId().getBankName()).collect(Collectors.toList()).contains(bankName))
            throw new BankNotFoundException("bank with name not found exception");

        BankCard bankCard = bankCardService.findBankCardOfUserWithBankNameAndNationalCode(bankName, nationalCode);
        System.out.println("enter your password");
        String password = ApplicationContext.getScannerForString().nextLine();

        if (!bankCard.getPassword().equals(password)){
            //todo if 3 time wrong password block account
            throw new PasswordNotValidException("password dont correct");
        }

        User user = repository.findByNationalCode(nationalCode);

        user.getBankCards().add(bankCard);
        SecurityContext.login(user);
    }

    @Override
    public void cartToCart()  {

        try {
            Account myAccount = checkInformationMyAccount();
            long amount = checkAccountHasInventorySameTransferAmount(myAccount);
            if (amount == 0)
                throw new Exception("inventory is not enough !!!");

            Account destinationAccount = getInformationDestinationAccount();
            destinationAccount.setInventory(destinationAccount.getInventory() + amount);

            myAccount.setInventory(myAccount.getInventory() - (amount + Transaction.TRANSACTION_TRANSFER_WAGE));

            myAccount.getTransactionList().add
                    (new Transaction(TransactionType.TRANSFER, amount, LocalDateTime.now()));

            destinationAccount.getTransactionList().add(
                    new Transaction(TransactionType.DEPOSIT, amount, LocalDateTime.now()));

            entityManager.getTransaction().begin();
            entityManager.merge(myAccount);
            entityManager.merge(destinationAccount);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.print("\t\t try again\n");
            cartToCart();
        }
    }

    @Override
    public void showAllInventoryOfAccount() throws Exception {

        List<Account> accounts = SecurityContext.getCurrentUser().getBankCards().get(0).getAccountList();
        System.out.println("enter witch one account from   \n " +
                accounts.stream().map(x -> x.getAccountType().name()).collect(Collectors.joining()));

        String type = ApplicationContext.getScannerForString().nextLine();

        if (accounts.stream().noneMatch(x -> x.getAccountType().name().equals(type)))
            throw new Exception("this type not exists");

        Account account = accounts.stream().filter(x -> x.getAccountType().name().equals(type)).findFirst().get();


        if ( Transaction.TRANSACTION_HARVEST_WAGE > account.getInventory())
            throw new Exception("dont money enough");

        account.setInventory(account.getInventory() - (Transaction.TRANSACTION_HARVEST_WAGE ));
        System.out.println("operation successfully withdrawal :" +
              "with wage" + Transaction.TRANSACTION_HARVEST_WAGE + "Toman");

        System.out.println("inventory of "+account.getAccountType().name()+"  is : "+account.getInventory());
        accountService.update(account); //call super class of accountServiceImpl it is BaseServiceImpl


    }

    private long checkAccountHasInventorySameTransferAmount(Account account) {

        System.out.println("how much money send  ??");
        long amount = ApplicationContext.getScannerForInteger().nextLong();

        return amount + Transaction.TRANSACTION_TRANSFER_WAGE > account.getInventory() ? 0 : amount;

    }

    private Account checkInformationMyAccount() throws Exception {

        System.out.println("enter cardNumber of account");
        User user = SecurityContext.getCurrentUser();

        String cardNumber = ApplicationContext.getScannerForString().nextLine();

        if(!cardNumber.matches(SecurityContext.getCurrentUser().getBankCards().get(0).getCardNumber()))
            throw new Exception("cardNumber not valid must");

        System.out.println("enter account type from "+SecurityContext.getCurrentUser().getBankCards().get(0)
        .getAccountList().stream().map(x -> x.getAccountType().name()).collect(Collectors.joining()));

        String type=ApplicationContext.getScannerForString().nextLine();
        Optional<Account> myAccount = user.getBankCards().get(0).getAccountList().
                stream().filter(x -> x.getAccountNumber().equals(type)).findFirst();

        if (myAccount.isEmpty())
            throw new Exception("you have dont any account with accountNumber in bank ");

        System.out.println("enter cvv2");
        String cvv2 = ApplicationContext.getScannerForString().nextLine();

        System.out.println("enter expire date");
        LocalDate expire = LocalDate.parse(ApplicationContext.getScannerForString().nextLine());

        Predicate<String> predicate1 = (x -> x.equals(myAccount.get().getBankCard().getCvv2Number()));
        Predicate<LocalDate> predicate2 = (x -> x.equals(myAccount.get().getBankCard().getExpiration()));

        boolean resultOfSecondPassword=secondPasswordIsValid();

        if ( !(predicate1.test(cvv2) && predicate2.test(expire) && resultOfSecondPassword) ) {
            throw new Exception("cvv2 or expire time or secondPassword is wrong try again ");
        }
        return myAccount.get();
    }
    private boolean secondPasswordIsValid(){
        Random random=new Random();

        int secondPassword=random.nextInt(3000)+1000;
        System.out.println("your secondPassword is :"+secondPassword);

        System.out.println("enter secondPassword");
        int mySecondPassword=ApplicationContext.getScannerForInteger().nextInt();

        if(secondPassword==mySecondPassword)
            return true;

        else secondPasswordIsValid();
        return false;
    }

    private Account getInformationDestinationAccount() throws Exception {

        System.out.println("enter cardNumber of destination account");
        String cardNumber = ApplicationContext.getScannerForString().nextLine();

        BankCard destinationCard = bankCardService.findBankCardWithCardNumber(cardNumber);
        // todo -> check enter valid cardNumber

        Account destinationAccount = destinationCard.getAccountList().stream().filter
                (x -> x.getAccountType().equals(AccountType.CURRENT)).findAny().get();


        System.out.println("owner of account  is : " + destinationAccount.getUser().getFirstName() + "     " +
                destinationAccount.getUser().getLastName());

        System.out.println("if you confirm this transaction enter yes other wise no");

        return switch (ApplicationContext.getScannerForString().nextLine()) {
            case "yes" -> destinationAccount;
            case "no" -> throw new Exception("rollBack transaction \n");

            default -> throw new IllegalStateException("Unexpected value: ");
        };


    }
}
