package backend.DataLayer.protocol.Support;

import backend.DataLayer.protocol.Person.PersonDAO;
import backend.DataLayer.protocol.Person.PersonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/support")
public class SupportController {

    @Autowired
    private SupportDAO supportDAO;

    @Autowired
    private PersonDAO personDAO;

    @PostMapping("/feedback")
    public ResponseEntity<?> submitFeedback(
            @RequestBody SupportEntity feedback,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            String alias = userDetails.getUsername();
            if (alias != null) {
                PersonEntity person = personDAO.findByAlias(alias);
                if (person != null) {
                    feedback.setIdentity(person);
                }
            }
        }

        SupportEntity saved = supportDAO.save(feedback);
        return ResponseEntity.ok(saved);
    }
}
