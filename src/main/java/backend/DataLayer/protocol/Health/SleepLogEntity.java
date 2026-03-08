package backend.DataLayer.protocol.Health;

import backend.DataLayer.protocol.Person.PersonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sleep_logs", schema = "health")
public class SleepLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sleep_log_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "identity_id", nullable = false)
    private PersonEntity identity;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "quality_score")
    private Integer qualityScore;
}
