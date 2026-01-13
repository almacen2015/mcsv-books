package backend.dtos.reservation.requests;

public record ReservationRequestDto(Long roomId,
                                    Long clientId,
                                    String startDate,
                                    String endDate) {
}
