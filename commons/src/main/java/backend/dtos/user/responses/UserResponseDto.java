package backend.dtos.user.responses;

import backend.dtos.rol.responses.RolResponseDto;

import java.util.Set;

public record UserResponseDto(Long id,
                              String username,
                              boolean isEnabled,
                              boolean accountNoExpired,
                              boolean accountNoLocked,
                              boolean credentialsNoExpired,
                              Set<RolResponseDto> roles) {
}
