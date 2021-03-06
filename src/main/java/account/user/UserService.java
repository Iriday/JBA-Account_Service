package account.user;

import account.auditor.AuditorService;
import account.auditor.Event;
import account.auditor.SecurityEvent;
import account.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;


@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private UserRepository userRepo;
    private UserMapper userMapper;
    private CurrentUser currentUser;
    private BCryptPasswordEncoder passwordEncoder;
    private PasswordBlacklist passwordBlacklist;
    private AuditorService auditorService;

    public UserDto signup(UserDto userDto) {
        throwIfUserExistsByEmailIgnoreCase(userDto.getEmail());
        throwIfPasswordInBlackList(userDto.getPassword());

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setEmail(userDto.getEmail().toLowerCase());
        userDto.setRoles(List.of(getInitialRole()));

        User user = userRepo.save(userMapper.userDtoToUser(userDto));

        auditorService.saveSecurityEvent(SecurityEvent
                .builder()
                .action(Event.CREATE_USER)
                .subject("Anonymous")
                .object(user.getEmail())
                .build());

        return userMapper.userToUserDto(user);
    }

    public StatusDto changePass(PasswordDto passDto) {
        User currUser = currentUser.getCurrentUser().getUserEntity();

        throwIfPasswordsMatch(passDto.getNew_password(), currUser.getPassword());
        throwIfPasswordInBlackList(passDto.getNew_password());

        currUser.setPassword(passwordEncoder.encode(passDto.getNew_password()));
        userRepo.save(currUser);

        auditorService.saveSecurityEvent(SecurityEvent
                .builder()
                .action(Event.CHANGE_PASSWORD)
                .object(currUser.getEmail())
                .subject(currUser.getEmail())
                .build());

        return new StatusDto(currUser.getEmail(), "The password has been updated successfully");
    }

    public User getUserByEmailIgnoreCase(String email) {
        return userRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    private Role getInitialRole() {
        return userRepo.count() == 0 ? Role.ROLE_ADMINISTRATOR : Role.ROLE_USER;
    }

    // Exceptions

    private void throwIfPasswordsMatch(String rawPassword, String encodedPass) {
        if (passwordEncoder.matches(rawPassword, encodedPass)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must be different!");
        }
    }

    private void throwIfUserExistsByEmailIgnoreCase(String email) {
        if (userRepo.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exists!");
        }
    }

    private void throwIfPasswordInBlackList(String password) {
        if (passwordBlacklist.isPasswordInBlacklist(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
    }
}
