package domain;

import base.domain.BaseEntity;
import domain.embeddable.Address;
import domain.enumeration.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
@Inheritance
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorColumn(name = User.ENTITY_NAME)
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_define",columnNames = {User.NATIONAL_CODE})})
public class User extends BaseEntity<Long> {


    public static final String FIRST_NAME="first_name";
    public static final String LAST_NAME="last_name";
    public static final String NATIONAL_CODE="national_code";
    public static final String BIRTHDAY="birthday";
    public static final String USER_ID="user_id";
    public static final String ENTITY_NAME="entity_name";

    @Column(name = FIRST_NAME,nullable = false)
    protected String firstName;

    @Column(name = LAST_NAME,nullable = false)
    protected String lastName;

    @Column(name= BIRTHDAY)
    protected LocalDate birthDay;

    @Column(name = NATIONAL_CODE)
    protected String nationalCode;

    @Transient
    protected UserType userType;

    @OneToMany
    @JoinColumn(name =USER_ID )
    private List<BankCard> bankCards=new LinkedList<>();


    public User(String firstName, String lastName, LocalDate birthDay, String nationalCode, UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.nationalCode = nationalCode;
        this.userType = userType;
    }

    @Embedded
    protected Address address;
}
