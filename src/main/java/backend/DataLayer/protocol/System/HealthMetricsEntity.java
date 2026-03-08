package backend.DataLayer.protocol.System;

import backend.DataLayer.protocol.Person.PersonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "health_metrics", schema = "public")
public class HealthMetricsEntity {

    @Id
    @Column(name = "id")
    private java.util.UUID id = backend.UUIDv7Generator.generateV7();

    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity person;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "steps", nullable = false)
    private Integer steps = 0;

    @Column(name = "exercise_minutes", nullable = false)
    private Integer exerciseMinutes = 0;

    @Column(name = "focus_minutes", nullable = false)
    private Integer focusMinutes = 0;

    @Column(name = "calories_consumed", nullable = false)
    private Integer caloriesConsumed = 0;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    public HealthMetricsEntity(PersonEntity person, LocalDate date) {
        this.person = person;
        this.date = date;
    }
}
