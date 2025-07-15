package backend.utils;

import backend.dtos.client.requests.ClientRequestDto;

public class Utils {

    public static void validateClientDto(ClientRequestDto dto) {
        if (dto.name().isEmpty() || dto.name().isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
    }
}
