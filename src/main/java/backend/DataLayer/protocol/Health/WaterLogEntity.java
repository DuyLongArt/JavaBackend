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
@Table(name = "water_logs", schema = "health")
public class WaterLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "water_log_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "identity_id", nullable = false)
    private PersonEntity identity;

    @Column(name = "amount_ml", nullable = false)
    private Integer amountMl;

    @Column(name = "logged_at", nullable = false)
    private Instant loggedAt = Instant.now();

    public WaterLogEntity(PersonEntity identity, Integer amountMl) {
        this.identity = identity;
        this.amountMl = amountMl;
    }
}
