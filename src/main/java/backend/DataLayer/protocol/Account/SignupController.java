package backend.DataLayer.protocol.Account;

import backend.DataLayer.protocol.Information.InformationDAO;
import backend.DataLayer.protocol.Information.InformationEntity;
import backend.DataLayer.protocol.Mail.EmailDAO;
import backend.DataLayer.protocol.Mail.EmailEntity;
import backend.DataLayer.protocol.Person.PersonDAO;
import backend.DataLayer.protocol.Person.PersonEntity;
import backend.DataLayer.protocol.Credential.RegistrationCredentials;
import org.springframework.beans.factory.annotation.Autowired;
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

        private final PasswordEncoder passwordEncoder;

        @Autowired
        private AccountDAO accountDAO;

        @Autowired
        private InformationDAO informationDAO;

        @Autowired
        private EmailDAO emailDAO;

        @Autowired
        private PersonDAO personDAO;

        public SignupController(PasswordEncoder passwordEncoder) {
                this.passwordEncoder = passwordEncoder;
        }

        @PostMapping("signup")
        @Transactional // CRITICAL: Ensures all saves happen or none happen if there's an error
        public ResponseEntity<String> signup(@RequestBody RegistrationCredentials credential) {

                // 1. Hashing
                String rawPassword = credential.getPassword();
                String hashedPassword = passwordEncoder.encode(rawPassword);

                // 2. SAVE PERSON FIRST (The Parent)
                // We must save Person first to generate the 'identity_id'
                PersonEntity person = new PersonEntity();
                person.setFirstName(credential.getFirstName());
                person.setLastName(credential.getLastName());

                PersonEntity savedPerson = personDAO.save(person); // Returns entity with ID

                // 3. GET THE GENERATED ID
                Integer generatedId = savedPerson.getId();

                // 4. CREATE EMAIL (Linked to Person)
                EmailEntity email = new EmailEntity();
                email.setEmailAddress(credential.getEmail());
                email.setIsPrimary(true); // Mark as primary
                // email.setIdentity(generatedId);
                email.setIdentity(savedPerson);
                // email.setIdentity(savedPerson);
                // email.setId(generatedId); // Link using the generated ID
                // Note: Assuming EmailEntity has a setter for identityId or PersonEntity
                EmailEntity savedEmail = emailDAO.save(email);

                // 5. CREATE ACCOUNT (Sharing the SAME ID)
                AccountEntity account = new AccountEntity();
                // account.setId(generatedId); // MANUALLY SET ID TO MATCH PERSON
                // account.setId(savedPerson.getId());
                account.setUsername(credential.getUserName());
                account.setPasswordHash(hashedPassword);
                account.setRole(credential.getRole());
                account.setPrimaryEmailId(savedEmail.getId()); // Link to the primary email

                account.setDeviceIP(credential.getDeviceIP());
                account.setIdentity(savedPerson);

                accountDAO.save(account);

                // 6. CREATE INFORMATION (Sharing the SAME ID)
                InformationEntity info = new InformationEntity();
                // info.setId(generatedId); // MANUALLY SET ID TO MATCH PERSON
                info.setBio(credential.getBio());
                // info.setDeviceIP(credential.getDeviceIP());
                String location = credential.getLocation();
                if (location == null || location.isEmpty()) {
                        location = "HaNoi";
                }
                info.setLocation(location);

                info.setIdentity(savedPerson);

                informationDAO.save(info);

                return ResponseEntity.ok("User registered successfully with ID: " + generatedId);
        }
}