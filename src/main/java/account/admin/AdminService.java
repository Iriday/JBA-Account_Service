package account.admin;

import account.security.Role;
import account.user.User;
import account.user.UserDto;
import account.user.UserMapper;
import account.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    public List<UserDto> getAllUsersOrderById() {
        return userMapper.usersToUserDtos(userRepository.findAllByOrderById());
    }

    public StatusDto deleteUserByEmail(String email) {
        User user = userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        if (user.getRoles().contains(Role.ROLE_ADMINISTRATOR))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");

        userRepository.deleteById(user.getId());

        return StatusDto
                .builder()
                .user(email)
                .status("Deleted successfully!")
                .build();
    }
}
