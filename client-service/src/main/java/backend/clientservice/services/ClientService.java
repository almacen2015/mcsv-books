package backend.clientservice.services;

import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;

public interface ClientService {
    ApiResponseDto<ClientResponseDto> add(ClientRequestDto dto, String traceId);
}
