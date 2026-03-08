package backend.DataLayer.protocol.System;

import backend.DataLayer.protocol.Person.PersonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "scores", schema = "public")
public class ScoreEntity {

    @Id
    @Column(name = "identity_id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "identity_id")
    private PersonEntity identity;

    @Column(name = "health_global_score", nullable = false)
    private Double healthGlobalScore = 0.0;

    @Column(name = "social_global_score", nullable = false)
    private Double socialGlobalScore = 0.0;

    @Column(name = "financial_global_score", nullable = false)
    private Double financialGlobalScore = 0.0;

    @Column(name = "career_global_score", nullable = false)
    private Double careerGlobalScore = 0.0;

    public ScoreEntity(PersonEntity identity) {
        this.identity = identity;
        this.id = identity.getId();
    }
}
