package account.payment;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PaymentDto {
    @NotBlank
    private String employee;
    @NotNull
    @Pattern(regexp = "(0[1-9]|1[0-2])-\\d{4}", message = "Period is incorrect")
    private String period;
    @NotNull
    @Min(value = 0)
    private Long salary;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentDto that = (PaymentDto) o;
        return getEmployee().equals(that.getEmployee()) && getPeriod().equals(that.getPeriod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployee(), getPeriod());
    }
}
