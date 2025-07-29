package backend.clientservice.controllers;

import backend.clientservice.services.ClientService;
import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import backend.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @Operation(summary = "Add a client", description = "Add a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client saved"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "No authorized"),
            @ApiResponse(responseCode = "500", description = "Internal Error")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDto<ClientResponseDto>> add(@RequestBody ClientRequestDto dto) {
        final String traceId = Utils.generateTraceId();
        final ApiResponseDto<ClientResponseDto> response = service.add(dto, traceId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Trace-Id", traceId);

        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }
}
