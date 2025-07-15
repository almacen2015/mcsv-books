package backend.utils;

import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.exceptions.client.ClientException;

public class Utils {

    public static void validateClientDto(ClientRequestDto dto) {
        if (dto.name().isEmpty() || dto.name().isBlank()) {
            throw new ClientException(ClientException.ERROR_NAME);
        }
    }
}
