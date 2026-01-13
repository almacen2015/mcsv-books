package backend.dtos.reservation.responses;

public record ReservationResponseDto(Long id,
                                     Long roomId,
                                     Long clientId,
                                     String startDate,
                                     String endDate,
                                     String status,
                                     Long paymentId,
                                     String createdAt) {
}
