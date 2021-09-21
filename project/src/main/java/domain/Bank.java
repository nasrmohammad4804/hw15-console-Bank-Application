package domain;

import domain.embeddable.Address;
import domain.embeddable.BankId;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bank {

    @EmbeddedId
    private BankId id;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "bank",cascade = CascadeType.ALL)
    private List<Account> accountList=new LinkedList<>();




}
