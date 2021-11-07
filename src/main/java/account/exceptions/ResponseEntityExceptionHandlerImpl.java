package account.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;

@ControllerAdvice
public class ResponseEntityExceptionHandlerImpl extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String defaultMessage = ex
                .getBindingResult()
                .getAllErrors()
                .stream().findFirst()
                .map(ObjectError::getDefaultMessage)
                .orElse("No message");

        ErrorResponse responseEntity = ErrorResponse
                .builder()
                .timestamp(new Date())
                .error(status.getReasonPhrase())
                .status(status.value())
                .message(defaultMessage)
                .path(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath())
                .build();

        return new ResponseEntity<>(responseEntity, headers, status);
    }
}
