package repository.impl;

import base.repository.impl.BaseRepositoryImpl;
import domain.Bank;
import domain.BankCard;
import repository.BankCardRepository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class BankCardRepositoryImpl extends BaseRepositoryImpl<BankCard, Long>
        implements BankCardRepository {

    public static final String FETCH_GRAPH = "javax.persistence.fetchgraph";

    public BankCardRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }


    @Override
    public Class<BankCard> getEntityClass() {
        return BankCard.class;
    }

    @Override
    public BankCard findBankCardOfUserWithBankNameAndNationalCode(String bankName, String nationalCode) {

        EntityGraph<?> entityGraph = entityManager.getEntityGraph("fetchBankCardWithAccounts");
        return entityManager.createQuery
                ("select a.bankCard from Account as  a where a.user.nationalCode=:national_code " +
                        "and a.bank.id.bankName=:bank_name ", BankCard.class)
                .setParameter("national_code", nationalCode).setHint(FETCH_GRAPH, entityGraph)
                .setParameter("bank_name", bankName).getSingleResult();
    }

    @Override
    public BankCard findBankCardWithCardNumber(String cardNumber) {

        CriteriaQuery<BankCard> criteriaQuery = criteriaBuilder.createQuery(BankCard.class);
        Root<BankCard> root = criteriaQuery.from(BankCard.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("cardNumber"), cardNumber));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
