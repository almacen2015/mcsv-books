package backend.reservationservice.repository;

import backend.enums.ReservationStatus;
import backend.reservationservice.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(""" 
            SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
            FROM Reservation r
            WHERE r.roomId = :roomId
                AND r.status IN :blockingStatuses
                AND r.startDate < :endDate
                AND r.endDate > :startDate
            """)
    boolean existsOverlappingReservation(Long roomId,
                                         Collection<ReservationStatus> blockingStatuses,
                                         LocalDate startDate,
                                         LocalDate endDate);

    @Query("""
                SELECT r FROM Reservation r
                WHERE r.status = :status
                AND r.createdAt <= :expirationTime
            """)
    List<Reservation> findExpiredReservations(
            ReservationStatus status,
            LocalDateTime expirationTime
    );

}
