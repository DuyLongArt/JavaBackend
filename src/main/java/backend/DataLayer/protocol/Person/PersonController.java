package backend.DataLayer.protocol.Person;

import backend.UserProfileStorageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/backend/person")

public class PersonController {
    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private UserProfileStorageService userProfileStorageService;

    // @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @Transactional
    @GetMapping("information")
    public ResponseEntity<PersonEntity> getInformation(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return ResponseEntity.status(401).build();
        String currentUsername = userDetails.getUsername();
        PersonEntity person = personDAO.findPersonEntityByUserName(currentUsername);
        return ResponseEntity.ok(person);
    }

    @GetMapping("archive")
    public ResponseEntity<ArchiveEntity> getArchiveInformation(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return ResponseEntity.status(401).build();
        String currentUsername = userDetails.getUsername();
        ArchiveEntity personArchive = personDAO.findArchiveByUserName(currentUsername);
        if (personArchive == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(personArchive);
    }

    @PostMapping("archive/edit")
    public ResponseEntity<ArchiveEntity> editArchive(
            @AuthenticationPrincipal UserDetails userDetails,
            String address,
            String phoneNumber,
            String bio) {
        if (userDetails == null) return ResponseEntity.status(401).build();
        return null;
    }

    @GetMapping("skills")
    public ResponseEntity<List<SkillEntity>> getSkillInformation(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return ResponseEntity.status(401).build();
        String currentUsername = userDetails.getUsername();
        List<SkillEntity> skills = personDAO.findSkillEntityByUsername(currentUsername);
        if (skills.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(skills);
    }

    @PostMapping("skill/add")
    public ResponseEntity<SkillEntity> editSkill(
            @AuthenticationPrincipal UserDetails userDetails,
            String category,
            String name,
            String description
    ) {
        if (userDetails == null) return ResponseEntity.status(401).build();
        String currentUsername = userDetails.getUsername();
        PersonEntity person = personDAO.findPersonEntityByUserName(currentUsername);
        if (person == null) return ResponseEntity.status(404).build();
        
        try {
            personDAO.addSkillEntityByUsername(category, name, description, person.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("avatar/update")
    public ResponseEntity<String> updateAvatar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file) {
        if (userDetails == null) return ResponseEntity.status(401).body("Unauthorized: No user found in context");
        try {
            String path = userProfileStorageService.uploadAvatar(userDetails.getUsername(), file);
            return ResponseEntity.ok(path);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error uploading avatar: " + e.getMessage());
        }
    }

    @PostMapping("cover/update")
    public ResponseEntity<String> updateCover(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file) {
        if (userDetails == null) return ResponseEntity.status(401).body("Unauthorized: No user found in context");
        try {
            String path = userProfileStorageService.uploadCover(userDetails.getUsername(), file);
            return ResponseEntity.ok(path);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error uploading cover: " + e.getMessage());
        }
    }

}
