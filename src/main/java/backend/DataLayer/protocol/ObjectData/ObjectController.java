package backend.DataLayer.protocol.ObjectData;

import backend.ObjectStorageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*; // Use .* to include RestController
import org.springframework.web.multipart.MultipartFile;

@RestController // <--- FIX 1: MUST HAVE THIS
@RequestMapping("/backend/object")
public class ObjectController {

    @Autowired // <--- FIX 2: MUST HAVE THIS to inject your service
    private ObjectStorageServices objectStorageServices;

    @PostMapping("/add")
    // FIX 3: Use @RequestParam("file") to match the key from your React/Postman request
    public String addObject(@RequestParam("file") MultipartFile file) {
        try {
            objectStorageServices.uploadFile(file);
            return "Object added successfully!";
        } catch (Exception ex) {
            // Log the error so you can see it in your Java console
            ex.printStackTrace();
            return "Error uploading file: " + ex.getMessage();
        }
    }
}