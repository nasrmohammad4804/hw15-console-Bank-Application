package domain.embeddable;

import domain.enumeration.TransactionType;
import lombok.*;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction  {

    public static final int TRANSACTION_TRANSFER_WAGE =600;
    public static final int TRANSACTION_HARVEST_WAGE=120;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private Long amount;

    private LocalDateTime transactionTime;
}
