package account.payment;

import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
public class PaymentController {
    private PaymentService paymentService;

    @PostMapping("api/acct/payments")
    public StatusDto addPayments(@RequestBody @UniqueElements List<@Valid PaymentDto> payments) {
        return paymentService.addPayments(payments);
    }

    @PutMapping("api/acct/payments")
    public StatusDto updatePayments(@RequestBody @Valid PaymentDto paymentDto) {
        return paymentService.updatePayment(paymentDto);
    }

    @GetMapping("api/empl/payment")
    public Object getPayment(@RequestParam(required = false) @Pattern(regexp = "(0[1-9]|1[0-2])-\\d{4}", message = "Period is incorrect") String period) {
        if (period != null) {
            return paymentService.getCurrentEmployeeDataByPeriod(period);
        } else {
            return paymentService.getAllCurrentEmployeeData();
        }
    }
}
