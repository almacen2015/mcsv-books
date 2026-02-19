package backend.reservationservice.services.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationExpirationJob {

    private final ReservationServiceImpl service;

    public ReservationExpirationJob(ReservationServiceImpl service) {
        this.service = service;
    }

    @Scheduled(fixedRate = 60000)
    public void expireReservations() {
        service.expirePendingReservations();
    }
}

