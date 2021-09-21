package domain.embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BankId implements Serializable {

    @Serial
    private  static   final long serialVersionUID=1L;

    public static final String BRANCH_NUMBER="branch_number";
    public static final String BANK_NAME="bank_name";

    @Column(name = BANK_NAME)
    private String bankName;

    @Column(name = BRANCH_NUMBER)
    private Long branchNumber;
}
