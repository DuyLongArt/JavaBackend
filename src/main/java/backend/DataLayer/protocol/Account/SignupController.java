package backend.DataLayer.protocol.Account;

import backend.DataLayer.protocol.Information.InformationDAO;
import backend.DataLayer.protocol.Information.InformationEntity;
import backend.DataLayer.protocol.Mail.EmailDAO;
import backend.DataLayer.protocol.Mail.EmailEntity;
import backend.DataLayer.protocol.Person.PersonDAO;
import backend.DataLayer.protocol.Person.PersonEntity;
import backend.DataLayer.protocol.Credential.RegistrationCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend/auth/")
public class SignupController {

    private final UserRegistrationService registrationService;

    public SignupController(UserRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody RegistrationCredentials credential) {
        try {
            // Preliminary Checks
            if (credential.getUserName() == null || credential.getUserName().isBlank()) {
                return ResponseEntity.badRequest().body("Username is required");
            }
            if (credential.getPassword() == null || credential.getPassword().isBlank()) {
                return ResponseEntity.badRequest().body("Password is required");
            }
            if (credential.getEmail() == null || credential.getEmail().isBlank()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (credential.getFirstName() == null || credential.getFirstName().isBlank()) {
                return ResponseEntity.badRequest().body("First name is required");
            }

            Integer generatedId = registrationService.registerUser(credential);
            return ResponseEntity.ok("User registered successfully with ID: " + generatedId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signup failed: " + e.getMessage());
        }
    }
}
