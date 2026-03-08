package backend.DataLayer.protocol.Health;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface DailyFoodLogDAO extends JpaRepository<DailyFoodLogEntity, LocalDate> {
}
