package backend.DataLayer.protocol.Information;

import backend.DataLayer.protocol.Person.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for InformationEntity data access.
 * Provides CRUD operations and custom queries for Information entities.
 */
@Repository
public interface InformationDAO extends JpaRepository<InformationEntity, Integer> {

    @Query("SELECT i FROM InformationEntity i JOIN AccountEntity a ON i.identity.id=a.identity.id  WHERE a.username = :username")
    InformationEntity findInformationByUserName(String username);


    @Query("UPDATE InformationEntity i SET i.university = :university, i.location = :location WHERE i.identity.id = (SELECT a.identity.id FROM AccountEntity a WHERE a.username = :username)")
    void updateInformation(@Param("university") String university, @Param("location") String location, @Param("username") String username);
}
