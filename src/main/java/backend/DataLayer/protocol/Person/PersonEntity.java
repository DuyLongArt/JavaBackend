package backend.DataLayer.protocol.Person;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "persons", schema = "users")
@EntityListeners(AuditingEntityListener.class)
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identity_id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", length = Integer.MAX_VALUE)
    private String lastName;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @Column(name = "gender", length = Integer.MAX_VALUE)
    private String gender;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "profile_image_url", length = Integer.MAX_VALUE)
    private String profileImageUrl;

    @Column(name = "cover_image_url", length = Integer.MAX_VALUE)
    private String coverImageUrl;

    @ColumnDefault("true")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    // @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @CreatedDate
    @Column(name = "created_at", insertable = false, updatable = false)
    // @org.hibernate.annotations.Generated(event =
    // org.hibernate.annotations.EventType.INSERT)
    private Instant createdAt;

    // @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @LastModifiedBy
    // @org.hibernate.annotations.Generated(event =
    // org.hibernate.annotations.EventType.INSERT)
    @Column(name = "updated_at", insertable = false, nullable = false)
    private Instant updatedAt;

    public PersonEntity() {
    }

    public PersonEntity(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Column(name = "alias", unique = true, length = 255)
    private String alias;
}