package backend.clientservice.services;

import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;

public interface ClientService {
    ClientResponseDto add(ClientRequestDto dto);
}
