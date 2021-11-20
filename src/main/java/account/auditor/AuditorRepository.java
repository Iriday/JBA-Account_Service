package account.auditor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface AuditorRepository extends JpaRepository<SecurityEvent, BigInteger> {

    List<SecurityEvent> findAllByOrderByIdAsc();
}
