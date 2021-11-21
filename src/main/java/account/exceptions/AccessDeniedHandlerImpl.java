package account.exceptions;

import account.auditor.AuditorService;
import account.auditor.Event;
import account.auditor.SecurityEvent;
import account.user.CurrentUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    private AuditorService auditorService;
    private CurrentUser currentUser;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        auditorService.saveSecurityEvent(SecurityEvent
                .builder()
                .action(Event.ACCESS_DENIED)
                .subject(currentUser.getCurrentUser().getUsername())
                .object(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath())
                .build());

        response.sendError(HttpStatus.FORBIDDEN.value(), "Access denied!");
    }
}
