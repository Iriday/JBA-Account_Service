package com.example.account_service.signup;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class SignupController {
    private UserService userService;
    private BCryptPasswordEncoder encoder;

    @PostMapping("api/auth/signup")
    public UserDto signup(@RequestBody @Valid UserDto userDto) {
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        return userService.signup(userDto);
    }
}
