package backend.clientservice.services.impl;

import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;

public interface ClientService {
    ClientResponseDto add(ClientRequestDto dto);
}
