package repository.impl;

import domain.Bank;
import repository.BankRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.List;

public class BankRepositoryImpl implements BankRepository {

    private EntityManager entityManager;

    public BankRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Bank> findAllBank(String bankName) {

        return entityManager.createQuery("select b from Bank  as b where b.id.bankName=:bank_name", Bank.class)
                .setParameter("bank_name", bankName).getResultList();
    }

    @Override
    public BigInteger countAll() {

        try {
            return (BigInteger) entityManager.createNativeQuery("select count(*) from bank")
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return BigInteger.valueOf(0);
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
