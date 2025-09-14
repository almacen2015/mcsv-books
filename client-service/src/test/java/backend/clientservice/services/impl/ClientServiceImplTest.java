package backend.clientservice.services.impl;

import backend.clientservice.models.entities.Client;
import backend.clientservice.repositories.ClientRepository;
import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import backend.enums.DocumentType;
import backend.enums.Gender;
import backend.exceptions.UtilException;
import backend.exceptions.client.ClientException;
import backend.exceptions.page.PageException;
import backend.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository repository;

    @InjectMocks
    private ClientServiceImpl service;

    @Test
    void testGetByDocumentNumber_whenDocumentTypeIsInvalid_returnsError() {
        ClientException exception = assertThrows(ClientException.class, () -> service.getByDocumentNumber("12345678", "AA"));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_DOCUMENT_TYPE);
    }

    @Test
    void testGetByDocumentNumber_whenCeHasNotNineDigits_returnsError() {
        ClientException exception = assertThrows(ClientException.class, () -> service.getByDocumentNumber("1234567", "CE"));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_CE);
    }

    @Test
    void testGetByDocumentNumber_whenDniHasNotEightDigits_returnsError() {
        ClientException exception = assertThrows(ClientException.class, () -> service.getByDocumentNumber("1234567", "DNI"));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_DNI);
    }

    @Test
    void testGetByDocumentNumber_whenValidDocumentNumber_returnsClient() {
        Client clientFound = buildClient(1L, "Victor", "Orbegozo", LocalDate.of(1994, 4, 5), 31, Gender.MALE, "12345678");
        when(repository.findByDocumentNumber(any(String.class))).thenReturn(Optional.of(clientFound));

        ApiResponseDto<ClientResponseDto> response = service.getByDocumentNumber("12345678", "DNI");

        assertThat(response).isNotNull();
        assertEquals(1L, response.data().id());
        assertEquals("Victor", response.data().name());
        assertEquals("Orbegozo", response.data().lastName());
        assertEquals("12345678", response.data().documentNumber());
        assertEquals(LocalDate.of(1994, 4, 5), response.data().birthDate());
    }

    @Test
    void testGetById_whenIdNotFound_returnsError() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        ClientException exception = assertThrows(ClientException.class, () -> service.getById(100L));

        assertEquals(ClientException.CLIENT_NOT_EXISTS, exception.getMessage());

    }

    @Test
    void testGetById_whenInvalidId_returnsError() {
        UtilException exception = assertThrows(UtilException.class, () -> service.getById(-1L));

        assertEquals(UtilException.ID_NOT_VALID, exception.getMessage());
    }

    @Test
    void testGetById_whenValidId_returnsClient() {
        Client clientFound = buildClient(1L, "Maria", "Rosas", LocalDate.of(1994, 7, 22), 30, Gender.FEMALE, "12345678");
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(clientFound));

        ApiResponseDto<ClientResponseDto> apiResponseDto = service.getById(1L);

        assertThat(apiResponseDto).isNotNull();
        assertEquals(1L, apiResponseDto.data().id());
        assertEquals("Maria", apiResponseDto.data().name());
        assertEquals(LocalDate.of(1994, 7, 22), apiResponseDto.data().birthDate());
        assertEquals(Gender.FEMALE.getCode(), apiResponseDto.data().gender());
    }

    @Test
    void testUpdate_whenValidData_returnsClient() {
        Client clientFound = buildClient(1L, "Maria", "Rosas", LocalDate.of(1994, 7, 22), 30, Gender.FEMALE, "12345678");
        ClientRequestDto request = buildClientRequest("Victor", "Orbegozo", "1994-04-05", "M", "12345678", "DNI");

        when(repository.findById(any())).thenReturn(Optional.of(clientFound));
        when(repository.save(any(Client.class))).thenReturn(clientFound);
        ApiResponseDto<ClientResponseDto> response = service.update(1L, request);

        assertThat(response.data()).isNotNull();
        assertEquals(request.name(), response.data().name());
        assertEquals(request.lastName(), response.data().lastName());
        assertEquals('M', response.data().gender());
        assertEquals(Utils.toLocalDate(request.birthDate()), response.data().birthDate());
    }

    @Test
    void testUpdate_whenGenderEmpty_returnsError() {
        ClientRequestDto request = buildClientRequest("Victor", "Orbegozo", "1994-04-05", " ", "12345678", "DNI");

        ClientException exception = assertThrows(ClientException.class, () -> service.update(1L, request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_GENDER);
    }

    @Test
    void testUpdate_whenGenderNull_returnsError() {
        ClientRequestDto request = buildClientRequest("Victor", "Orbegozo", "1994-04-05", null, "12345678", "DNI");

        ClientException exception = assertThrows(ClientException.class, () -> service.update(1L, request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_GENDER);
    }

    @Test
    void testUpdate_whenBirthdateIsNotValid_returnsError() {
        ClientRequestDto request = buildClientRequest("Victor", "Orbegozo", "1994-15-05", "M", "12345678", "DNI");

        UtilException exception = assertThrows(UtilException.class, () -> service.update(1L, request));

        assertThat(exception.getMessage()).isEqualTo(UtilException.DATE_NOT_VALID);
    }

    @Test
    void testUpdate_whenBirthdateIsNotAdult_returnsError() {
        ClientRequestDto request = buildClientRequest("Victor", "Orbegozo", "2020-12-05", "M", "12345678", "DNI");

        ClientException exception = assertThrows(ClientException.class, () -> service.update(1L, request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_BIRTHDATE);
    }

    @Test
    void testUpdate_whenBirthdateNull_returnsError() {
        ClientRequestDto request = buildClientRequest("Victor", "Orbegozo", null, "M", "12345678", "DNI");

        UtilException exception = assertThrows(UtilException.class, () -> service.update(1L, request));

        assertThat(exception.getMessage()).isEqualTo(UtilException.DATE_NOT_VALID);
    }

    @Test
    void testUpdate_whenLastNameEmpty_returnsError() {
        ClientRequestDto request = buildClientRequest("Victor", " ", "1994-04-05", "M", "12345678", "DNI");

        ClientException exception = assertThrows(ClientException.class, () -> service.update(1L, request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_LASTNAME);
    }

    @Test
    void testUpdate_whenLastNameNull_returnError() {
        ClientRequestDto request = buildClientRequest("Victor", null, "1994-04-05", "M", "12345678", "DNI");

        ClientException exception = assertThrows(ClientException.class, () -> service.update(1L, request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_LASTNAME);
    }

    @Test
    void testUpdate_whenNameEmpty_returnsError() {
        ClientRequestDto request = buildClientRequest(" ", "Orbegozo", "1994-04-05", "M", "12345678", "DNI");

        ClientException exception = assertThrows(ClientException.class, () -> service.update(1L, request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_NAME);
    }

    @Test
    void testUpdate_whenNameNull_returnsError() {

        ClientRequestDto request = buildClientRequest(null, "Orbegozo", "1994-04-05", "M", "12345678", "DNI");

        ClientException exception = assertThrows(ClientException.class, () -> service.update(1L, request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_NAME);
    }

    @Test
    void testUpdate_whenClientNotFound_ReturnsError() {
        ClientRequestDto dto = buildClientRequest("Victor", "Orbegozo", "1994-04-05", "M", "12345678", "DNI");

        when(repository.findById(any())).thenReturn(Optional.empty());

        ClientException exception = assertThrows(ClientException.class, () -> service.update(2L, dto));
        assertThat(exception.getMessage()).isEqualTo(ClientException.CLIENT_NOT_EXISTS);
    }

    @Test
    void testUpdate_whenInvalidId_returnsError() {
        ClientRequestDto dto = buildClientRequest("Victor", "Orbegozo", "1994-04-05", "M", "12345678", "DNI");
        UtilException exception = assertThrows(UtilException.class, () -> service.update(null, dto));
        assertThat(exception.getMessage()).isEqualTo(UtilException.ID_NOT_VALID);
    }

    @Test
    void testList_whenOrderByIsEmpty_returnsError() {
        PageException exception = assertThrows(PageException.class, () -> service.list(1, 5, " "));
        assertThat(exception.getMessage()).isEqualTo(PageException.SORT_NAME_INVALID);
    }

    @Test
    void testList_whenOrderByIsNull_returnsError() {
        PageException exception = assertThrows(PageException.class, () -> service.list(1, 5, null));
        assertThat(exception.getMessage()).isEqualTo(PageException.SORT_NAME_INVALID);
    }

    @Test
    void testList_whenSizeIsNull_returnsError() {
        PageException exception = assertThrows(PageException.class, () -> service.list(1, null, "id"));
        assertThat(exception.getMessage()).isEqualTo(PageException.SIZE_NUMBER_INVALID);
    }

    @Test
    void testList_whenSizeIsInvalid_returnsError() {
        PageException exception = assertThrows(PageException.class, () -> service.list(1, -1, "id"));
        assertThat(exception.getMessage()).isEqualTo(PageException.SIZE_NUMBER_INVALID);
    }

    @Test
    void testList_whenSizeIsZero_returnsError() {
        PageException exception = assertThrows(PageException.class, () -> service.list(1, 0, "id"));
        assertThat(exception.getMessage()).isEqualTo(PageException.SIZE_NUMBER_INVALID);
    }

    @Test
    void testList_whenPageNumberIsNull_returnsError() {
        PageException exception = assertThrows(PageException.class, () -> service.list(null, 5, "id"));
        assertThat(exception.getMessage()).isEqualTo(PageException.PAGE_NUMBER_INVALID);
    }

    @Test
    void testList_whenPageNumberIsInvalid_returnsError() {
        PageException exception = assertThrows(PageException.class, () -> service.list(-1, 5, "id"));
        assertThat(exception.getMessage()).isEqualTo(PageException.PAGE_NUMBER_INVALID);
    }

    @Test
    void testList_whenPageNumberIsZero_returnsError() {
        PageException exception = assertThrows(PageException.class, () -> service.list(0, 5, "id"));
        assertThat(exception.getMessage()).isEqualTo(PageException.PAGE_NUMBER_INVALID);
    }

    @Test
    void testList_whenPageDataValid_returnsEmpty() {
        Pageable pageable = PageRequest.of(1, 5);

        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(), pageable, 0));
        ApiResponseDto<Page<ClientResponseDto>> response = service.list(1, 5, "id");

        assertThat(response.data().getContent()).isEmpty();
    }

    @Test
    void testList_whenPageDataValid_returnsClients() {
        Client client1 = buildClient(1L, "Victor", "Orbegozo", LocalDate.of(1994, 4, 5), 30, Gender.MALE, "12345678");
        Client client2 = buildClient(2L, "Luis", "Mesa", LocalDate.of(1994, 4, 5), 30, Gender.MALE, "12345678");
        Client client3 = buildClient(3L, "Mario", "Mario", LocalDate.of(1994, 4, 5), 30, Gender.MALE, "12345678");

        Pageable pageable = PageRequest.of(1, 5);

        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(client1, client2, client3), pageable, 3));
        ApiResponseDto<Page<ClientResponseDto>> response = service.list(1, 5, "id");

        assertThat(response.data().getContent()).isNotNull();
        assertThat(response.data().getContent().size()).isEqualTo(3);
        assertThat(response.data().getContent().get(0).id()).isEqualTo(1L);
        assertThat(response.data().getContent().get(0).name()).isEqualTo("Victor");
        assertThat(response.data().getContent().get(2).id()).isEqualTo(3L);

    }

    @Test
    void testAdd_whenGenderInvalid_returnsError() {
        ClientRequestDto request = buildClientRequest("Victor", "Orbegozo", "1994-04-05", "A", "12345678", "DNI");

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_GENDER);
    }

    @Test
    void testAdd_whenGenderEmpty_returnsError() {
        ClientRequestDto request = buildClientRequest("Victor", "Orbegozo", "1994-04-05", " ", "12345678", "DNI");

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_GENDER);
    }

    @Test
    void testAdd_whenGenderNull_returnsError() {
        ClientRequestDto request = buildClientRequest("Victor", "Orbegozo", "1994-04-05", null, "12345678", "DNI");

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_GENDER);
    }

    @Test
    void testAdd_whenBirthdateIsNotAdult_returnsError() {
        ClientRequestDto request = buildClientRequest("Victor", "Orbegozo", "2020-10-05", "M", "12345678", "DNI");

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_BIRTHDATE);
    }

    @Test
    void testAdd_whenBirthdateInvalid_returnsError() {
        ClientRequestDto request = buildClientRequest("Victor", "Orbegozo", "1994-13-05", "M", "12345678", "DNI");

        UtilException exception = assertThrows(UtilException.class, () -> service.add(request));

        assertThat(exception.getMessage()).isEqualTo(UtilException.DATE_NOT_VALID);
    }

    @Test
    void testAdd_whenBirthdateNull_returnsError() {
        ClientRequestDto request = buildClientRequest("Victor", "Orbegozo", null, "M", "12345678", "DNI");

        UtilException exception = assertThrows(UtilException.class, () -> service.add(request));

        assertThat(exception.getMessage()).isEqualTo(UtilException.DATE_NOT_VALID);
    }

    @Test
    void testAdd_whenLastNameEmpty_returnsError() {
        ClientRequestDto request = buildClientRequest("Victor", " ", "1994-04-05", "M", "12345678", "DNI");

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_LASTNAME);
    }

    @Test
    void testAdd_whenLastNameNull_returnError() {
        ClientRequestDto request = buildClientRequest("Victor", null, "1994-04-05", "M", "12345678", "DNI");

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_LASTNAME);
    }

    @Test
    void testAdd_whenNameEmpty_returnsError() {
        ClientRequestDto request = buildClientRequest(" ", "Orbegozo", "1994-04-05", "M", "12345678", "DNI");

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_NAME);
    }

    @Test
    void testAdd_whenNameNull_returnsError() {
        ClientRequestDto request = buildClientRequest(null, "Orbegozo", "1994-04-05", "M", "12345678", "DNI");
        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_NAME);
    }

    @Test
    void testAdd_whenValidData_returnsClient() {
        Client client = buildClient(1L, "Victor", "Orbegozo", LocalDate.of(1994, 4, 5), 31, Gender.MALE, "12345678");
        ClientRequestDto request = buildClientRequest("Victor", "Orbegozo", "1994-05-04", "M", "12345678", "DNI");

        when(repository.save(any(Client.class))).thenReturn(client);

        ApiResponseDto<ClientResponseDto> response = service.add(request);

        assertNotNull(response.data());
        assertEquals(1L, response.data().id());
        assertEquals("Victor", response.data().name());
    }

    private ClientRequestDto buildClientRequest(String name, String lastName, String birthDate, String gender, String documentNumber, String documentType) {
        return new ClientRequestDto(name, lastName, birthDate, gender, documentNumber, documentType);
    }

    private Client buildClient(Long id, String name, String lastName, LocalDate birthDate, Integer age, Gender gender, String documentNumber) {
        return Client.builder()
                .id(id)
                .name(name)
                .lastName(lastName)
                .birthDate(birthDate)
                .age(age)
                .gender(gender.getCode())
                .documentNumber(documentNumber)
                .documentType(DocumentType.DNI.name())
                .build();
    }
}