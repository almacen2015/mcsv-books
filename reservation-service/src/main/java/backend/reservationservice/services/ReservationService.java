package backend.reservationservice.services;

import backend.dtos.reservation.requests.ReservationRequestDto;
import backend.dtos.reservation.responses.ReservationResponseDto;

public interface ReservationService {
    ReservationResponseDto crateReservation(ReservationRequestDto dto);
}
