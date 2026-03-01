package backend;

import backend.DataLayer.protocol.ObjectData.AssetDAO;
import backend.DataLayer.protocol.ObjectData.AssetEntity;
import backend.DataLayer.protocol.Person.PersonDAO;
import backend.DataLayer.protocol.Person.PersonEntity;
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

    @Autowired
    private PersonDAO personDAO;

    public String uploadAvatar(String username, MultipartFile file) throws Exception {
        PersonEntity person = personDAO.findPersonEntityByUserName(username);
        String alias = (person != null && person.getAlias() != null && !person.getAlias().isBlank()) 
            ? person.getAlias() : username;
            
        String fileName = alias + "/avatars/" + file.getOriginalFilename();
        String path = upload(fileName, file);
        
        if (person != null) {
            person.setProfileImageUrl(path);
            personDAO.save(person);
        }
        return path;
    }

    public String uploadCover(String username, MultipartFile file) throws Exception {
        PersonEntity person = personDAO.findPersonEntityByUserName(username);
        String alias = (person != null && person.getAlias() != null && !person.getAlias().isBlank()) 
            ? person.getAlias() : username;
            
        String fileName = alias + "/covers/" + file.getOriginalFilename();
        String path = upload(fileName, file);

        if (person != null) {
            person.setCoverImageUrl(path);
            personDAO.save(person);
        }
        return path;
    }

    private String upload(String fileName, MultipartFile file) throws Exception {
        // Check if bucket exists, create if it does not
        boolean isBucketExists = minioClient.bucketExists(
                io.minio.BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isBucketExists) {
            minioClient.makeBucket(
                    io.minio.MakeBucketArgs.builder().bucket(bucketName).build());
        }

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
