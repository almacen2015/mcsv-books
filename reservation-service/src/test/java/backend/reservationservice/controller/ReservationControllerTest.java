package backend.reservationservice.controller;

import backend.dtos.reservation.requests.ConfirmReservationRequest;
import backend.dtos.reservation.requests.CreateReservationRequest;
import backend.dtos.reservation.responses.ReservationResponseDto;
import backend.enums.ReservationStatus;
import backend.exceptions.reservation.ReservationException;
import backend.reservationservice.model.entity.Reservation;
import backend.reservationservice.model.mapper.ReservationMapper;
import backend.reservationservice.service.impl.ReservationServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @MockitoBean
    private ReservationMapper mapper;

    @Test
    void confirmReservation_WhenPaymentIdIsNull_ShouldReturn400() throws Exception {

        Long reservationId = 1L;

        ConfirmReservationRequest confirmReservationRequest = new ConfirmReservationRequest(null);

        mockMvc.perform(patch("/api/reservations/{id}/confirm", reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmReservationRequest))
                )
                .andExpect(status().isBadRequest());
    }


    @Test
    void confirmReservation_WhenReservationNotFound_ShouldReturn404() throws Exception {

        Long reservationId = 1L;
        Long paymentId = 10L;

        when(service.confirm(reservationId, paymentId))
                .thenThrow(new ReservationException(ReservationException.RESERVATION_NOT_FOUND));

        ConfirmReservationRequest confirmReservationRequest = new ConfirmReservationRequest(paymentId);

        mockMvc.perform(patch("/api/reservations/{id}/confirm", reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmReservationRequest))
                )
                .andExpect(status().isNotFound());
    }


    @Test
    void confirmReservation_WhenValidCommand_ShouldReturnConfirmedReservation() throws Exception {

        Long reservationId = 1L;
        Long paymentId = 10L;

        ReservationResponseDto response = new ReservationResponseDto(
                1L,
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                ReservationStatus.CONFIRMED.name(),
                1L,
                LocalDateTime.now()
        );

        ConfirmReservationRequest confirmReservationRequest = new ConfirmReservationRequest(paymentId);

        when(service.confirm(reservationId, paymentId))
                .thenReturn(response);

        mockMvc.perform(patch("/api/reservations/{id}/confirm", reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmReservationRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status")
                        .value(ReservationStatus.CONFIRMED.toString()));
    }


    @Test
    void shouldCancelReservationSuccessfully() throws Exception {

        Long id = 1L;

        ReservationResponseDto response = new ReservationResponseDto(
                1L,
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                ReservationStatus.CANCELLED.name(),
                null,
                LocalDateTime.now()
        );

        when(service.cancel(id)).thenReturn(response);

        mockMvc.perform(patch("/api/reservations/{id}/cancel", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status")
                        .value("CANCELLED"));
    }


    @Test
    void shouldReturn404WhenReservationNotFound() throws Exception {
        Long id = 99L;

        when(service.getById(id))
                .thenThrow(new ReservationException(ReservationException.RESERVATION_NOT_FOUND));

        mockMvc.perform(get("/api/reservations/{id}", id))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldReturnReservationWhenFound() throws Exception {
        Long id = 1L;

        Reservation reservation = new Reservation(
                1L,
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );

        ReservationResponseDto responseDto = new ReservationResponseDto(
                id,
                1L,
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                "PAYMENT_PENDING",
                null,
                LocalDateTime.now()
        );

        when(service.getById(id)).thenReturn(reservation);
        when(mapper.toResponse(any(Reservation.class)))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/reservations/{id}", id))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.data.roomId").value(1))
                .andExpect(jsonPath("$.data.status").value("PAYMENT_PENDING"));
    }


    @Test
    void shouldReturn500WhenUnexpectedErrorOccurs() throws Exception {

        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2)
        );

        when(service.create(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenThrow(new RuntimeException("DB down"));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        CreateReservationRequest request = new CreateReservationRequest(
                null,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2)
        );

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateReservation() throws Exception {
        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                10L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        ReservationResponseDto response = new ReservationResponseDto(
                1L,
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                ReservationStatus.PAYMENT_PENDING.name(),
                null,
                LocalDateTime.now()
        );

        when(service.create(request.roomId(),
                request.clientId(),
                request.startDate(),
                request.endDate()))
                .thenReturn(response);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.roomId").value(1))
                .andExpect(jsonPath("$.data.clientId").value(1))
                .andExpect(jsonPath("$.data.status").value(ReservationStatus.PAYMENT_PENDING.name()));

    }

}