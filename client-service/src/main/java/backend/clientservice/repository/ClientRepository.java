package backend.clientservice.repository;

import backend.clientservice.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByDocumentNumber(String documentNumber);
}
