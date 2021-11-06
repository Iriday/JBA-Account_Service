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

    public UserDto signup(UserDto userDto) {
        if (userRepo.existsByEmailIgnoreCase(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exists!");
        }

        userDto.setEmail(userDto.getEmail().toLowerCase());

        User user = userRepo.save(userMapper.userDtoToUser(userDto));
        return userMapper.userToUserDto(user);
    }

    public StatusDto changePass(PasswordDto passDto) {
        User currUser = currentUser.getCurrentUser().getUserEntity();

        if (passwordEncoder.matches(passDto.getNew_password(), currUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must be different!");
        }

        currUser.setPassword(passwordEncoder.encode(passDto.getNew_password()));
        userRepo.save(currUser);

        return new StatusDto(currUser.getEmail(), "The password has been updated successfully");
    }
}
