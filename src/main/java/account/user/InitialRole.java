package account.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InitialRole {
    private final UserRepository userRepository;

    private final String USER = "ROLE_USER";
    private final String ADMIN = "ROLE_ADMINISTRATOR";

    public String getInitialRole() {
        return userRepository.count() == 0 ? ADMIN : USER;
    }
}
