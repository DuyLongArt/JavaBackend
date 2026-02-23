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
public class UserProfileStorageService {

    @Autowired
    @Qualifier("minioUserClient")
    private MinioClient minioClient;

    @Value("${minio.user.bucket}")
    private String bucketName;

    @Autowired
    private AssetDAO assetDAO;

    public String uploadAvatar(Integer userId, MultipartFile file) throws Exception {
        String fileName = "avatars/" + userId + "_" + file.getOriginalFilename();
        return upload(fileName, file);
    }

    public String uploadCover(Integer userId, MultipartFile file) throws Exception {
        String fileName = "covers/" + userId + "_" + file.getOriginalFilename();
        return upload(fileName, file);
    }

    private String upload(String fileName, MultipartFile file) throws Exception {
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
            fileName,
            file.getOriginalFilename(),
            file.getContentType(),
            file.getSize()
        );
        assetDAO.save(asset);

        return fileName;
    }
}
