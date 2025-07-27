package backend.authservice.controllers;

import backend.authservice.services.UserService;
import backend.authservice.services.impl.UserServiceImpl;
import backend.dtos.auth.requests.AuthLoginRequest;
import backend.dtos.auth.responses.AuthLoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService service;
    private final UserServiceImpl userDetailsService;

    public AuthController(UserService service, UserServiceImpl userDetailsService) {
        this.service = service;
        this.userDetailsService = userDetailsService;
    }

    @Operation(summary = "Authenticate", description = "Authenticate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authorized"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }
}
