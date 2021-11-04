package com.example.account_service.signup;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepo;
    private UserMapper userMapper;

    public UserDto signup(UserDto userDto) {
        if (userRepo.existsByEmailIgnoreCase(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exists!");
        }

        userDto.setEmail(userDto.getEmail().toLowerCase());

        User user = userRepo.save(userMapper.userDtoToUser(userDto));
        return userMapper.userToUserDto(user);
    }
}
