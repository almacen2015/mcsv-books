package backend.dtos.client.requests;

import java.time.LocalDate;

public record ClientRequestDto(String name,
                               String lastname,
                               LocalDate birthDay,
                               char gender) {
}