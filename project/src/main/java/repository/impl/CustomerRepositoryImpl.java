package repository.impl;

import base.repository.impl.BaseRepositoryImpl;
import domain.Account;
import domain.Customer;
import domain.User;
import repository.CustomerRepository;
import service.util.SecurityContext;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CustomerRepositoryImpl extends BaseRepositoryImpl<Customer, Long>
        implements CustomerRepository {

    public CustomerRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<Customer> getEntityClass() {
        return Customer.class;
    }

    @Override
    public List<Account> allAccountInBankForCurrentUserExists(String bankName) {

        return entityManager.createQuery("select a from Account as a where" +
                " a.bank.id.bankName=:myBankName ", Account.class)
                        .setParameter("myBankName", bankName)
                .getResultList();
    }

    @Override
    public List<String> allNationalCode() {

        CriteriaQuery<String> criteriaQuery=criteriaBuilder.createQuery(String.class);
        Root<User> root=criteriaQuery.from(User.class);

        criteriaQuery.select(root.get("nationalCode"));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Customer findByNationalCode(String nationalCode) {
        return entityManager.createQuery("select c from Customer as c where c.nationalCode=:national_code",
                Customer.class).setParameter("national_code",nationalCode).getSingleResult();
    }
}
