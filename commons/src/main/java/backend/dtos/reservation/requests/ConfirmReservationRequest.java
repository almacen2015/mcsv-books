package backend.dtos.reservation.requests;

import jakarta.validation.constraints.NotNull;

public record ConfirmReservationRequest(@NotNull(message = "paymentId is required") Long paymentId) {
}
