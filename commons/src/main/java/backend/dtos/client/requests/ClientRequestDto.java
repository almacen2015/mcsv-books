package backend.dtos.client.requests;

public record ClientRequestDto(String name,
                               String lastName,
                               String birthDate,
                               String gender,
                               String documentNumber,
                               String documentType) {
}