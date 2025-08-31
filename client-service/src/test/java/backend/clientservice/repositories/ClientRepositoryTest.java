package backend.clientservice.repositories;

import backend.clientservice.models.entities.Client;
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
    void testUpdate() {
        Client client = new Client(null, "Victor", "Orbegozo", LocalDate.of(1994, 4, 5), 30, 'M');
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
        Client client1 = new Client(null, "Victor", "Orbegozo", LocalDate.of(1994, 4, 5), 30, 'M');
        Client client2 = new Client(null, "Luis", "Mesa", LocalDate.of(1994, 4, 5), 30, 'M');
        Client client3 = new Client(null, "Mario", "Mario", LocalDate.of(1994, 4, 5), 30, 'M');

        List<Client> clients = repository.saveAll(List.of(client1, client2, client3));

        assertNotNull(clients);
        assertEquals(3, clients.size());
        assertEquals(1L, clients.get(0).getId());
        assertEquals(2L, clients.get(1).getId());
        assertEquals(3L, clients.get(2).getId());
        assertEquals("Victor", clients.get(0).getName());
        assertEquals("Luis", clients.get(1).getName());
        assertEquals("Mario", clients.get(2).getName());
    }

    @Test
    void testAdd() {
        Client client = new Client(null, "Victor", "Orbegozo", LocalDate.of(1994, 4, 5), 30, 'M');
        Client clientSaved = repository.save(client);

        assertNotNull(clientSaved);
        assertEquals(1L, clientSaved.getId());
        assertEquals("Victor", clientSaved.getName());
    }
}