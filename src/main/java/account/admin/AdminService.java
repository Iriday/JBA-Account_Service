package account.admin;

import account.auditor.AuditorService;
import account.auditor.Event;
import account.auditor.SecurityEvent;
import account.security.Role;
import account.user.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

import static account.admin.AccessOperation.LOCK;
import static account.admin.AccessOperation.UNLOCK;
import static account.auditor.Event.LOCK_USER;
import static account.auditor.Event.UNLOCK_USER;
import static account.security.Role.*;

@Service
@AllArgsConstructor
@Transactional
public class AdminService {
    private UserRepository userRepo;
    private UserService userService;
    private UserMapper userMapper;
    private AuditorService auditorService;
    private CurrentUser currentUser;

    public List<UserDto> getAllUsersOrderById() {
        return userMapper.usersToUserDtos(userRepo.findAllByOrderById());
    }

    public StatusDto deleteUserByEmail(String email) {
        User user = userService.getUserByEmailIgnoreCase(email);

        if (user.getRoles().contains(ROLE_ADMINISTRATOR))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");

        userRepo.deleteById(user.getId());

        auditorService.saveSecurityEvent(SecurityEvent
                .builder()
                .action(Event.DELETE_USER)
                .subject(currentUser.getCurrentUser().getUsername())
                .object(user.getEmail())
                .build());

        return StatusDto
                .builder()
                .user(email)
                .status("Deleted successfully!")
                .build();
    }

    public UserDto changeUserRole(ChangeRoleDto changeRoleDto) {
        final User user = userService.getUserByEmailIgnoreCase(changeRoleDto.getUser());
        final List<Role> roles = user.getRoles();
        final Role role = Role
                .roleFromStr("ROLE_" + changeRoleDto.getRole().toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!"));

        switch (changeRoleDto.getOperation()) {
            case GRANT:
                auditorService.saveSecurityEvent(SecurityEvent
                        .builder()
                        .action(Event.GRANT_ROLE)
                        .subject(currentUser.getCurrentUser().getUsername())
                        .object("Grant role " + changeRoleDto.getRole() + " to " + user.getEmail())
                        .build());

                if (roles.contains(role)) {
                    break;
                }
                if (role == ROLE_ADMINISTRATOR || roles.contains(ROLE_ADMINISTRATOR)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user can't combine the administrative and business roles!");
                }

                user.getRoles().add(role);
                break;

            case REMOVE:
                auditorService.saveSecurityEvent(SecurityEvent
                        .builder()
                        .action(Event.REMOVE_ROLE)
                        .subject(currentUser.getCurrentUser().getUsername())
                        .object("Remove role " + changeRoleDto.getRole() + " from " + user.getEmail())
                        .build());

                if (!roles.contains(role)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
                }
                if (role == ROLE_ADMINISTRATOR) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
                }
                if (roles.size() == 1) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
                }

                roles.remove(role);
                break;
        }

        return userMapper.userToUserDto(userRepo.save(user));
    }

    public account.payment.StatusDto changeAccess(ChangeAccessDto dto) {
        User user = userService.getUserByEmailIgnoreCase(dto.getUser());

        if (dto.getOperation() == LOCK && user.getRoles().contains(ROLE_ADMINISTRATOR)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't lock the ADMINISTRATOR!");
        }

        user.setAccountNonLocked(dto.getOperation().boolValue);
        userRepo.save(user);


        SecurityEvent securityEvent = SecurityEvent
                .builder()
                .subject(currentUser.getCurrentUser().getUsername())
                .build();

        switch (dto.getOperation()) {
            case LOCK:
                securityEvent.setAction(LOCK_USER);
                securityEvent.setObject("Lock user " + dto.getUser());
                break;

            case UNLOCK:
                securityEvent.setAction(UNLOCK_USER);
                securityEvent.setObject("Unlock user " + dto.getUser());
                break;
        }
        auditorService.saveSecurityEvent(securityEvent);

        return new account.payment.StatusDto(String.format("User %s %s!", user.getEmail(), dto.getOperation().strValue));
    }
}
