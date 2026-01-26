package backend.reservationservice.services.impl;

import backend.enums.ReservationStatus;
import backend.exceptions.reservation.ReservationException;
import backend.reservationservice.models.entities.Reservation;
import backend.reservationservice.models.mapper.ReservationMapper;
import backend.reservationservice.repositories.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationServiceImpl {
    private final ReservationRepository repository;
    private final ReservationMapper mapper = ReservationMapper.INSTANCE;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.repository = reservationRepository;
    }

    @Transactional
    public Reservation create(Reservation reservation) {
        boolean conflict = repository.existsOverlappingReservation(reservation.getRoomId(),
                List.of(ReservationStatus.PAYMENT_PENDING, ReservationStatus.CONFIRMED),
                reservation.getStartDate(),
                reservation.getEndDate());

        if (conflict) {
            throw new ReservationException(ReservationException.ROOM_NOT_AVAILABLE);
        }

        return repository.save(reservation);
    }

    @Transactional
    public void confirm(Long id) {
        Reservation reservation = repository.findById(id)
                .orElseThrow(() -> new ReservationException(ReservationException.RESERVATION_NOT_FOUND));

        reservation.confirm();
    }

    @Transactional
    public void cancel(Long id) {
        Reservation reservation = repository.findById(id)
                .orElseThrow(() -> new ReservationException(ReservationException.RESERVATION_NOT_FOUND));

        reservation.cancel();
    }

    @Transactional
    public void expire(Long id) {
        Reservation reservation = repository.findById(id)
                .orElseThrow(() -> new ReservationException(ReservationException.RESERVATION_NOT_FOUND));

        reservation.expire();
    }
}
