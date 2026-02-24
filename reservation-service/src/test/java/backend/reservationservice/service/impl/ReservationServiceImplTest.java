package backend.reservationservice.service.impl;

import backend.dtos.reservation.responses.ReservationResponseDto;
import backend.enums.ReservationStatus;
import backend.exceptions.reservation.ReservationException;
import backend.reservationservice.model.entity.Reservation;
import backend.reservationservice.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    void expirePendingReservations_WhenReservationIsAlreadyConfirmed_ShouldThrowException() {

        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        reservation.confirm(10L);

        List<Reservation> expiredReservations = List.of(reservation);

        when(repository.findExpiredReservations(
                eq(ReservationStatus.PAYMENT_PENDING),
                any(LocalDateTime.class)
        )).thenReturn(expiredReservations);

        assertThatThrownBy(() -> service.expirePendingReservations())
                .isInstanceOf(IllegalStateException.class);
    }


    @Test
    void expirePendingReservations_WhenNoExpiredReservationsExist_ShouldDoNothing() {

        when(repository.findExpiredReservations(
                eq(ReservationStatus.PAYMENT_PENDING),
                any(LocalDateTime.class)
        )).thenReturn(Collections.emptyList());

        service.expirePendingReservations();

        verify(repository, never()).save(any());
    }


    @Test
    void expirePendingReservations_WhenExpiredReservationsExist_ShouldExpireThem() {

        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        List<Reservation> expiredReservations = List.of(reservation);

        when(repository.findExpiredReservations(
                eq(ReservationStatus.PAYMENT_PENDING),
                any(LocalDateTime.class)
        )).thenReturn(expiredReservations);

        service.expirePendingReservations();

        assertThat(reservation.getStatus())
                .isEqualTo(ReservationStatus.EXPIRED);
    }


    @Test
    void confirm_WhenReservationIsAlreadyConfirmed_ShouldThrowIllegalStateException() {

        Long id = 1L;
        Long paymentId = 100L;

        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        reservation.confirm(999L);

        when(repository.findById(id)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> service.confirm(id, paymentId))
                .isInstanceOf(IllegalStateException.class);
    }


    @Test
    void confirm_WhenReservationIsCancelled_ShouldThrowIllegalStateException() {

        Long id = 1L;
        Long paymentId = 100L;

        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        reservation.cancel();

        when(repository.findById(id)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> service.confirm(id, paymentId))
                .isInstanceOf(IllegalStateException.class);
    }


    @Test
    void confirm_WhenReservationDoesNotExist_ShouldThrowReservationNotFoundException() {

        Long id = 1L;
        Long paymentId = 100L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.confirm(id, paymentId))
                .isInstanceOf(ReservationException.class)
                .hasMessageContaining(ReservationException.RESERVATION_NOT_FOUND);
    }


    @Test
    void confirm_WhenReservationIsPaymentPending_ShouldConfirmSuccessfully() {

        Long id = 1L;
        Long paymentId = 100L;

        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        when(repository.findById(id)).thenReturn(Optional.of(reservation));

        Reservation confirmed = service.confirm(id, paymentId);

        assertThat(confirmed.getStatus())
                .isEqualTo(ReservationStatus.CONFIRMED);

        assertThat(confirmed.getPaymentId())
                .isEqualTo(paymentId);
    }


    @Test
    void shouldThrowExceptionWhenAlreadyCancelled() {

        Long id = 1L;

        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        reservation.cancel();

        when(repository.findById(id)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> service.cancel(id))
                .isInstanceOf(IllegalStateException.class);

        verify(repository, never()).save(any());
    }


    @Test
    void shouldThrowExceptionWhenReservationNotFoundCancel() {

        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cancel(id))
                .isInstanceOf(ReservationException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void shouldCancelReservationSuccessfully() {

        Long id = 1L;

        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        when(repository.findById(id)).thenReturn(Optional.of(reservation));
        when(repository.save(reservation)).thenReturn(reservation);

        ReservationResponseDto cancelled = service.cancel(id);

        assertThat(cancelled.status())
                .isEqualTo(ReservationStatus.CANCELLED.toString());

        verify(repository).save(reservation);
    }

    @Test
    void shouldThrowExceptionWhenReservationNotFound() {
        Long id = 99L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(ReservationException.class);

        verify(repository).findById(id);
    }

    @Test
    void shouldReturnReservationWhenFound() {
        Long id = 1L;

        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );

        when(repository.findById(id))
                .thenReturn(Optional.of(reservation));

        Reservation result = service.getById(id);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(reservation);

        verify(repository).findById(id);

    }

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