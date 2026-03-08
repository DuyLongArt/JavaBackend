package backend;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MinIOService {

    private final MinioClient mediaClient;
    private final MinioClient userClient;

    @Value("${minio.media.bucket}")
    private String mediaBucket;

    @Value("${minio.user.bucket}")
    private String userBucket;

    public MinIOService(@Qualifier("minioMediaClient") MinioClient mediaClient,
                        @Qualifier("minioUserClient") MinioClient userClient) {
        this.mediaClient = mediaClient;
        this.userClient = userClient;
    }

    /**
     * Ensures that the shared buckets exist.
     */
    public void initializeBuckets() throws Exception {
        ensureBucketExists(mediaClient, mediaBucket);
        ensureBucketExists(userClient, userBucket);
    }

    /**
     * Initializes a user's storage area.
     * Based on knowledge base: create a bucket or folder for user media.
     * Currently, we use a shared bucket with user-specific prefixes (aliases).
     */
    public void initializeUserStorage(String alias) throws Exception {
        // Ensuring the buckets exist is the first step
        initializeBuckets();
        
        // In the current implementation, we don't create per-user buckets,
        // but we could put a placeholder file if we wanted to "create" the folder.
        // MinIO (and S3) doesn't have real folders, just prefixes.
        System.out.println("Initialized storage area for user: " + alias);
    }

    private void ensureBucketExists(MinioClient client, String bucket) throws Exception {
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            System.out.println("Created bucket: " + bucket);
        }
    }
}
