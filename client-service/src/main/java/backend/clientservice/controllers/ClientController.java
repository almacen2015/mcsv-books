package backend.clientservice.controllers;

import backend.clientservice.services.ClientService;
import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import backend.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @Operation(summary = "List all clients", description = "List all clients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client saved"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "No authorized"),
            @ApiResponse(responseCode = "500", description = "Internal Error")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<ClientResponseDto>>> list(@RequestParam Integer page,
                                                                        @RequestParam Integer size,
                                                                        @RequestParam String orderBy) {
        final String traceId = Utils.generateTraceId();
        final ApiResponseDto<Page<ClientResponseDto>> response = service.list(page, size, orderBy, traceId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Trace-Id", traceId);

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
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
