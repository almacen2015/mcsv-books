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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationServiceImpl service;
    private final ReservationMapper mapper = ReservationMapper.INSTANCE;

    public ReservationController(ReservationServiceImpl service) {
        this.service = service;
    }

    private HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Trace-Id", MDC.get("traceId"));
        return headers;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<ReservationResponseDto>> createReservation(@Valid @RequestBody CreateReservationRequest request){
        Reservation reservationCreated = service.create(request.roomId(), request.clientId(), request.startDate(), request.endDate());
        ReservationResponseDto response = mapper.toResponse(reservationCreated);
        ApiResponseDto<ReservationResponseDto> apiResponseDto =  new ApiResponseDto<>(HttpStatus.CREATED.value(), Message.CLIENT_CREATED, response);

        final HttpHeaders headers = createHeader();

        return new ResponseEntity<>(apiResponseDto, headers, HttpStatus.CREATED);
    }
}
