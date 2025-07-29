package backend.clientservice.controllers;

import backend.clientservice.security.TestSecurityConfig;
import backend.clientservice.services.ClientService;
import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@Import(TestSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAdd_WhenDataValid_ReturnClient() throws Exception {
        String traceId = UUID.randomUUID().toString();

        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", LocalDate.of(1994, 4, 5), "M");
        ClientResponseDto response = new ClientResponseDto(1L, "Victor", "Orbegozo", LocalDate.of(1994, 4, 5), 30, 'M');
        ApiResponseDto<ClientResponseDto> apiResponseDto = new ApiResponseDto<>(HttpStatus.CREATED.value(), "Cliente created", response, traceId);

        String json = objectMapper.writeValueAsString(request);

        when(service.add(any(ClientRequestDto.class), any(String.class))).thenReturn(apiResponseDto);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Victor"))
                .andExpect(jsonPath("$.data.lastName").value("Orbegozo"))
                .andExpect(jsonPath("$.data.birthDate").value("1994-04-05"))
                .andExpect(jsonPath("$.data.gender").value("M"))
                .andExpect(jsonPath("$.data.age").value(30));
    }

}