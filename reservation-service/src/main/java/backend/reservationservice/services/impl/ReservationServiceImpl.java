package backend.reservationservice.services.impl;

import backend.dtos.reservation.requests.ReservationRequestDto;
import backend.dtos.reservation.responses.ReservationResponseDto;
import backend.reservationservice.repositories.ReservationRepository;
import backend.reservationservice.services.ReservationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository repository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.repository = reservationRepository;
    }

    @Override
    @Transactional
    public ReservationResponseDto crateReservation(ReservationRequestDto dto) {
        return null;
    }
}
