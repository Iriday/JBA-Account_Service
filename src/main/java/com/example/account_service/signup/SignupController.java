package com.example.account_service.signup;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SignupController {
    private UserService userService;

    @PostMapping("api/auth/signup")
    public UserDto signup(@RequestBody @Validated UserDto userDto) {
        return userService.signup(userDto);
    }
}
