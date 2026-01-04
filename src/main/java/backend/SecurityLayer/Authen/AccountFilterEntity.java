package backend.SecurityLayer.Authen;

import backend.DataLayer.protocol.Account.AccountEntity;
import backend.DataLayer.protocol.Url.UrlEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account_filter") // Clear name for the linking table
@Getter
@Setter
public class AccountFilterEntity {

    // FIX 1: Use a Composite Key for the junction table
    @EmbeddedId
    private AccountFilterId id;

    // FIX 2: Map the Account relationship
    @MapsId("accountId") // Links this field to the 'accountId' in the embedded ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity accountEntity;

    // FIX 3: Map the URL relationship (UrlEntity, not AccountEntity)
    @MapsId("urlId") // Links this field to the 'urlId' in the embedded ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id", nullable = false)
    private UrlEntity urlEntity; // Use the new UrlEntity


    // Additional metadata for the link (e.g., specific access level)
    @Column(name = "access_level")
    private String accessLevel;
}