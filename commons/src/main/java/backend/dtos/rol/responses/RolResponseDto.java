package backend.dtos.rol.responses;

import backend.dtos.permission.responses.PermissionResponseDto;

import java.util.Set;

public record RolResponseDto(Long id, String name, Set<PermissionResponseDto> permissions) {
}
