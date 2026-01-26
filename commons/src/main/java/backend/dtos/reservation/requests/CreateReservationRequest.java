package backend.dtos.reservation.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateReservationRequest(@NotNull Long roomId,
                                       @NotNull Long clientId,
                                       @NotNull LocalDate startDate,
                                       @NotNull LocalDate endDate) {
}
