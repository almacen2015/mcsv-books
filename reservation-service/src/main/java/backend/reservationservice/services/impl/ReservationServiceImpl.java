package backend.reservationservice.services.impl;

import backend.enums.ReservationStatus;
import backend.exceptions.reservation.ReservationException;
import backend.reservationservice.models.entities.Reservation;
import backend.reservationservice.models.mapper.ReservationMapper;
import backend.reservationservice.repositories.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationServiceImpl {
    private final ReservationRepository repository;
    private final ReservationMapper mapper = ReservationMapper.INSTANCE;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.repository = reservationRepository;
    }

    @Transactional
    public Reservation create(Long roomId, Long clientId, LocalDate startDate, LocalDate endDate) {
        boolean conflict = repository.existsOverlappingReservation(roomId,
                List.of(ReservationStatus.PAYMENT_PENDING, ReservationStatus.CONFIRMED),
                startDate,
                endDate);

        if (conflict) {
            throw new ReservationException(ReservationException.ROOM_NOT_AVAILABLE);
        }

        Reservation reservation = new Reservation(roomId, clientId, startDate, endDate);

        return repository.save(reservation);
    }
}
