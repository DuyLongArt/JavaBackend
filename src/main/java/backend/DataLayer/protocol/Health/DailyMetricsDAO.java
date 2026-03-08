package backend.DataLayer.protocol.Health;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyMetricsDAO extends JpaRepository<DailyMetricsEntity, DailyMetricsId> {
    Optional<DailyMetricsEntity> findByIdentityIdAndDate(Integer identityId, LocalDate date);
}
