package domain;

import base.domain.BaseEntity;
import domain.embeddable.BankId;
import domain.embeddable.Transaction;
import domain.enumeration.AccountType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.*;

import static domain.BankCard.CARD_NUMBER;
import static domain.BankCard.TRANSACTION_OF_BANK_CARD;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Account extends BaseEntity<Long> {

    public static final String ACCOUNT_NUMBER = "account_number";
    public static final String CREATED_ACCOUNT = "created_account";

    @Column(name = ACCOUNT_NUMBER)
    private String accountNumber;

    @Column(name = CREATED_ACCOUNT)
    private LocalDateTime createdAccount;

    private Long inventory;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = BankId.BANK_NAME, referencedColumnName = BankId.BANK_NAME),
            @JoinColumn(name = BankId.BRANCH_NUMBER, referencedColumnName = BankId.BRANCH_NUMBER)
    })
    private Bank bank;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = CARD_NUMBER)
    private BankCard bankCard;

    @ManyToOne
    private User user;

    @ElementCollection
    @JoinTable(name = TRANSACTION_OF_BANK_CARD,joinColumns = @JoinColumn(referencedColumnName = CARD_NUMBER))
    private List<Transaction> transactionList=new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private AccountType accountType;

    public void print(){

        System.out.println("bank :"+bank.getId().getBankName()+"  "+bank.getId().getBranchNumber()+
                "  inventory :"+inventory+"   "+"bankCard : "+bankCard.getCardNumber()+"  "+bankCard.getExpiration());
    }
}
