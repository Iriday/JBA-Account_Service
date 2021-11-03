package com.example.account_service;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupController {

    @PostMapping("api/auth/signup")
    public UserDto signup(@RequestBody @Validated UserDto userDto) {
        return userDto;
    }
}
