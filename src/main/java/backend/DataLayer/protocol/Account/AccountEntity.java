package backend.DataLayer.protocol.Account;

import backend.DataLayer.protocol.Person.PersonEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "accounts", schema = "users", uniqueConstraints = {
        @UniqueConstraint(name = "accounts_username_key", columnNames = { "username" })
})
public class AccountEntity {
    @Id
    @Column(name = "identity_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "identity_id", nullable = false)
    private PersonEntity identity;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @NotNull
    @Column(name = "password_hash", nullable = false, length = Integer.MAX_VALUE)
    private String passwordHash;

    @Column(name = "primary_email_id")
    private Integer primaryEmailId;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked = false;

    @Column(name = "device_ip", length = Integer.MAX_VALUE)
    private String deviceIP;

    @ColumnDefault("0")
    @Column(name = "failed_login_attempts", insertable = false, nullable = false)
    private Integer failedLoginAttempts;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "password_changed_at", insertable = false, nullable = false)
    private Instant passwordChangedAt;

    // @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", insertable = false, nullable = false)
    private Instant createdAt;
    // @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", insertable = false, nullable = false)
    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class) // This tells Hibernate to use PG-specific casting
    @Column(name = "role", columnDefinition = "users.user_role")
    private UserRole role;

    public AccountEntity() {
    }

    public AccountEntity(String username, String passwordHash, UserRole role, Integer primaryEmailId) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.primaryEmailId = primaryEmailId;
        // this.primaryEmailId = primaryEmailId;
    }
}