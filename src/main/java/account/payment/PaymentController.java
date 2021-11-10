package account.payment;

import account.security.UserDetailsImpl;
import account.user.UserDto;
import account.user.UserMapper;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
public class PaymentController {
    private PaymentService paymentService;
    private UserMapper userMapper;

    @PostMapping("api/acct/payments")
    public StatusDto addPayments(@RequestBody @UniqueElements List<@Valid PaymentDto> payments) {
        return paymentService.addPayments(payments);
    }

    @PutMapping("api/acct/payments")
    public StatusDto updatePayments(@RequestBody @Valid PaymentDto paymentDto){
         return paymentService.updatePayment(paymentDto);
    }

    @GetMapping("api/empl/payment")
    public UserDto payment(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return userMapper.userToUserDto(userDetailsImpl.getUserEntity());
    }
}
