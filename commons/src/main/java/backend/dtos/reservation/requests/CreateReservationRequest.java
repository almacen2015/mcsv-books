package backend.dtos.reservation.requests;

import java.time.LocalDate;

public record CreateReservationRequest(Long roomId,
                                       Long clientId,
                                       LocalDate startDate,
                                       LocalDate endDate) {
}
