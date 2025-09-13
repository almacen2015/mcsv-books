package backend.clientservice.repositories;

import backend.clientservice.models.entities.Client;
import backend.enums.DocumentType;
import backend.enums.Gender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ClientRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ClientRepository repository;

    @AfterEach
    void resetAutoIncrement() {
        jdbcTemplate.execute("ALTER TABLE clients ALTER COLUMN id RESTART WITH 1");
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testFindByDocumentNumber() {
        Client client = buildClient("Victor", "Orbegozo", "12345678");
        Client clientSaved = repository.save(client);

        Optional<Client> clientFound = repository.findByDocumentNumber("12345678");

        assertTrue(clientFound.isPresent());
        assertEquals(clientSaved, clientFound.get());
    }

    @Test
    void testFindById() {
        Client client = buildClient("Victor", "Orbegozo", "12345678");
        Client clientSaved = repository.save(client);

        Optional<Client> clientFound = repository.findById(clientSaved.getId());

        assertTrue(clientFound.isPresent());
        assertEquals(clientSaved, clientFound.get());
    }

    @Test
    void testUpdate() {
        Client client = buildClient("Victor", "Orbegozo", "12345678");
        Client clientSaved = repository.save(client);

        clientSaved.setName("Maria");
        clientSaved.setLastName("Rosas");
        repository.save(clientSaved);

        Optional<Client> updatedClient = repository.findById(clientSaved.getId());

        assertTrue(updatedClient.isPresent());
        assertEquals("Maria", updatedClient.get().getName());
        assertEquals("Rosas", updatedClient.get().getLastName());
    }

    @Test
    void testList() {
        Client client1 = buildClient("Victor", "Orbegozo", "12345678");
        Client client2 = buildClient("Luis", "Casas", "11111111");
        Client client3 = buildClient("Mario", "Zelada", "22222222");

        List<Client> clients = repository.saveAll(List.of(client1, client2, client3));

        assertNotNull(clients);
        assertEquals(3, clients.size());
        assertEquals("Victor", clients.get(0).getName());
        assertEquals("Luis", clients.get(1).getName());
        assertEquals("Mario", clients.get(2).getName());
    }

    @Test
    void testAdd() {
        Client client = buildClient("Victor", "Orbegozo", "12345678");
        Client clientSaved = repository.save(client);

        assertNotNull(clientSaved);
        assertEquals(1L, clientSaved.getId());
        assertEquals("Victor", clientSaved.getName());
    }

    private Client buildClient(String name, String lastName, String documentNumber) {
        return Client.builder()
                .id(null)
                .name(name)
                .lastName(lastName)
                .birthDate(LocalDate.of(1994, 4, 5))
                .age(30)
                .gender(Gender.MALE.getCode())
                .documentNumber(documentNumber)
                .documentType(DocumentType.DNI.name())
                .build();
    }
}