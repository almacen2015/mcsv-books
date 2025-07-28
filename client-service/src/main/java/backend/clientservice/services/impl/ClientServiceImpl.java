package backend.clientservice.services.impl;

import backend.clientservice.repositories.ClientRepository;
import backend.clientservice.services.ClientService;
import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import backend.utils.Utils;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private static final Logger logger = LogManager.getLogger(ClientServiceImpl.class);

    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ApiResponseDto<ClientResponseDto> add(ClientRequestDto dto, String traceId) {
        logger.info("[{}] Data client: {}", traceId, dto.toString());
        Utils.validateClientDto(dto);
        return null;
    }
}
