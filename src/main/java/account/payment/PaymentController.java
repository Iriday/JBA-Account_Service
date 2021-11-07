package account.payment;

import account.security.UserDetailsImpl;
import account.user.UserDto;
import account.user.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PaymentController {
    private UserMapper userMapper;

    @GetMapping("api/empl/payment")
    public UserDto payment(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return userMapper.userToUserDto(userDetailsImpl.getUserEntity());
    }
}
