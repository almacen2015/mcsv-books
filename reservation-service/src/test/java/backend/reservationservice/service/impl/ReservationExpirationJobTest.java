package backend.reservationservice.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

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