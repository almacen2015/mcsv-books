package backend.reservationservice.exception;

import backend.dtos.apiresponse.ApiResponseDto;
import backend.exceptions.reservation.ReservationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Set<String> ERROR_MESSAGES = Set.of(
            ReservationException.RESERVATION_NOT_FOUND,
            ReservationException.ROOM_NOT_AVAILABLE);

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<ApiResponseDto<?>> handleClientException(ReservationException e) {
        log.error(e.getMessage(), e);

        HttpStatus status = ERROR_MESSAGES.contains(e.getMessage()) ?
                HttpStatus.BAD_REQUEST :
                HttpStatus.INTERNAL_SERVER_ERROR;

        ApiResponseDto<Object> response = new ApiResponseDto<>(status.value(), e.getMessage(), null);

        return new ResponseEntity<>(response, status);
    }
}
