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
import java.util.Optional;

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
                " a.bank.id.bankName=:myBankName and a.user.nationalCode=:national_code ", Account.class)
                .setParameter("myBankName", bankName).setParameter
                        ("national_code", SecurityContext.getCurrentUser().getNationalCode()).getResultList();
    }

    @Override
    public List<String> allNationalCode() {

        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<User> root = criteriaQuery.from(User.class);

        criteriaQuery.select(root.get("nationalCode"));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<Customer> findByNationalCode(String nationalCode) {
        Optional<Customer> optional = Optional.empty();
        try {
            Customer customer = entityManager.createQuery("select c from Customer as c where" +
                            " c.nationalCode=:national_code",
                    Customer.class).setParameter("national_code", nationalCode).getSingleResult();

            return Optional.of(customer);
        } catch (Exception e) {
            return optional;
        }
    }
}
