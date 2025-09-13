package backend.clientservice.exception;

import backend.dtos.apiresponse.ApiResponseDto;
import backend.exceptions.client.ClientException;
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
            ClientException.ERROR_NAME,
            ClientException.ERROR_LASTNAME,
            ClientException.ERROR_GENDER,
            ClientException.ERROR_BIRTHDATE,
            ClientException.CLIENT_NOT_EXISTS,
            ClientException.ERROR_DOCUMENT_NUMBER,
            ClientException.ERROR_DNI,
            ClientException.ERROR_CE
    );

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ApiResponseDto<?>> handleClientException(ClientException e) {
        log.error(e.getMessage(), e);

        HttpStatus status = ERROR_MESSAGES.contains(e.getMessage()) ?
                HttpStatus.BAD_REQUEST :
                HttpStatus.INTERNAL_SERVER_ERROR;

        ApiResponseDto<Object> response = new ApiResponseDto<>(status.value(), e.getMessage(), null);

        return new ResponseEntity<>(response, status);
    }
}
