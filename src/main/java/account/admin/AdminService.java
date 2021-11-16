package account.admin;

import account.user.UserDto;
import account.user.UserMapper;
import account.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    public List<UserDto> getAllUsersOrderById() {
        return userMapper.usersToUserDtos(userRepository.findAllByOrderById());
    }
}
