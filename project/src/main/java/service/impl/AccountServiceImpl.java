package service.impl;

import base.exception.BankNotFoundException;
import base.exception.BranchNotFoundException;
import base.exception.NotSuitableBankCardNumberFormatException;
import base.service.impl.BaseServiceImpl;
import domain.*;
import domain.embeddable.Transaction;
import domain.enumeration.AccountType;
import domain.enumeration.BankType;
import domain.enumeration.TransactionType;
import repository.impl.AccountRepositoryImpl;
import service.AccountService;
import service.util.ApplicationContext;
import service.util.SecurityContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountServiceImpl extends BaseServiceImpl<Account, Long, AccountRepositoryImpl>
        implements AccountService {

    private Random random;

    private CustomerServiceImpl customerService;
    private BankServiceImpl bankService;


    public AccountServiceImpl(AccountRepositoryImpl repository) {
        super(repository);
        random = new Random();
    }

    public void initialize() {
        this.customerService = ApplicationContext.getCustomerService();
        this.bankService = ApplicationContext.getBankService();
    }

    @Override
    public Account openAccount() throws Exception {
        String result = getNameOfBank();

        SecurityContext.getCurrentUser().setBankName(result);

        List<Account> listOfAccountConfirmInBank = customerService.allAccountInBankForCurrentUserExists(result);
        System.out.print("enter want you type account in bank from ");

        String selectedAccount = Arrays.stream(AccountType.values()).filter(x -> !listOfAccountConfirmInBank.stream()
                .map(Account::getAccountType)
                .collect(Collectors.toList()).contains(x)).
                map(Enum::name).collect(Collectors.joining(",", "(", ")"));

        System.out.println("   " + selectedAccount);

        AccountType accountType = AccountType.valueOf(ApplicationContext.getScannerForString().nextLine());

        if (listOfAccountConfirmInBank.stream().map(Account::getAccountType)
                .anyMatch(x -> x.equals(accountType)))
            throw new Exception("this account type in bank already exists");

        List<Bank> banks = bankService.findAllBank(result);

        System.out.println("enter branch number  from   " + banks.stream()
                .map(x -> String.valueOf(x.getId().getBranchNumber()))
                .collect(Collectors.joining(",", "(", ")")));

        long branchNumber = ApplicationContext.getScannerForInteger().nextLong();

        if (banks.stream().map(x -> x.getId().getBranchNumber()).noneMatch(x -> x.equals(branchNumber)))
            throw new BranchNotFoundException("dont any find branch");

        List<String> accountNumberExistsInBank = repository.accountNumberOfBank(result);

        String accountNumber = Stream.generate(() -> String.valueOf
                (random.nextInt(2000 - 1000) + 1000)).filter(x -> !accountNumberExistsInBank.contains(x))
                .findFirst().get();

        Bank bankOfConfirmedAccount = banks.stream().
                filter(x -> x.getId().getBranchNumber().equals(branchNumber))
                .findFirst().get();

        Account account;

        if (listOfAccountConfirmInBank.isEmpty())
            account = createAccountWithCreateBankCard(accountNumber, accountType, bankOfConfirmedAccount);
        else {
            BankCard bankCard = listOfAccountConfirmInBank.get(0).getBankCard();

            account = createAccountWithOutCreateBankCard(accountNumber, accountType, bankOfConfirmedAccount);
            account.setTransactionList(new ArrayList<>());
            account.setBankCard(bankCard);

        }
        return account;
    }

    private Account createAccountWithOutCreateBankCard(String accountNumber, AccountType accountType, Bank bankOfConfirmedAccount) {

        return Account.builder().accountNumber(accountNumber).accountType(accountType).bank
                (bankOfConfirmedAccount).inventory(resultOfInventoryWhenAddAccount()).
                createdAccount(LocalDateTime.now()).user(new User(SecurityContext.getCurrentUser().getId()))
                .bank(bankOfConfirmedAccount).build();
    }

    private Account createAccountWithCreateBankCard(String accountNumber, AccountType accountType, Bank bankOfConfirmedAccount) throws Exception {

        BankCard bankCard=createBankCard();
        Account account= Account.builder().accountNumber(accountNumber).accountType(accountType).createdAccount
                (LocalDateTime.now()).inventory(resultOfInventoryWhenAddAccount())
                .user(SecurityContext.getCurrentUser())
                .bank(bankOfConfirmedAccount).bankCard(bankCard).bank(bankOfConfirmedAccount)
                .transactionList(new ArrayList<>()).build();
                return account;
    }

    @Override
    public void showCurrentAccountOfUserWithNationalCode(String nationalCode) {
        repository.currentAccountOfUserWithNationalCode(nationalCode)
                .forEach(Account::print);
    }

    @Override
    public void withdrawal(User user) throws Exception {

        List<Account> accountList = user.getBankCards().get(0).getAccountList();
        System.out.println("enter witch one account from  \n " +
                accountList.stream().map(x -> x.getAccountType().name())
                        .collect(Collectors.joining(",","(",")")));

        String type = ApplicationContext.getScannerForString().nextLine();

        if (accountList.stream().noneMatch(x -> x.getAccountType().name().equals(type)))
            throw new Exception("this type not exists");

        Account account = accountList.stream().filter(x -> x.getAccountType().name().equals(type)).findFirst().get();

        System.out.println("enter how much amount withdrawal ");
        long amount = ApplicationContext.getScannerForInteger().nextLong();

        if (amount > account.getInventory())
            throw new Exception("dont money enough");

        account.setInventory(account.getInventory() - (amount));
        System.out.println("operation successfully withdrawal amount :" +
                " " + amount + "Toman");


        account.getTransactionList().add(new Transaction(TransactionType.HARVEST, amount, LocalDateTime.now()));
        super.update(account);
    }

    @Override
    public void showAllTransactionFromSpecifiedTime() throws Exception {

        String bankName = SecurityContext.getCurrentUser().getBankName();

        System.out.println("enter accountNumber");
        String accountNumber = ApplicationContext.getScannerForString().nextLine();
        Account account = repository.findAccountWithAccountNumber(accountNumber, bankName);

        System.out.println("enter date to calculated !!!");
        LocalDate date = LocalDate.parse(ApplicationContext.getScannerForString().nextLine());

        System.out.println("enter time to calculated !!!");
        LocalTime time = LocalTime.parse(ApplicationContext.getScannerForString().nextLine());
        LocalDateTime from = LocalDate.from(date).atTime(time);

        account.getTransactionList().stream().filter(x -> x.getTransactionTime().isAfter(from))
                .forEach(System.out::println);
    }

    private Long resultOfInventoryWhenAddAccount() {

        System.out.println("if you want enter some money enter yes other wise no\n");
        String result = ApplicationContext.getScannerForString().nextLine();

        return switch (result) {
            case "yes" -> {
                System.out.println("enter how much amount in your account");
                yield ApplicationContext.getScannerForInteger().nextLong();
            }

            case "no" -> 0L;

            default -> throw new IllegalStateException("Unexpected value: " + result);
        };
    }

    public void openAccountWhenUserRegistered(User user) throws Exception {
        Account account = openAccount();
        user.getBankCards().get(0).getAccountList().add(account); //new
        super.save(account);
    }

    private String getNameOfBank() throws Exception {
        System.out.println("enter your bank from ");
        Arrays.stream(BankType.values()).forEach(x -> System.out.println(x.name()));

        String result = ApplicationContext.getScannerForString().nextLine();

        try {
            BankType bankType = BankType.valueOf(result);
        } catch (Exception e) {
            throw new BankNotFoundException("any bank with this name not found");
        }
        return result;
    }

    private BankCard createBankCard() throws Exception {
        System.out.println("enter bankCardNumber");
        String bankCardNumber = ApplicationContext.getScannerForString().nextLine();

        Predicate<String> predicate1 = (x -> x.matches("[\\d]{12}"));
        Predicate<String> finalPredicate = predicate1.or(x -> x.matches("[\\d]{16}")).negate();

        if (finalPredicate.test(bankCardNumber))
            throw new NotSuitableBankCardNumberFormatException("we enter 12 digit or 16 digit");

        System.out.println("enter cvv2");
        String cvv2 = ApplicationContext.getScannerForString().nextLine();
        LocalDate expireTime = LocalDate.of(LocalDate.now().getYear() + 4, LocalDate.now().getMonth().getValue(),
                LocalDate.now().getDayOfMonth());

        return  BankCard.builder().cardNumber(bankCardNumber).cvv2Number(cvv2).expiration(expireTime)
                .accountList(new ArrayList<>()).user(new Customer(SecurityContext.getCurrentUser().getId())).build();
    }

}
