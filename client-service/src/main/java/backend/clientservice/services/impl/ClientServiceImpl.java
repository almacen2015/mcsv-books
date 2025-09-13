package backend.clientservice.services.impl;

import backend.clientservice.models.entities.Client;
import backend.clientservice.models.mappers.ClientMapper;
import backend.clientservice.repositories.ClientRepository;
import backend.clientservice.services.ClientService;
import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import backend.dtos.pageable.PageableCustom;
import backend.enums.Gender;
import backend.exceptions.client.ClientException;
import backend.utils.Message;
import backend.utils.Utils;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private static final Logger logger = LogManager.getLogger(ClientServiceImpl.class);
    private final ClientMapper mapper = ClientMapper.INSTANCE;

    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public ApiResponseDto<ClientResponseDto> getByDocumentNumber(String documentNumber, String documentType) {
        logger.info("getByDocumentNumber documentNumber: {} , documentType: {}", documentNumber, documentType);

        if (Utils.isInvalidString(documentNumber)) {
            throw new ClientException(ClientException.ERROR_DOCUMENT_NUMBER);
        }
        Utils.validateDocumentNumber(documentNumber, documentType);

        Client clientFound = repository.findByDocumentNumber(documentNumber).orElseThrow(() -> new ClientException(ClientException.ERROR_DOCUMENT_NUMBER));

        ClientResponseDto clientResponseDto = mapper.toDto(clientFound);

        ApiResponseDto<ClientResponseDto> response = new ApiResponseDto<>(HttpStatus.FOUND.value(), Message.CLIENT_FOUND, clientResponseDto);

        logger.info("getByDocumentNumber response: {}", response);

        return response;
    }

    @Override
    public ApiResponseDto<ClientResponseDto> update(Long id, ClientRequestDto dto) {
        logger.info("update id: {}, client update {}", id, dto);

        Utils.isValidId(id);
        Utils.validateClientDto(dto);
        Client clientFound = repository.findById(id).orElseThrow(() -> new ClientException(ClientException.CLIENT_NOT_EXISTS));

        clientFound.setName(dto.name());
        clientFound.setLastName(dto.lastName());
        char gender = Objects.equals(dto.gender(), String.valueOf(Gender.MALE.getCode())) ? Gender.MALE.getCode() : Gender.FEMALE.getCode();
        clientFound.setGender(gender);

        LocalDate date = Utils.toLocalDate(dto.birthDate());
        clientFound.setBirthDate(date);
        clientFound.setAge(Utils.getAge(date));
        clientFound.setDocumentNumber(dto.documentNumber());
        clientFound.setDocumentType(dto.documentType());

        ClientResponseDto response = mapper.toDto(repository.save(clientFound));

        logger.info("update response: {}", response);

        return new ApiResponseDto<>(HttpStatus.OK.value(), Message.CLIENT_UPDATE, response);
    }

    @Override
    public ApiResponseDto<ClientResponseDto> getById(Long id) {
        logger.info("getById Id: {}", id);

        Utils.isValidId(id);

        Client clientFound = repository.findById(id).orElseThrow(() -> new ClientException(ClientException.CLIENT_NOT_EXISTS));

        ClientResponseDto response = mapper.toDto(clientFound);

        logger.info("getById response: {}", response);

        return new ApiResponseDto<>(HttpStatus.FOUND.value(), Message.CLIENT_FOUND, response);
    }

    @Override
    public ApiResponseDto<Page<ClientResponseDto>> list(Integer page, Integer size, String orderBy) {
        PageableCustom pageableCustom = new PageableCustom(page, size, orderBy);
        Utils.validatePagination(pageableCustom);
        Pageable pageable = Utils.constructPageable(pageableCustom);

        Page<Client> clients = repository.findAll(pageable);

        List<ClientResponseDto> data = clients.getContent().stream()
                .map(mapper::toDto).toList();

        return new ApiResponseDto<>(HttpStatus.OK.value(), Message.OK, new PageImpl<>(data, pageable, clients.getTotalElements()));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ApiResponseDto<ClientResponseDto> add(ClientRequestDto dto) {
        logger.info("Add data client: {}", dto.toString());

        Utils.validateClientDto(dto);

        Client client = mapper.toEntity(dto);
        client.setAge(Utils.getAge(client.getBirthDate()));

        ClientResponseDto response = mapper.toDto(repository.save(client));

        logger.info("Add response: {}", response);
        return new ApiResponseDto<>(HttpStatus.CREATED.value(), Message.CLIENT_CREATED, response);
    }
}
