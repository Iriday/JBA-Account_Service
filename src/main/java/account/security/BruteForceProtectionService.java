package account.security;

import account.user.User;
import account.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Transactional
public class BruteForceProtectionService {
    private UserRepository userRepo;

    private final int MAX_LOGIN_FAIL_ATTEMPTS = 5;

    public void resetLoginFailCount(String email) {
        userRepo.setLoginFailCount(0, email);
    }

    public void resetUserLockIfLockedAndIf24HoursPassedAfterLock(String email) {
        User user = userRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        if (!user.isAccountNonLocked() && user.getLockedAt().plusDays(1).isBefore(LocalDate.now())) {
            user.setAccountNonLocked(true);
            user.setLoginFailCount(0);
            user.setLockedAt(null);
        }
    }

    public void incrementLoginFailCountAndLockAccountIfLimit(String email) {
        User user = userRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        user.setLoginFailCount(user.getLoginFailCount() + 1);

        if (user.getLoginFailCount() == MAX_LOGIN_FAIL_ATTEMPTS) {
            user.setLockedAt(LocalDate.now());
            user.setAccountNonLocked(false);
        }
    }
}
