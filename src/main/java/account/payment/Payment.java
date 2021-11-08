package account.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Payment {
    @Id
    @GeneratedValue
    private BigInteger id;
    private String employee;
    private String period;
    private Long salary;
}
