package backend.dtos.client.requests;

import java.time.LocalDate;

public record ClientRequestDto(String name,
                               String lastName,
                               LocalDate birthDate,
                               String gender) {
}