package backend.utils;

import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.pageable.PageableCustom;
import backend.enums.Gender;
import backend.exceptions.UtilException;
import backend.exceptions.client.ClientException;
import backend.exceptions.page.PageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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

        if (isStringValid(dto.gender()) || dto.gender().length() != 1
                || !isValidGender(dto.gender())) {
            throw new ClientException(ClientException.ERROR_GENDER);
        }

        if (dto.birthDate() == null || isAdult(dto.birthDate())) {
            throw new ClientException(ClientException.ERROR_BIRTHDATE);
        }
    }

    public static void validatePagination(PageableCustom paginado) {
        if (isNotPositive(paginado.page())) {
            throw new PageException(PageException.PAGE_NUMBER_INVALID);
        }

        if (isNotPositive(paginado.size())) {
            throw new PageException(PageException.SIZE_NUMBER_INVALID);
        }

        if (isStringValid(paginado.orderBy())) {
            throw new PageException(PageException.SORT_NAME_INVALID);
        }
    }

    public static void isValidId(Long id) {
        if (isNotPositive(id.intValue())) {
            throw new UtilException(UtilException.ID_NOT_VALID);
        }
    }

    public static boolean isNotPositive(Integer value) {
        return value == null || value <= 0;
    }

    public static PageRequest constructPageable(PageableCustom paginado) {
        return PageRequest.of(paginado.page() - 1, paginado.size(), Sort.by(paginado.orderBy()).descending());
    }

    public static boolean isValidGender(String gender) {
        return gender.equals(Gender.M.name()) || gender.equals(Gender.F.name());
    }

    public static int getAge(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);
        return period.getYears();
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
