package backend.reservationservice.controllers;

import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.reservation.requests.CreateReservationRequest;
import backend.dtos.reservation.responses.ReservationResponseDto;
import backend.reservationservice.models.entities.Reservation;
import backend.reservationservice.models.mapper.ReservationMapper;
import backend.reservationservice.services.impl.ReservationServiceImpl;
import backend.utils.Message;
import jakarta.validation.Valid;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationServiceImpl service;
    private final ReservationMapper mapper = ReservationMapper.INSTANCE;

    public ReservationController(ReservationServiceImpl service) {
        this.service = service;
    }

    @PatchMapping("/reservations/{id}/confirm/{paymentId}")
    public ResponseEntity<ApiResponseDto<ReservationResponseDto>> confirm(
            @PathVariable Long id, @PathVariable Long paymentId) {

        Reservation confirmed = service.confirm(id, paymentId);
        ReservationResponseDto response = mapper.toResponse(confirmed);

        ApiResponseDto<ReservationResponseDto> apiResponse =
                new ApiResponseDto<>(HttpStatus.OK.value(),
                        Message.RESERVATION_CONFIRMED,
                        response);

        final HttpHeaders headers = createHeader();

        return new ResponseEntity<>(apiResponse, headers, HttpStatus.OK);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponseDto<ReservationResponseDto>> cancel(
            @PathVariable Long id) {

        Reservation cancelled = service.cancel(id);
        ReservationResponseDto response = mapper.toResponse(cancelled);

        ApiResponseDto<ReservationResponseDto> apiResponse =
                new ApiResponseDto<>(HttpStatus.OK.value(),
                        Message.RESERVATION_CANCELLED,
                        response);

        final HttpHeaders headers = createHeader();

        return new ResponseEntity<>(apiResponse, headers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<ReservationResponseDto>> createReservation(@Valid @RequestBody CreateReservationRequest request) {
        Reservation reservationCreated = service.create(request.roomId(), request.clientId(), request.startDate(), request.endDate());
        ReservationResponseDto response = mapper.toResponse(reservationCreated);
        ApiResponseDto<ReservationResponseDto> apiResponseDto = new ApiResponseDto<>(HttpStatus.CREATED.value(), Message.RESERVATION_CREATED, response);

        final HttpHeaders headers = createHeader();

        return new ResponseEntity<>(apiResponseDto, headers, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ReservationResponseDto>> getReservation(@PathVariable Long id) {
        Reservation reservation = service.getById(id);
        ReservationResponseDto response = mapper.toResponse(reservation);

        ApiResponseDto<ReservationResponseDto> apiResponseDto = new ApiResponseDto<>(HttpStatus.FOUND.value(), Message.RESERVATION_FOUND, response);

        final HttpHeaders headers = createHeader();

        return new ResponseEntity<>(apiResponseDto, headers, HttpStatus.FOUND);

    }

    private HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Trace-Id", MDC.get("traceId"));
        return headers;
    }
}
