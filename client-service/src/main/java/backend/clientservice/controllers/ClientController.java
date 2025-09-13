package backend.clientservice.controllers;

import backend.clientservice.services.ClientService;
import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@SecurityRequirement(name = "BearerAuth")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @Operation(summary = "Get a client by documentNumber", description = "Get a client by documentNumber")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client saved"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "No authorized"),
            @ApiResponse(responseCode = "500", description = "Internal Error")
    })
    @GetMapping("/document")
    public ResponseEntity<ApiResponseDto<ClientResponseDto>> getByDocumentNumber(@RequestParam String documentNumber,
                                                                                 @RequestParam String documentType) {
        final ApiResponseDto<ClientResponseDto> response = service.getByDocumentNumber(documentNumber, documentType);
        final HttpHeaders headers = createHeader();
        return new ResponseEntity<>(response, headers, response.code());
    }

    @Operation(summary = "Update a client", description = "Update a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client saved"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "No authorized"),
            @ApiResponse(responseCode = "500", description = "Internal Error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ClientResponseDto>> update(@PathVariable Long id, @RequestBody ClientRequestDto request) {
        final ApiResponseDto<ClientResponseDto> response = service.update(id, request);
        final HttpHeaders headers = createHeader();
        return new ResponseEntity<>(response, headers, response.code());
    }


    @Operation(summary = "Get a client", description = "Get a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client saved"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "No authorized"),
            @ApiResponse(responseCode = "500", description = "Internal Error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ClientResponseDto>> getById(@PathVariable Long id) {
        final ApiResponseDto<ClientResponseDto> response = service.getById(id);
        final HttpHeaders headers = createHeader();
        return new ResponseEntity<>(response, headers, response.code());
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
        final ApiResponseDto<Page<ClientResponseDto>> response = service.list(page, size, orderBy);
        final HttpHeaders headers = createHeader();
        return new ResponseEntity<>(response, headers, response.code());
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
        final ApiResponseDto<ClientResponseDto> response = service.add(dto);
        final HttpHeaders headers = createHeader();
        return new ResponseEntity<>(response, headers, response.code());
    }

    private HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Trace-Id", MDC.get("traceId"));
        return headers;
    }
}
