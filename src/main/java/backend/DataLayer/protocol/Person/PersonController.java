package backend.DataLayer.protocol.Person;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/backend/person/")

public class PersonController
{
    @Autowired
    private PersonDAO personDAO;
//    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @Transactional
    @GetMapping("information")
    public ResponseEntity<PersonEntity> getInformation(@AuthenticationPrincipal UserDetails userDetails)
    {

        String currentUsername = userDetails.getUsername();

        // Now find the person based on the username/email instead of ID 1
//        PersonEntity person = personDAO.(currentUsername).orElse(null);
        PersonEntity person=personDAO.findPersonEntityByUserName(currentUsername);
//        return ResponseEntity.ok(person);
        return ResponseEntity.ok(person);
    }

    @GetMapping("archive")
    public ResponseEntity<ArchiveEntity> getArchiveInformation(@AuthenticationPrincipal UserDetails userDetails)
    {
        String currentUsername = userDetails.getUsername();

        // Now find the person based on the username/email instead of ID 1
//        PersonEntity person = personDAO.(currentUsername).orElse(null);
        ArchiveEntity personArchive= personDAO.findArchiveByUserName(currentUsername);
//        return ResponseEntity.ok(person);
        if(personArchive==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(personArchive);
    }
    @PostMapping("archive/edit")
    public ResponseEntity<ArchiveEntity> editArchive(
            @AuthenticationPrincipal UserDetails userDetails,
            String address,
            String phoneNumber,
            String bio
    )
    {

return null;
    }


    @GetMapping("skills")
    public ResponseEntity<List<SkillEntity>> getSkillInformation(@AuthenticationPrincipal UserDetails userDetails) {
        String currentUsername = userDetails.getUsername();

        List<SkillEntity> skills = personDAO.findSkillEntityByUsername(currentUsername);

        // If the list is empty, you can return 404 or an empty 200 list.
        // Usually, an empty list with 200 OK is preferred for collections.
        if (skills.isEmpty()) {
            return ResponseEntity.noContent().build(); // or .notFound().build()
        }

        return ResponseEntity.ok(skills);
    }
    @PostMapping("skill/add")
    public ResponseEntity<SkillEntity> editSkill(
            @AuthenticationPrincipal UserDetails userDetails,
            String category,
            String name,
            String description

    )
    {
        String currentUsername = userDetails.getUsername();
        Integer currentIdentityId= personDAO.findPersonEntityByUserName(currentUsername).getId();
        try{
            personDAO.addSkillEntityByUsername(category,name,description,currentIdentityId);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

    }

}
