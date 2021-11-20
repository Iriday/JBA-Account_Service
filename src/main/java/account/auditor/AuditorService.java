package account.auditor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuditorService {
    private AuditorRepository auditorRepo;
    private SecurityEventMapper securityEventMapper;

    public List<SecurityEventDto> getAllSecurityEventsOrderedByIdAsc(){
        return securityEventMapper.securityEventsToSecurityEventDtos(auditorRepo.findAllByOrderByIdAsc());
    }
}
