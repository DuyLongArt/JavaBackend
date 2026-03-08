package backend.DataLayer.protocol.System;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreDAO extends JpaRepository<ScoreEntity, Integer> {
}
