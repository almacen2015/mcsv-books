package backend.clientservice.services;

import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import backend.dtos.pageable.PageableCustom;
import org.springframework.data.domain.Page;

public interface ClientService {
    ApiResponseDto<ClientResponseDto> add(ClientRequestDto dto, String traceId);

    ApiResponseDto<Page<ClientResponseDto>> list(Integer page, Integer size, String orderBy, String traceId);

    ApiResponseDto<ClientResponseDto> getById(Long id, String traceId);
}
