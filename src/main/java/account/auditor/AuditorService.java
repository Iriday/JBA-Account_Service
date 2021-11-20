package account.auditor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class AuditorService {
    private AuditorRepository auditorRepo;
    private SecurityEventMapper securityEventMapper;

    public List<SecurityEventDto> getAllSecurityEventsOrderedByIdAsc() {
        return securityEventMapper.securityEventsToSecurityEventDtos(auditorRepo.findAllByOrderByIdAsc());
    }

    public void saveSecurityEvent(SecurityEvent securityEvent) {
        if (securityEvent.getDate() == null) {
            securityEvent.setDate(new Date());
        }
        if (securityEvent.getPath() == null) {
            securityEvent.setPath(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath());
        }
        auditorRepo.save(securityEvent);
    }
}
