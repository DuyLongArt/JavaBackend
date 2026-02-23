package backend.DataLayer.protocol.ObjectData;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "assets", schema = "public")
@EntityListeners(AuditingEntityListener.class)
public class AssetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "bucket_name", nullable = false)
    private String bucketName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public AssetEntity() {}

    public AssetEntity(String bucketName, String filePath, String fileName, String contentType, Long fileSize) {
        this.bucketName = bucketName;
        this.filePath = filePath;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }
}
