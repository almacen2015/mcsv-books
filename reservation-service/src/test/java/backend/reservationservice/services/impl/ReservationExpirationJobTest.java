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
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationExpirationJobTest {

    @Mock
    private ReservationServiceImpl service;

    @InjectMocks
    private ReservationExpirationJob job;

    @Test
    void expireReservations_ShouldCallExpirePendingReservationsService() {

        job.expireReservations();

        verify(service).expirePendingReservations();
    }
}