package domain;


import domain.embeddable.BankId;
import domain.enumeration.UserType;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends User {


    private Double salary;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns(value = {
            @JoinColumn(name = BankId.BANK_NAME, referencedColumnName = BankId.BANK_NAME),
            @JoinColumn(name = BankId.BRANCH_NUMBER, referencedColumnName = BankId.BRANCH_NUMBER)
    })
    private Bank bank;

    public void setUserType() {
        super.setUserType(UserType.EMPLOYEE);
    }
}
