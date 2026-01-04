package backend.DataLayer.protocol.Mail;

import backend.DataLayer.protocol.Person.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for EmailEntity data access.
 * Provides CRUD operations and custom queries for Email entities.
 */
@Repository
public interface EmailDAO extends JpaRepository<EmailEntity, Integer> {

}
