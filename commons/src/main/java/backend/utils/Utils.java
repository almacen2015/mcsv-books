package backend.utils;

import backend.dtos.client.requests.ClientRequestDto;
import backend.dtos.pageable.PageableCustom;
import backend.enums.DocumentType;
import backend.enums.Gender;
import backend.exceptions.UtilException;
import backend.exceptions.client.ClientException;
import backend.exceptions.page.PageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Objects;

public class Utils {

    public static void validateDocumentNumber(String documentNumber, String documentType) {
        validateDocumentType(documentType);

        if (Objects.equals(documentType, DocumentType.DNI.name())) {
            validateDni(documentNumber);
        }

        if (Objects.equals(documentType, DocumentType.CE.name())) {
            validateCe(documentNumber);
        }
    }

    public static void validateDocumentType(String documentType) {
        boolean exists = Arrays.stream(DocumentType.values())
                .anyMatch(dt -> Objects.equals(dt.name(), documentType));
        if (!exists) {
            throw new ClientException(ClientException.ERROR_DOCUMENT_TYPE);
        }
    }

    public static void validateDni(String documentNumber) {
        if (documentNumber.length() != 8) {
            throw new ClientException(ClientException.ERROR_DNI);
        }
    }

    public static void validateCe(String documentNumber) {
        if (documentNumber.length() != 9) {
            throw new ClientException(ClientException.ERROR_CE);
        }
    }

    public static void validateClientDto(ClientRequestDto dto) {
        if (isInvalidString(dto.name())) {
            throw new ClientException(ClientException.ERROR_NAME);
        }

        if (isInvalidString(dto.lastName())) {
            throw new ClientException(ClientException.ERROR_LASTNAME);
        }

        if (isInvalidString(dto.gender()) || dto.gender().length() != 1
                || !isValidGender(dto.gender())) {
            throw new ClientException(ClientException.ERROR_GENDER);
        }

        LocalDate date = toLocalDate(dto.birthDate());

        if (!isAdult(date)) {
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

        if (isInvalidString(paginado.orderBy())) {
            throw new PageException(PageException.SORT_NAME_INVALID);
        }
    }

    public static void isValidId(Long id) {
        if (id == null || isNotPositive(id.intValue())) {
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
        return gender.equals(String.valueOf(Gender.MALE.getCode())) || gender.equals(String.valueOf(Gender.FEMALE.getCode()));
    }

    public static int getAge(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);
        return period.getYears();
    }

    public static boolean isAdult(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        return Period.between(birthDate, now).getYears() > 18;
    }

    public static LocalDate toLocalDate(String date) {
        if (date == null || date.isBlank()) {
            throw new UtilException(UtilException.DATE_NOT_VALID);
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new UtilException(UtilException.DATE_NOT_VALID);
        }
    }

    public static boolean isInvalidString(String string) {
        return string == null || string.isBlank();
    }
}
