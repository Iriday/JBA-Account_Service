package account.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, BigInteger> {
    boolean existsByEmployeeIgnoreCaseAndPeriod(String employee, String period);

    Optional<Payment> findByEmployeeIgnoreCaseAndPeriod(String employee, String period);
}
