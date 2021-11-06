package com.example.account_service.signup;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepo;
    private UserMapper userMapper;
    private CurrentUser currentUser;
    private BCryptPasswordEncoder passwordEncoder;
    private PasswordBlacklist passwordBlacklist;

    public UserDto signup(UserDto userDto) {
        if (userRepo.existsByEmailIgnoreCase(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exists!");
        }

        throwIfPasswordInBlackList(userDto.getPassword());

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setEmail(userDto.getEmail().toLowerCase());

        User user = userRepo.save(userMapper.userDtoToUser(userDto));
        return userMapper.userToUserDto(user);
    }

    public StatusDto changePass(PasswordDto passDto) {
        User currUser = currentUser.getCurrentUser().getUserEntity();

        if (passwordEncoder.matches(passDto.getNew_password(), currUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must be different!");
        }

        throwIfPasswordInBlackList(passDto.getNew_password());

        currUser.setPassword(passwordEncoder.encode(passDto.getNew_password()));
        userRepo.save(currUser);

        return new StatusDto(currUser.getEmail(), "The password has been updated successfully");
    }

    private void throwIfPasswordInBlackList(String password) {
        if (passwordBlacklist.isPasswordInBlacklist(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
    }
}
