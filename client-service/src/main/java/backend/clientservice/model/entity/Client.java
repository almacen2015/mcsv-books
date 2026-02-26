package backend.clientservice.model.entity;

import backend.enums.Gender;
import backend.exceptions.client.ClientException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "clients")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private char gender;

    @Column(nullable = false, unique = true)
    private String documentNumber;

    @Column(nullable = false)
    private String documentType;

    public void update(
            String name,
            String lastName,
            LocalDate birthDate,
            String gender,
            String documentNumber,
            String documentType
    ) {

        if (birthDate.isAfter(LocalDate.now())) {
            throw new ClientException("Birthdate cannot be in the future");
        }

        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender.charAt(0) == Gender.MALE.getCode() ? Gender.MALE.getCode() : Gender.FEMALE.getCode();
        this.documentNumber = documentNumber;
        this.documentType = documentType;
    }
}
