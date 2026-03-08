package backend.DataLayer.protocol.System;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface QuestDAO extends JpaRepository<QuestEntity, java.util.UUID> {
    List<QuestEntity> findByPersonId(Integer personId);
    List<QuestEntity> findByPersonIdAndIsCompleted(Integer personId, Boolean isCompleted);
}
