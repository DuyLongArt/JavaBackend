package backend.DataLayer.protocol.Health;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "meal_logs", schema = "health")
public class MealLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_log_id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "fat")
    private Float fat;

    @Column(name = "carb")
    private Float carb;

    @Column(name = "protein")
    private Float protein;

    @Column(name = "calories")
    private Integer calories;

    @Column(name = "eaten_at")
    private LocalDateTime eatenAt = LocalDateTime.now();

    @Column(name = "time_in_day", insertable = false, updatable = false)
    private String timeInDay; // Generated column in DB

    @Column(name = "daily_food_log_id")
    private LocalDate dailyFoodLogId;
}
