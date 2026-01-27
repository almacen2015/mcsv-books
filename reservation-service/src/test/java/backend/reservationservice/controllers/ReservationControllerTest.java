package backend.reservationservice.controllers;

import backend.dtos.reservation.requests.CreateReservationRequest;
import backend.reservationservice.models.entities.Reservation;
import backend.reservationservice.services.impl.ReservationServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationServiceImpl service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateReservation() throws Exception {
        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                10L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        Reservation reservation = new Reservation(
                1L,
                10L,
                request.startDate(),
                request.endDate());

        when(service.create(request.roomId(),
                request.clientId(),
                request.startDate(),
                request.endDate()))
                .thenReturn(reservation);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.roomId").value(1))
                .andExpect(jsonPath("$.data.clientId").value(10))
                .andExpect(jsonPath("$.data.status").value("PAYMENT_PENDING"));

    }

}