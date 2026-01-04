package backend.DataLayer.protocol.Mail;

import backend.DataLayer.protocol.Person.PersonEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

//import org.hibernate.annotations.EventType;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "emails", schema = "users")
public class EmailEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_address_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "identity_id", nullable = false)
    private PersonEntity identity;

    @Size(max = 320)
    @NotNull
    @Column(name = "email_address", nullable = false, length = 320)
    private String emailAddress;

//    @NotNull
    @ColumnDefault("'personal'")
    @Column(name = "email_type", nullable = false,insertable = false, length = Integer.MAX_VALUE)
    private String emailType;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column(name = "verified_at")
    private Instant verifiedAt;

//    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", insertable = false, nullable = false)
    private Instant createdAt;
//    @NotNull



    @ColumnDefault("CURRENT_TIMESTAMP")

    @Column(name = "updated_at",insertable = false, nullable = false)
    private Instant updatedAt;

/*
 TODO [Reverse Engineering] create field to map the 'status' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'ACTIVE'")
    @Column(name = "status", columnDefinition = "email_status not null")
    private Object status;
*/



    public EmailEntity() {

    }
    public EmailEntity(String emailAddress) {
        this.emailAddress = emailAddress;

    }
}