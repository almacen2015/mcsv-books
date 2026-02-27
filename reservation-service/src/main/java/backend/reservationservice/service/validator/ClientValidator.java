package backend.reservationservice.service.validator;

import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;

public interface ClientValidator {

    ApiResponseDto<ClientResponseDto> validateClientExists(ClientRequestDto request);
}
