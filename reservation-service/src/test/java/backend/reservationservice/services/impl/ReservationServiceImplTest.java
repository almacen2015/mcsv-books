package backend.reservationservice.services.impl;

import backend.enums.ReservationStatus;
import backend.reservationservice.models.entities.Reservation;
import backend.reservationservice.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository repository;

    @InjectMocks
    private ReservationServiceImpl service;

    @Test
    void shouldCreateReservationWithPaymentPendingStatus() {
        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3));

        when(repository.existsOverlappingReservation(reservation.getRoomId(), List.of(ReservationStatus.PAYMENT_PENDING, ReservationStatus.CONFIRMED), reservation.getStartDate(), reservation.getEndDate())).thenReturn(false);

        Reservation reservationCreated = service.create(reservation);

        assertThat(reservationCreated).isNotNull();

        verify(repository).save(reservationCreated);
    }
}