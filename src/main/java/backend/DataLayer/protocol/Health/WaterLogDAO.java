package backend.DataLayer.protocol.Health;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterLogDAO extends JpaRepository<WaterLogEntity, Integer> {
}
