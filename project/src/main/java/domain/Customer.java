package domain;

import domain.enumeration.UserType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public Customer(Long id){
        setId(id);
    }
    @Builder
    public Customer(String firstName, String lastName, LocalDate birthDay, String nationalCode, UserType userType) {
        super(firstName, lastName, birthDay, nationalCode, userType);
    }
}
