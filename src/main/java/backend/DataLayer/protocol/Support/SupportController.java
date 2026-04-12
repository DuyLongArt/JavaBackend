package backend.DataLayer.protocol.Support;

import backend.DataLayer.protocol.JWT.JWTService;
import backend.DataLayer.protocol.Person.PersonDAO;
import backend.DataLayer.protocol.Person.PersonEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/support")
public class SupportController {

    @Autowired
    private SupportDAO supportDAO;

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/feedback")
    public ResponseEntity<?> submitFeedback(
            @RequestBody SupportEntity feedback,
            HttpServletRequest request) {
        
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String alias = jwtService.extractAlias(token);
            if (alias != null) {
                Optional<PersonEntity> person = personDAO.findByAlias(alias);
                person.ifPresent(feedback::setIdentity);
            }
        }

        SupportEntity saved = supportDAO.save(feedback);
        return ResponseEntity.ok(saved);
    }
}
