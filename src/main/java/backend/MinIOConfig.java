package backend;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfig {

    @Value("${minio.media.endpoint}")
    private String mediaEndpoint;
    @Value("${minio.media.access-key}")
    private String mediaAccessKey;
    @Value("${minio.media.secret-key}")
    private String mediaSecretKey;

    @Value("${minio.user.endpoint}")
    private String userEndpoint;
    @Value("${minio.user.access-key}")
    private String userAccessKey;
    @Value("${minio.user.secret-key}")
    private String userSecretKey;

    @Bean(name = "minioMediaClient")
    public MinioClient minioMediaClient() {
        return MinioClient.builder()
                .endpoint(mediaEndpoint)
                .credentials(mediaAccessKey, mediaSecretKey)
                .build();
    }

    @Bean(name = "minioUserClient")
    public MinioClient minioUserClient() {
        return MinioClient.builder()
                .endpoint(userEndpoint)
                .credentials(userAccessKey, userSecretKey)
                .build();
    }
}