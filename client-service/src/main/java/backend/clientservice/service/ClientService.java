package backend.clientservice.service;

import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import org.springframework.data.domain.Page;

public interface ClientService {
    ClientResponseDto getByDocumentNumber(String documentNumber, String documentType);

    ApiResponseDto<ClientResponseDto> add(ClientRequestDto dto);

    Page<ClientResponseDto> list(Integer page, Integer size, String orderBy);

    ClientResponseDto getById(Long id);

    ApiResponseDto<ClientResponseDto> update(Long id, ClientRequestDto dto);
}
