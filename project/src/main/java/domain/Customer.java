package domain;

import domain.enumeration.UserType;
import lombok.*;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
//to String

public class Customer extends User {

    public void setUserType() {
        super.setUserType(UserType.CUSTOMER);
    }

    @Builder
    public Customer(String firstName, String lastName, LocalDate birthDay, String nationalCode, UserType userType) {
        super(firstName, lastName, birthDay, nationalCode, userType);
    }
}
