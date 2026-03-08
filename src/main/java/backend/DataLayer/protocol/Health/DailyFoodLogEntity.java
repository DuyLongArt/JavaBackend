package backend.DataLayer.protocol.Health;

import backend.DataLayer.protocol.Person.PersonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "daily_food_logs", schema = "health")
public class DailyFoodLogEntity {

    @Id
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "calories_out")
    private Integer caloriesOut = 0;

    @Column(name = "weight_kg")
    private Double weightKg;

    public DailyFoodLogEntity(LocalDate date) {
        this.date = date;
    }
}
