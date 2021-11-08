package account.payment;

import account.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@Service
public class PaymentService {
    private PaymentRepository paymentRepository;
    private UserRepository userRepository;
    private PaymentMapper paymentMapper;

    @Transactional
    public StatusDto addPayments(List<PaymentDto> paymentDtos) {
        List<Payment> payments = paymentMapper.paymentDtosToPayments(paymentDtos);

        for (var payment : payments) {
            if (!userRepository.existsByEmailIgnoreCase(payment.getEmployee())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee with specified email not found");
            }

            if (paymentRepository.existsByEmployeeIgnoreCaseAndPeriod(payment.getEmployee(), payment.getPeriod())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Record already created");
            }

            paymentRepository.save(payment);
        }

        return new StatusDto("Added successfully!");
    }
}
