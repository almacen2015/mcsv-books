package backend.roomservice.controller;

import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.room.request.RoomRequestDto;
import backend.dtos.room.response.RoomResponseDto;
import backend.roomservice.service.RoomService;
import backend.utils.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
@SecurityRequirement(name = "BearerAuth")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService roomService) {
        this.service = roomService;
    }

    @Operation(summary = "Add a room", description = "Add a room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client saved"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "No authorized"),
            @ApiResponse(responseCode = "500", description = "Internal Error")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDto<RoomResponseDto>> addRoom(@RequestBody RoomRequestDto dto) {
        final RoomResponseDto data = service.add(dto);
        final ApiResponseDto<RoomResponseDto> response = new ApiResponseDto<>(HttpStatus.CREATED.value(), Message.ROOM_CREATED, data);
        final HttpHeaders headers = createHeader();
        return new ResponseEntity<>(response, headers, response.code());
    }

    private HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Trace-Id", MDC.get("traceId"));
        return headers;
    }
}
