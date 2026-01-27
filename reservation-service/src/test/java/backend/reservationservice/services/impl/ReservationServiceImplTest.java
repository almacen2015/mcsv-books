package backend.reservationservice.services.impl;

import backend.enums.ReservationStatus;
import backend.exceptions.reservation.ReservationException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository repository;

    @InjectMocks
    private ReservationServiceImpl service;

    @Test
    void shouldCreateReservationWithPaymentPendingStatus() {
        Long roomId = 1L;
        Long clientId = 1L;
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);

        Reservation reservation = new Reservation(
                roomId,
                clientId,
                startDate,
                endDate);

        when(repository.existsOverlappingReservation(reservation.getRoomId(), List.of(ReservationStatus.PAYMENT_PENDING, ReservationStatus.CONFIRMED), reservation.getStartDate(), reservation.getEndDate())).thenReturn(false);

        Reservation reservationCreated = service.create(roomId, clientId, startDate, endDate);

        assertThat(reservationCreated).isNotNull();

        verify(repository).save(reservationCreated);
    }

    @Test
    void shouldFailWhenRoomIsNotAvailable() {
        Long roomId = 1L;
        Long clientId = 2L;
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);

        Reservation reservation = new Reservation(
                roomId,
                clientId,
                startDate,
                endDate);

        when(repository.existsOverlappingReservation(reservation.getRoomId(), List.of(ReservationStatus.PAYMENT_PENDING, ReservationStatus.CONFIRMED), reservation.getStartDate(), reservation.getEndDate())).thenReturn(true);

        assertThatThrownBy(() ->
                service.create(roomId, clientId, startDate, endDate))
                .isInstanceOf(ReservationException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void shouldFailWhenEndDateIsBeforeStartDate() {
        Long roomId = 1L;
        Long clientId = 2L;
        LocalDate startDate = LocalDate.now().plusDays(3);
        LocalDate endDate = LocalDate.now().plusDays(1);

        assertThatThrownBy(() ->
                service.create(roomId, clientId, startDate, endDate))
                .isInstanceOf(ReservationException.class);

        verify(repository, never()).save(any());
    }
}