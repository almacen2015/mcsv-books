package backend.clientservice.services;

import backend.clientservice.repositories.ClientRepository;
import backend.clientservice.services.impl.ClientService;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClientResponseDto add(ClientRequestDto dto) {
        return null;
    }
}
