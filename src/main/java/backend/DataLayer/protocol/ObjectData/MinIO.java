//package backend.DataLayer.protocol.ObjectData;
//import io.minio.MinioClient;
//
//public class MinIO {
//    public static void config(String[] args) {
//        MinioClient minioClient = MinioClient.builder()
//                .endpoint("http://192.168.22.4:9000") // Your MinIO API port
//                .credentials("duylongadmin", "duylongadminpass")
//                .build();
//
//        System.out.println("MinIO Client initialized successfully.");
//    }
//}