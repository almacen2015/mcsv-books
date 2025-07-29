package backend.clientservice.services.impl;

import backend.clientservice.models.entities.Client;
import backend.clientservice.repositories.ClientRepository;
import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import backend.exceptions.client.ClientException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

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
    void testAdd_WhenGenderInvalid_ReturnError() {
        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", LocalDate.of(1994, 4, 5), "A");
        String traceId = UUID.randomUUID().toString();

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request, traceId));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_GENDER);
    }

    @Test
    void testAdd_WhenGenderEmpty_ReturnError() {
        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", LocalDate.of(1994, 4, 5), " ");
        String traceId = UUID.randomUUID().toString();

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request, traceId));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_GENDER);
    }

    @Test
    void testAdd_WhenGenderNull_ReturnError() {
        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", LocalDate.of(1994, 4, 5), null);
        String traceId = UUID.randomUUID().toString();

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request, traceId));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_GENDER);
    }

    @Test
    void testAdd_WhenBirthdateInvalid_ReturnError() {
        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", LocalDate.of(1994, 13, 5), "M");
        String traceId = UUID.randomUUID().toString();

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request, traceId));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_BIRTHDATE);
    }

    @Test
    void testAdd_WhenBirthdateNull_ReturnError() {
        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", null, "M");
        String traceId = UUID.randomUUID().toString();

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request, traceId));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_BIRTHDATE);
    }

    @Test
    void testAdd_WhenLastNameEmpty_ReturnError() {
        ClientRequestDto request = new ClientRequestDto("Victor", " ", LocalDate.of(1994, 4, 5), "M");
        String traceId = UUID.randomUUID().toString();

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request, traceId));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_LASTNAME);
    }

    @Test
    void testAdd_WhenLastNameNull_ReturnError() {
        ClientRequestDto request = new ClientRequestDto("Victor", null, LocalDate.of(1994, 4, 5), "M");
        String traceId = UUID.randomUUID().toString();

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request, traceId));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_LASTNAME);
    }

    @Test
    void testAdd_WhenNameEmpty_ReturnError() {
        ClientRequestDto request = new ClientRequestDto(" ", "Orbegozo", LocalDate.of(1994, 4, 5), "M");
        String traceId = UUID.randomUUID().toString();

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request, traceId));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_NAME);
    }

    @Test
    void testAdd_WhenNameNull_ReturnError() {
        ClientRequestDto request = new ClientRequestDto(null, "Orbegozo", LocalDate.of(1994, 4, 5), "M");
        String traceId = UUID.randomUUID().toString();

        ClientException exception = assertThrows(ClientException.class, () -> service.add(request, traceId));

        assertThat(exception.getMessage()).isEqualTo(ClientException.ERROR_NAME);
    }

    @Test
    void testAdd_WhenValidData_ReturnClient() {
        Client client = new Client(1L, "Victor", "Orbegozo", LocalDate.of(1994, 4, 5), 30, 'M');
        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", LocalDate.of(1994, 4, 5), "M");
        String traceId = UUID.randomUUID().toString();

        when(repository.save(any(Client.class))).thenReturn(client);

        ApiResponseDto<ClientResponseDto> response = service.add(request, traceId);

        assertNotNull(response.data());
        assertEquals(1L, response.data().id());
        assertEquals("Victor", response.data().name());
    }
}