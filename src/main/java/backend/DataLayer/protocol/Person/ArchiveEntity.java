package backend.DataLayer.protocol.Person;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "archives", schema = "users")
public class ArchiveEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "archive_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "category")
    private String category;

    @NotNull
    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_id")
    private PersonEntity identity;

}