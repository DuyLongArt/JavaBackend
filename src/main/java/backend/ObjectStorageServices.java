package backend;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ObjectStorageServices
{


    @Autowired
    private MinioClient minioClient;

    public void uploadFile(MultipartFile file) throws Exception
    {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("duylongwebappobjectdatabase")
                        .object(file.getOriginalFilename())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
    }
}