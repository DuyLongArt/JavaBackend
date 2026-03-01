package backend;

import backend.DataLayer.protocol.ObjectData.AssetDAO;
import backend.DataLayer.protocol.ObjectData.AssetEntity;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ObjectStorageServices
{


    @Autowired
    @Qualifier("minioMediaClient")
    private MinioClient minioClient;

    @Value("${minio.media.bucket}")
    private String bucketName;

    @Autowired
    private AssetDAO assetDAO;

    public void uploadFile(MultipartFile file) throws Exception
    {
        // Check if bucket exists, create if it does not
        boolean isBucketExists = minioClient.bucketExists(
                io.minio.BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isBucketExists) {
            minioClient.makeBucket(
                    io.minio.MakeBucketArgs.builder().bucket(bucketName).build());
        }

        String fileName = file.getOriginalFilename();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        // Mirror metadata to local Postgres
        AssetEntity asset = new AssetEntity(
            bucketName,
            fileName, // path is just filename for now
            fileName,
            file.getContentType(),
            file.getSize()
        );
        assetDAO.save(asset);
    }
}