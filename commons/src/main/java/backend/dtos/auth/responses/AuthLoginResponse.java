package backend.dtos.auth.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "jwt", "status"})
public record AuthLoginResponse(String username,
                                String message,
                                String jwt,
                                boolean status) {
}
