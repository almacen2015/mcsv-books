package backend.clientservice.controllers;

import backend.clientservice.security.TestSecurityConfig;
import backend.clientservice.services.ClientService;
import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import backend.enums.Gender;
import backend.utils.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@Import(TestSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private ClientService service;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void testGetById_whenValidId_returnsClient() throws Exception {
//        ClientResponseDto response = new ClientResponseDto(1L, "Susan", "Chapoñan", LocalDate.of(1994, 9, 22), 31, Gender.FEMALE.getCode());
//        ApiResponseDto<ClientResponseDto> apiResponseDto = new ApiResponseDto<>(HttpStatus.FOUND.value(), Message.CLIENT_FOUND, response);
//
//        when(service.getById(any(Long.class))).thenReturn(apiResponseDto);
//
//        mockMvc.perform(get("/api/clients/{id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isFound())
//                .andExpect(jsonPath("$.data.id").value(1L))
//                .andExpect(jsonPath("$.data.name").value("Susan"))
//                .andExpect(jsonPath("$.data.lastName").value("Chapoñan"))
//                .andExpect(jsonPath("$.data.gender").value("F"));
//    }
//
//    @Test
//    void testUpdate_whenValidData_returnsClient() throws Exception {
//        ClientResponseDto response = new ClientResponseDto(1L, "Susan", "Chapoñan", LocalDate.of(1994, 9, 22), 31, 'F');
//        ClientRequestDto dto = new ClientRequestDto("Susan", "Chapoñan", "1994-07-22", "F");
//
//        ApiResponseDto<ClientResponseDto> apiResponseDto = new ApiResponseDto<>(HttpStatus.CREATED.value(), Message.CLIENT_UPDATE, response);
//        String json = objectMapper.writeValueAsString(dto);
//
//        when(service.update(any(Long.class), any(ClientRequestDto.class))).thenReturn(apiResponseDto);
//
//        mockMvc.perform(put("/api/clients/{id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data.id").value(1L))
//                .andExpect(jsonPath("$.data.name").value("Susan"))
//                .andExpect(jsonPath("$.data.lastName").value("Chapoñan"))
//                .andExpect(jsonPath("$.data.birthDate").value("1994-09-22"))
//                .andExpect(jsonPath("$.data.gender").value("F"))
//                .andExpect(jsonPath("$.data.age").value(31));
//    }
//
//    @Test
//    void testList_WhenPageDataIsValid_ReturnsClients() throws Exception {
//        ClientResponseDto client1 = new ClientResponseDto(1L, "Victor", "Orbegozo", LocalDate.of(1994, 4, 5), 30, 'M');
//        ClientResponseDto client2 = new ClientResponseDto(2L, "Mario", "Mesa", LocalDate.of(1994, 4, 5), 20, 'M');
//        ClientResponseDto client3 = new ClientResponseDto(3L, "Susan", "Bonita", LocalDate.of(1994, 4, 5), 29, 'F');
//        List<ClientResponseDto> clients = List.of(client1, client2, client3);
//
//        Pageable pageable = PageRequest.of(1, 5);
//
//        ApiResponseDto<Page<ClientResponseDto>> response = new ApiResponseDto<>(HttpStatus.OK.value(), Message.OK, new PageImpl<>(clients, pageable, clients.size()));
//
//        when(service.list(any(Integer.class), any(Integer.class), any(String.class))).thenReturn(response);
//
//        mockMvc.perform(get("/api/clients")
//                        .param("page", "1")
//                        .param("size", "5")
//                        .param("orderBy", "id")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content.size()").value(clients.size()))
//                .andExpect(jsonPath("$.data.content[0].id").value(1L))
//                .andExpect(jsonPath("$.data.content[0].name").value("Victor"))
//                .andExpect(jsonPath("$.data.content[1].id").value(2L))
//                .andExpect(jsonPath("$.data.content[1].name").value("Mario"));
//    }
//
//    @Test
//    void testAdd_WhenDataValid_ReturnClient() throws Exception {
//        String traceId = UUID.randomUUID().toString();
//
//        ClientRequestDto request = new ClientRequestDto("Victor", "Orbegozo", "1994-04-05", "M");
//        ClientResponseDto response = new ClientResponseDto(1L, "Victor", "Orbegozo", LocalDate.of(1994, 4, 5), 30, 'M');
//        ApiResponseDto<ClientResponseDto> apiResponseDto = new ApiResponseDto<>(HttpStatus.CREATED.value(), "Cliente created", response);
//
//        String json = objectMapper.writeValueAsString(request);
//
//        when(service.add(any(ClientRequestDto.class))).thenReturn(apiResponseDto);
//
//        mockMvc.perform(post("/api/clients")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data.id").value(1L))
//                .andExpect(jsonPath("$.data.name").value("Victor"))
//                .andExpect(jsonPath("$.data.lastName").value("Orbegozo"))
//                .andExpect(jsonPath("$.data.birthDate").value("1994-04-05"))
//                .andExpect(jsonPath("$.data.gender").value("M"))
//                .andExpect(jsonPath("$.data.age").value(30));
//    }
}