package backend.reservationservice.service.impl;

import backend.dtos.reservation.responses.ReservationResponseDto;
import backend.enums.ReservationStatus;
import backend.exceptions.reservation.ReservationException;
import backend.reservationservice.model.entity.Reservation;
import backend.reservationservice.model.mapper.ReservationMapper;
import backend.reservationservice.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl {
    private final ReservationRepository repository;
    private final ReservationMapper mapper = ReservationMapper.INSTANCE;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.repository = reservationRepository;
    }

    public Reservation getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ReservationException(ReservationException.RESERVATION_NOT_FOUND));
    }

    @Transactional
    public Reservation create(Long roomId, Long clientId, LocalDate startDate, LocalDate endDate) {
        Reservation reservation = new Reservation(roomId, clientId, startDate, endDate);
        boolean conflict = repository.existsOverlappingReservation(reservation.getRoomId(),
                List.of(ReservationStatus.PAYMENT_PENDING, ReservationStatus.CONFIRMED),
                reservation.getStartDate(),
                reservation.getEndDate());

        if (conflict) {
            throw new ReservationException(ReservationException.ROOM_NOT_AVAILABLE);
        }

        repository.save(reservation);

        return reservation;
    }

    @Transactional
    public ReservationResponseDto confirm(Long id, Long paymentId) {
        Reservation reservation = repository.findById(id)
                .orElseThrow(() -> new ReservationException(ReservationException.RESERVATION_NOT_FOUND));

        reservation.confirm(paymentId);
        return mapper.toResponse(reservation);
    }

    @Transactional
    public ReservationResponseDto cancel(Long id) {
        Reservation reservation = repository.findById(id)
                .orElseThrow(() -> new ReservationException(ReservationException.RESERVATION_NOT_FOUND));

        reservation.cancel();

        Reservation cancelled = repository.save(reservation);

        return mapper.toResponse(cancelled);
    }

    @Transactional
    public void expirePendingReservations() {

        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(15);

        List<Reservation> expiredReservations =
                repository.findExpiredReservations(
                        ReservationStatus.PAYMENT_PENDING,
                        expirationTime
                );

        for (Reservation reservation : expiredReservations) {
            reservation.expire();
        }
    }

}
