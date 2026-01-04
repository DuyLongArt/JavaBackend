package backend;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfig {

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://192.168.22.4:9000")
                .credentials("duylongadmin", "duylongadminpass")
                .build();
    }
}