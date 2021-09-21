package domain;

import base.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_define",columnNames = {BankCard.CARD_NUMBER})
})
@NamedEntityGraph(name = "fetchBankCardWithAccounts",
    attributeNodes = {
        @NamedAttributeNode(value = "accountList")
    })
public class BankCard extends BaseEntity<Long> {

    public static final String  CVV2_NUMBER="cvv2_number";
    public static final String  CARD_NUMBER="card_number";
    public static final String TRANSACTION_OF_BANK_CARD="transaction_bank_card";

    @Column(name = CARD_NUMBER)
    private String cardNumber;

    @Column(name = CVV2_NUMBER)
    private String cvv2Number;

    @Column(nullable = false)
    private LocalDate expiration;

    @OneToMany(mappedBy = "bankCard",cascade = CascadeType.ALL)
    private List<Account> accountList=new ArrayList<>();

    private String password;
}
