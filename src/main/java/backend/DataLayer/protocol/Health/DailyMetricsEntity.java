package backend.DataLayer.protocol.Health;

import backend.DataLayer.protocol.Person.PersonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "daily_metrics", schema = "health")
@IdClass(DailyMetricsId.class)
public class DailyMetricsEntity {

    @Id
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Id
    @Column(name = "identity_id")
    private Integer identityId;

    @ManyToOne
    @MapsId("identityId")
    @JoinColumn(name = "identity_id")
    private PersonEntity identity;

    @Column(name = "steps", nullable = false)
    private Integer steps = 0;

    @Column(name = "heart_rate_avg")
    private Integer heartRateAvg;

    @Column(name = "sleep_hours")
    private Float sleepHours = 0.0f;

    @Column(name = "water_ml", nullable = false)
    private Integer waterMl = 0;

    @Column(name = "exercise_minutes", nullable = false)
    private Integer exerciseMinutes = 0;

    @Column(name = "focus_minutes", nullable = false)
    private Integer focusMinutes = 0;

    @Column(name = "calories_consumed", nullable = false)
    private Integer caloriesConsumed = 0;

    @Column(name = "calories_burned", nullable = false)
    private Integer caloriesBurned = 0;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    public DailyMetricsEntity(PersonEntity person, LocalDate date) {
        this.identity = person;
        this.identityId = person.getId();
        this.date = date;
    }
}
