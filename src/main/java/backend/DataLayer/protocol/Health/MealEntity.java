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
@Table(name = "meals", schema = "health")
public class MealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Integer id;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "meal_image_url")
    private String mealImageUrl;

    @Column(name = "identity_id")
    private Long identityId; // Matching DB int8
}
