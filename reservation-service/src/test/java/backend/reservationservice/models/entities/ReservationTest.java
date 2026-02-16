package backend.reservationservice.models.entities;

import backend.enums.ReservationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationTest {

    @Test
    void shouldConfirmReservationWhenPendingPayment() {
        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3));

        reservation.confirm(1L);

        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
    }

    @Test
    void shouldNotConfirmReservationWhenAlreadyConfirmed() {
        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3));

        reservation.confirm(1L);

        assertThrows(IllegalStateException.class, () -> reservation.confirm(1L));
    }

    @Test
    void shouldCancelReservationWhenPendingPayment() {
        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3));

        reservation.cancel();

        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
    }
}