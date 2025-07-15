package backend.clientservice.exception;

import backend.dtos.exceptions.client.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Set<String> ERROR_MESSAGES = Set.of(
            ClientException.ERROR_NAME
    );

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<String> handleClientException(ClientException e) {
        log.error(e.getMessage(), e);

        HttpStatus status = ERROR_MESSAGES.contains(e.getMessage()) ?
                HttpStatus.BAD_REQUEST :
                HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status).body(e.getMessage());
    }
}
