package backend.dtos.client.responses;

import java.time.LocalDate;

public record ClientResponseDto(Long id,
                                String name,
                                String lastName,
                                LocalDate birthDate,
                                char gender,
                                String documentNumber,
                                String documentType) {
}
