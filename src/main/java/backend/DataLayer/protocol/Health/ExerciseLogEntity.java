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
@Table(name = "exercise_logs", schema = "health")
public class ExerciseLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_log_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "identity_id", nullable = false)
    private PersonEntity identity;

    @Column(name = "exercise_type", nullable = false)
    private String exerciseType;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "intensity")
    private String intensity;

    @Column(name = "calories_burned")
    private Integer caloriesBurned;

    @Column(name = "logged_at", nullable = false)
    private Instant loggedAt = Instant.now();
}
