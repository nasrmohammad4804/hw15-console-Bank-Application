package repository.impl;

import base.repository.impl.BaseRepositoryImpl;
import domain.Account;
import domain.enumeration.AccountType;
import repository.AccountRepository;
import service.util.ApplicationContext;
import service.util.SecurityContext;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

public class AccountRepositoryImpl extends BaseRepositoryImpl<Account, Long>
        implements AccountRepository {

    public AccountRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<Account> getEntityClass() {
        return Account.class;
    }

    @Override
    public List<Account> currentAccountOfUserWithNationalCode(String nationalCode) {

        return entityManager.createQuery("select a from Account as a join a.user as u where " +
                "  u.nationalCode=:national_code and  a.accountType=:myType", Account.class)
                .setParameter("national_code", nationalCode).setParameter("myType", AccountType.CURRENT)
                .getResultList();
    }

    @Override
    public List<String> accountNumberOfBank(String bankName) {

        return entityManager.createQuery("select a.accountNumber from Account  as a " +
                " where a.bank.id.bankName=:bank_name", String.class)
                .setParameter("bank_name", bankName).getResultList();
    }

    @Override
    public Account findAccountWithAccountNumber(String accountNumber,String bankName) {

       return entityManager.createQuery("select a from Account  as a where a.bank.id.bankName=:bank_name " +
                "and a.accountNumber=:account_number",Account.class)
                .setParameter("bank_name",bankName).setParameter("account_number",accountNumber)
                .getSingleResult();

    }

}
