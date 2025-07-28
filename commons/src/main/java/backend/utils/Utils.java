package backend.utils;

import backend.dtos.client.requests.ClientRequestDto;
import backend.exceptions.client.ClientException;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class Utils {

    public static void validateClientDto(ClientRequestDto dto) {
        if (isStringValid(dto.name())) {
            throw new ClientException(ClientException.ERROR_NAME);
        }

        if (isStringValid(dto.lastName())) {
            throw new ClientException(ClientException.ERROR_LASTNAME);
        }

        if (isStringValid(dto.gender()) || dto.gender().length() != 1) {
            throw new ClientException(ClientException.ERROR_GENDER);
        }

        if (isAdult(dto.birthDate())) {
            throw new ClientException(ClientException.ERROR_BIRTHDATE);
        }
    }

    public static boolean isAdult(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        return Period.between(now, birthDate).getYears() > 18;
    }

    public static boolean isStringValid(String string) {
        return string == null || string.isBlank();
    }

    public static String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
