package backend.reservationservice.models.entities;

import backend.enums.ReservationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void shouldConfirmReservationWhenPendingPayment() {
        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3));

        reservation.confirm();

        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
    }

    @Test
    void shouldNotConfirmReservationWhenAlreadyConfirmed() {
        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3));

        reservation.confirm();

        assertThrows(IllegalStateException.class, reservation::confirm);
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