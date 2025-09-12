package backend.clientservice.services;

import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import org.springframework.data.domain.Page;

public interface ClientService {
    ApiResponseDto<ClientResponseDto> add(ClientRequestDto dto);

    ApiResponseDto<Page<ClientResponseDto>> list(Integer page, Integer size, String orderBy);

    ApiResponseDto<ClientResponseDto> getById(Long id);

    ApiResponseDto<ClientResponseDto> update(Long id, ClientRequestDto dto);
}
