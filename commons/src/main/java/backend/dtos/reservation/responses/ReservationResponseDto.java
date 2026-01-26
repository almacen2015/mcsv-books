package backend.dtos.reservation.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservationResponseDto(Long id,
                                     Long roomId,
                                     Long clientId,
                                     LocalDate startDate,
                                     LocalDate endDate,
                                     String status,
                                     Long paymentId,
                                     LocalDateTime createdAt) {
}
