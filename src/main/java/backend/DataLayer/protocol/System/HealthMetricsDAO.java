package backend.DataLayer.protocol.System;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HealthMetricsDAO extends JpaRepository<HealthMetricsEntity, java.util.UUID> {
    Optional<HealthMetricsEntity> findByPersonIdAndDate(Integer personId, LocalDate date);
}
