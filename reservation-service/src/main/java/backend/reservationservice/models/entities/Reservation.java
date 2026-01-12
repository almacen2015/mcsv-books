package backend.reservationservice.models.entities;

import backend.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations",
        indexes = {
                @Index(name = "idx_reservation_room", columnList = "room_id"),
                @Index(name = "idx_reservation_status", columnList = "status")
        })
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public void confirm() {
        if (this.status != ReservationStatus.PAYMENT_PENDING) {
            throw new IllegalStateException("Only reservations in PAYMENT_PENDING can be confirmed");
        }
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status != ReservationStatus.PAYMENT_PENDING) {
            throw new IllegalStateException("Only reservations in PAYMENT_PENDING can be cancelled");
        }
        this.status = ReservationStatus.CANCELLED;
    }

    public void expire() {
        if (this.status != ReservationStatus.PAYMENT_PENDING) {
            throw new IllegalStateException("Only reservations in PAYMENT_PENDING can be expired");
        }
        this.status = ReservationStatus.EXPIRED;
    }
}
