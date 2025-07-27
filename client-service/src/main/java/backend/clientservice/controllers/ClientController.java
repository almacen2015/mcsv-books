package backend.clientservice.controllers;

import backend.clientservice.services.ClientService;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Add a client", description = "Add a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client saved"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "No authorized"),
            @ApiResponse(responseCode = "500", description = "Internal Error")
    })
    @PostMapping
    public ResponseEntity<ClientResponseDto> add(@RequestBody ClientRequestDto dto) {
        return new ResponseEntity<>(service.add(dto), HttpStatus.CREATED);
    }
}
