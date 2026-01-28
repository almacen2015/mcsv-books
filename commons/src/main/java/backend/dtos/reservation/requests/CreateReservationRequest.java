package backend.dtos.reservation.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateReservationRequest(@NotNull(message = "roomId is required") Long roomId,
                                       @NotNull(message = "clientId is required") Long clientId,
                                       @NotNull(message = "startDate is required") LocalDate startDate,
                                       @NotNull(message = "endDate is required") LocalDate endDate) {
}
