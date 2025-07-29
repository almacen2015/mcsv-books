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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void testAdd() {
        Client client = new Client(null, "Victor", "Orbegozo", LocalDate.of(1994, 4, 5), 30, 'M');
        Client clientSaved = repository.save(client);

        assertNotNull(clientSaved);
        assertEquals(1L, clientSaved.getId());
        assertEquals("Victor", clientSaved.getName());
    }
}