package backend.clientservice.controllers;

import backend.clientservice.services.impl.ClientService;
import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.client.responses.ClientResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar un cliente", description = "Registra un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente registrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PostMapping
    public ClientResponseDto add(@RequestBody ClientRequestDto dto) {
        return service.add(dto);
    }
}
