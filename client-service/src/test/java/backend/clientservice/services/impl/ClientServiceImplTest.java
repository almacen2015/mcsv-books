package backend.clientservice.services.impl;

import backend.clientservice.models.entities.Client;
import backend.clientservice.repositories.ClientRepository;
import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
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

//    @Mock
//    private ClientRepository repository;
//
//    @InjectMocks
//    private ClientServiceImpl service;
//
//    @Test
//    void testGetById_whenIdNotFound_returnsError() {
//        when(repository.findById(any())).thenReturn(Optional.empty());
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.getById(100L));
//
//        assertEquals(ClientException.CLIENT_NOT_EXISTS, exception.getMessage());
//
//    }
//
//    @Test
//    void testGetById_whenInvalidId_returnsError() {
//        UtilException exception = assertThrows(UtilException.class, () -> service.getById(-1L));
//
//        assertEquals(UtilException.ID_NOT_VALID, exception.getMessage());
//    }
//
//    @Test
//    void testGetById_whenValidId_returnsClient() {
//        Client clientFound = new Client(1L, "Maria", "Rosas", LocalDate.of(1994, 7, 22), 30, Gender.FEMALE.getCode());
//        when(repository.findById(any(Long.class))).thenReturn(Optional.of(clientFound));
//
//        ApiResponseDto<ClientResponseDto> apiResponseDto = service.getById(1L);
//
//        assertThat(apiResponseDto).isNotNull();
//        assertEquals(1L, apiResponseDto.data().id());
//        assertEquals("Maria", apiResponseDto.data().name());
//        assertEquals(LocalDate.of(1994, 7, 22), apiResponseDto.data().birthDate());
//        assertEquals(Gender.FEMALE.getCode(), apiResponseDto.data().gender());
//    }
//
//    @Test
//    void testUpdate_whenValidData_returnsClient() {
//        Client clientFound = new Client(1L, "Maria", "Rosas", LocalDate.of(1994, 7, 22), 30, Gender.FEMALE.getCode());
//        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", "1994-04-05", "M");
//
//        when(repository.findById(any())).thenReturn(Optional.of(clientFound));
//        when(repository.save(any(Client.class))).thenReturn(clientFound);
//        ApiResponseDto<ClientResponseDto> response = service.update(1L, request);
//
//        assertThat(response.data()).isNotNull();
//        assertEquals(request.name(), response.data().name());
//        assertEquals(request.lastName(), response.data().lastName());
//        assertEquals('M', response.data().gender());
//        assertEquals(Utils.toLocalDate(request.birthDate()), response.data().birthDate());
//    }
//
//    @Test
//    void testUpdate_whenGenderEmpty_returnsError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", "1994-04-05", " ");
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.update(1L, request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_GENDER);
//    }
//
//    @Test
//    void testUpdate_whenGenderNull_returnsError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", "1994-04-05", null);
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.update(1L, request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_GENDER);
//    }
//
//    @Test
//    void testUpdate_whenBirthdateIsNotValid_returnsError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", "1994-15-05", "M");
//
//
//        UtilException exception = assertThrows(UtilException.class, () -> service.update(1L, request));
//
//        assertThat(exception.getMessage()).isEqualTo(UtilException.DATE_NOT_VALID);
//    }
//
//    @Test
//    void testUpdate_whenBirthdateIsNotAdult_returnsError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", "2020-12-05", "M");
//
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.update(1L, request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_BIRTHDATE);
//    }
//
//    @Test
//    void testUpdate_whenBirthdateNull_returnsError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", null, "M");
//
//
//        UtilException exception = assertThrows(UtilException.class, () -> service.update(1L, request));
//
//        assertThat(exception.getMessage()).isEqualTo(UtilException.DATE_NOT_VALID);
//    }
//
//    @Test
//    void testUpdate_whenLastNameEmpty_returnsError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", " ", "1994-04-05", "M");
//
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.update(1L, request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_LASTNAME);
//    }
//
//    @Test
//    void testUpdate_whenLastNameNull_returnError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", null, "1994-04-05", "M");
//
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.update(1L, request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_LASTNAME);
//    }
//
//    @Test
//    void testUpdate_whenNameEmpty_returnsError() {
//        ClientRequestDto request = new ClientRequestDto(" ", "Orbegozo", "1994-04-05", "M");
//
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.update(1L, request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_NAME);
//    }
//
//    @Test
//    void testUpdate_whenNameNull_returnsError() {
//
//        ClientRequestDto request = new ClientRequestDto(null, "Orbegozo", "1994-04-05", "M");
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.update(1L, request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_NAME);
//    }
//
//    @Test
//    void testUpdate_whenClientNotFound_ReturnsError() {
//
//        when(repository.findById(any())).thenReturn(Optional.empty());
//        ClientRequestDto dto = new ClientRequestDto("Victor", "Orbegozo", "1994-04-05", "M");
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.update(2L, dto));
//        assertThat(exception.getMessage()).isEqualTo(ClientException.CLIENT_NOT_EXISTS);
//    }
//
//    @Test
//    void testUpdate_whenInvalidId_returnsError() {
//
//        ClientRequestDto dto = new ClientRequestDto("Victor", "Orbegozo", "1994-04-05", "M");
//        UtilException exception = assertThrows(UtilException.class, () -> service.update(null, dto));
//        assertThat(exception.getMessage()).isEqualTo(UtilException.ID_NOT_VALID);
//    }
//
//    @Test
//    void testList_whenOrderByIsEmpty_returnsError() {
//
//        PageException exception = assertThrows(PageException.class, () -> service.list(1, 5, " "));
//        assertThat(exception.getMessage()).isEqualTo(PageException.SORT_NAME_INVALID);
//    }
//
//    @Test
//    void testList_whenOrderByIsNull_returnsError() {
//
//        PageException exception = assertThrows(PageException.class, () -> service.list(1, 5, null));
//        assertThat(exception.getMessage()).isEqualTo(PageException.SORT_NAME_INVALID);
//    }
//
//    @Test
//    void testList_whenSizeIsNull_returnsError() {
//
//        PageException exception = assertThrows(PageException.class, () -> service.list(1, null, "id"));
//        assertThat(exception.getMessage()).isEqualTo(PageException.SIZE_NUMBER_INVALID);
//    }
//
//    @Test
//    void testList_whenSizeIsInvalid_returnsError() {
//
//        PageException exception = assertThrows(PageException.class, () -> service.list(1, -1, "id"));
//        assertThat(exception.getMessage()).isEqualTo(PageException.SIZE_NUMBER_INVALID);
//    }
//
//    @Test
//    void testList_whenSizeIsZero_returnsError() {
//
//        PageException exception = assertThrows(PageException.class, () -> service.list(1, 0, "id"));
//        assertThat(exception.getMessage()).isEqualTo(PageException.SIZE_NUMBER_INVALID);
//    }
//
//    @Test
//    void testList_whenPageNumberIsNull_returnsError() {
//
//        PageException exception = assertThrows(PageException.class, () -> service.list(null, 5, "id"));
//        assertThat(exception.getMessage()).isEqualTo(PageException.PAGE_NUMBER_INVALID);
//    }
//
//    @Test
//    void testList_whenPageNumberIsInvalid_returnsError() {
//
//        PageException exception = assertThrows(PageException.class, () -> service.list(-1, 5, "id"));
//        assertThat(exception.getMessage()).isEqualTo(PageException.PAGE_NUMBER_INVALID);
//    }
//
//    @Test
//    void testList_whenPageNumberIsZero_returnsError() {
//
//        PageException exception = assertThrows(PageException.class, () -> service.list(0, 5, "id"));
//        assertThat(exception.getMessage()).isEqualTo(PageException.PAGE_NUMBER_INVALID);
//    }
//
//    @Test
//    void testList_whenPageDataValid_returnsEmpty() {
//
//        Pageable pageable = PageRequest.of(1, 5);
//
//        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(), pageable, 0));
//        ApiResponseDto<Page<ClientResponseDto>> response = service.list(1, 5, "id");
//
//        assertThat(response.data().getContent()).isEmpty();
//    }
//
//    @Test
//    void testList_whenPageDataValid_returnsClients() {
//        Client client1 = new Client(1L, "Victor", "Orbegozo", LocalDate.of(1994, 4, 5), 30, 'M');
//        Client client2 = new Client(2L, "Luis", "Mesa", LocalDate.of(1994, 4, 5), 30, 'M');
//        Client client3 = new Client(3L, "Mario", "Mario", LocalDate.of(1994, 4, 5), 30, 'M');
//
//
//        Pageable pageable = PageRequest.of(1, 5);
//
//        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(client1, client2, client3), pageable, 3));
//        ApiResponseDto<Page<ClientResponseDto>> response = service.list(1, 5, "id");
//
//        assertThat(response.data().getContent()).isNotNull();
//        assertThat(response.data().getContent().size()).isEqualTo(3);
//        assertThat(response.data().getContent().get(0).id()).isEqualTo(1L);
//        assertThat(response.data().getContent().get(0).name()).isEqualTo("Victor");
//        assertThat(response.data().getContent().get(2).id()).isEqualTo(3L);
//
//    }
//
//    @Test
//    void testAdd_whenGenderInvalid_returnsError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", "1994-04-05", "A");
//
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_GENDER);
//    }
//
//    @Test
//    void testAdd_whenGenderEmpty_returnsError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", "1994-04-05", " ");
//
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_GENDER);
//    }
//
//    @Test
//    void testAdd_whenGenderNull_returnsError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", "1994-04-05", null);
//
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_GENDER);
//    }
//
//    @Test
//    void testAdd_whenBirthdateIsNotAdult_returnsError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", "2020-10-05", "M");
//
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_BIRTHDATE);
//    }
//
//    @Test
//    void testAdd_whenBirthdateInvalid_returnsError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", "1994-13-05", "M");
//
//
//        UtilException exception = assertThrows(UtilException.class, () -> service.add(request));
//
//        assertThat(exception.getMessage()).isEqualTo(UtilException.DATE_NOT_VALID);
//    }
//
//    @Test
//    void testAdd_whenBirthdateNull_returnsError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", null, "M");
//
//
//        UtilException exception = assertThrows(UtilException.class, () -> service.add(request));
//
//        assertThat(exception.getMessage()).isEqualTo(UtilException.DATE_NOT_VALID);
//    }
//
//    @Test
//    void testAdd_whenLastNameEmpty_returnsError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", " ", "1994-04-05", "M");
//
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_LASTNAME);
//    }
//
//    @Test
//    void testAdd_whenLastNameNull_returnError() {
//        ClientRequestDto request = new ClientRequestDto("Victor", null, "1994-04-05", "M");
//
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_LASTNAME);
//    }
//
//    @Test
//    void testAdd_whenNameEmpty_returnsError() {
//        ClientRequestDto request = new ClientRequestDto(" ", "Orbegozo", "1994-04-05", "M");
//
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_NAME);
//    }
//
//    @Test
//    void testAdd_whenNameNull_returnsError() {
//        ClientRequestDto request = new ClientRequestDto(null, "Orbegozo", "1994-04-05", "M");
//
//
//        ClientException exception = assertThrows(ClientException.class, () -> service.add(request));
//
//        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_NAME);
//    }
//
//    @Test
//    void testAdd_whenValidData_returnsClient() {
//        Client client = new Client(1L, "Victor", "Orbegozo", LocalDate.of(1994, 4, 5), 30, 'M');
//        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", "1994-05-04", "M");
//
//
//        when(repository.save(any(Client.class))).thenReturn(client);
//
//        ApiResponseDto<ClientResponseDto> response = service.add(request);
//
//        assertNotNull(response.data());
//        assertEquals(1L, response.data().id());
//        assertEquals("Victor", response.data().name());
//    }
}