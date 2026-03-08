package backend.DataLayer.protocol.System;

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
@Table(name = "quests", schema = "public")
public class QuestEntity {

    @Id
    @Column(name = "id")
    private java.util.UUID id = backend.UUIDv7Generator.generateV7();

    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity person;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "target_value", nullable = false)
    private Double targetValue = 0.0;

    @Column(name = "current_value", nullable = false)
    private Double currentValue = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private QuestCategory category;

    @Column(name = "reward_exp", nullable = false)
    private Integer rewardExp = 0;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
}
