package backend.DataLayer.protocol.Person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for PersonEntity data access.
 * Provides CRUD operations and custom queries for Person entities.
 */
@Repository
public interface PersonDAO extends JpaRepository<PersonEntity, Integer>
{

    @Query("SELECT p FROM PersonEntity p JOIN AccountEntity a ON p.id=a.identity.id WHERE a.username= :username")
    PersonEntity findPersonEntityByUserName(String username);


    @Query("SELECT a FROM ArchiveEntity a JOIN AccountEntity ac ON a.identity.id=ac.identity.id WHERE ac.username= :username")
    ArchiveEntity findArchiveByUserName(String username);



    @Query(value = "SELECT s.* FROM users.skills s JOIN users.persons p ON s.identity_id=p.identity_id " +
            "JOIN users.accounts a ON s.identity_id=a.identity_id " +
            "WHERE a.username= :username",
    nativeQuery = true)
    List<SkillEntity> findSkillEntityByUsername(String username);

    @Query(value = "INSERT into SkillEntity (name,description,identity_id) VALUES (name,description,identity_id) ",nativeQuery = true)
    void addSkillEntityByUsername(String category,String name,String description, Integer identity_id);
}

