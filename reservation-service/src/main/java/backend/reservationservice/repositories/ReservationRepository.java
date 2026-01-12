package backend.reservationservice.repositories;

import backend.enums.ReservationStatus;
import backend.reservationservice.models.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;

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
                                         LocalDateTime startDate,
                                         LocalDateTime endDate);
}
