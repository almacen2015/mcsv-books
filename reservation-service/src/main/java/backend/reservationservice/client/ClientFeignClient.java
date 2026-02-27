package backend.reservationservice.client;

import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.responses.ClientResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-service")
public interface ClientFeignClient {

    @GetMapping("/{id}")
    ApiResponseDto<ClientResponseDto> findById(@PathVariable Long id);
}
