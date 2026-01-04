package backend.DataLayer.protocol.Person;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "skills", schema = "users")
public class SkillEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_id")
    private PersonEntity identity;

    @Size(max = 255)
    @Column(name = "category")
    private String category;

    @NotNull
    @Column(name = "name", nullable = false, length = 255)
    private String name;

/*
 TODO [Reverse Engineering] create field to map the 'name' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "name", columnDefinition = "bit varying(255) not null")
    private Object name;
*/
}