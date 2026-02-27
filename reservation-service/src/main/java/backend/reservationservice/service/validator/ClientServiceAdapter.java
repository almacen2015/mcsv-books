package backend.reservationservice.service.validator;

import backend.exceptions.reservation.ReservationException;
import backend.reservationservice.client.ClientFeignClient;
import feign.FeignException;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceAdapter implements ClientValidator {
    private final ClientFeignClient feignClient;

    public ClientServiceAdapter(ClientFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @Override
    public void validateClientExists(Long id) {
        try {
            feignClient.findById(id).data();
        } catch (FeignException.NotFound ex) {
            throw new ReservationException("Client not found");
        }
    }
}
