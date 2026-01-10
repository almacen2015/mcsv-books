package backend.roomservice.exception;

import backend.exceptions.room.RoomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

@RestControllerAdvice
@Slf4j
public class RoomControllerAdvice {
    private static final Set<String> ERRORS_MESSAGES = Set.of(
            RoomException.ERROR_CAPACITY,
            RoomException.ERROR_DESCRIPTION,
            RoomException.ERROR_NUMBER,
            RoomException.ERROR_PRICE,
            RoomException.ERROR_STATUS,
            RoomException.ERROR_TYPE);

    @ExceptionHandler(RoomException.class)
    public ResponseEntity<?> handleException(RoomException e) {
        log.error(e.getMessage(), e);

        HttpStatus status = ERRORS_MESSAGES.contains(e.getMessage()) ?
                HttpStatus.BAD_REQUEST :
                HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status).body(e.getMessage());
    }

}
