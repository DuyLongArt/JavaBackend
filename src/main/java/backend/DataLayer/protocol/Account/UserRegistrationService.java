package backend.DataLayer.protocol.Account;

import backend.DataLayer.protocol.Information.InformationDAO;
import backend.DataLayer.protocol.Information.InformationEntity;
import backend.DataLayer.protocol.Mail.EmailDAO;
import backend.DataLayer.protocol.Mail.EmailEntity;
import backend.DataLayer.protocol.Person.PersonDAO;
import backend.DataLayer.protocol.Person.PersonEntity;
import backend.DataLayer.protocol.Credential.RegistrationCredentials;
import backend.MinIOService;
import backend.DataLayer.protocol.System.SystemScoringService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationService {

    private final AccountDAO accountDAO;
    private final InformationDAO informationDAO;
    private final EmailDAO emailDAO;
    private final PersonDAO personDAO;
    private final PasswordEncoder passwordEncoder;
    private final MinIOService minIOService;
    private final SystemScoringService scoringService;

    public UserRegistrationService(AccountDAO accountDAO, InformationDAO informationDAO, 
                                   EmailDAO emailDAO, PersonDAO personDAO, 
                                   PasswordEncoder passwordEncoder,
                                   MinIOService minIOService,
                                   SystemScoringService scoringService) {
        this.accountDAO = accountDAO;
        this.informationDAO = informationDAO;
        this.emailDAO = emailDAO;
        this.personDAO = personDAO;
        this.passwordEncoder = passwordEncoder;
        this.minIOService = minIOService;
        this.scoringService = scoringService;
    }

    @Transactional
    public Integer registerUser(RegistrationCredentials credential) throws Exception {
        // 1. Preliminary Checks
        if (accountDAO.existsByUsername(credential.getUserName())) {
            throw new Exception("Username already exists");
        }
        if (emailDAO.existsByEmailAddress(credential.getEmail())) {
            // Check if we can just link the alias to the existing person
            java.util.Optional<EmailEntity> existingEmailOpt = emailDAO.findFirstByEmailAddress(credential.getEmail());
            if (existingEmailOpt.isPresent()) {
                PersonEntity existingPerson = existingEmailOpt.get().getIdentity();
                if (existingPerson != null) {
                    if (existingPerson.getAlias() == null || existingPerson.getAlias().isBlank()) {
                        System.out.println("Linking existing email " + credential.getEmail() + " to new alias " + credential.getAlias());
                        existingPerson.setAlias(credential.getAlias());
                        personDAO.saveAndFlush(existingPerson);
                        return existingPerson.getId();
                    } else if (existingPerson.getAlias().equals(credential.getAlias())) {
                        System.out.println("Email " + credential.getEmail() + " already linked to alias " + credential.getAlias());
                        return existingPerson.getId();
                    }
                }
            }
            throw new Exception("Email already exists and is linked to a different identity");
        }

        // 2. Hashing (if password provided, otherwise use a random uuid or something)
        String hashedPassword = "";
        if (credential.getPassword() != null && !credential.getPassword().isBlank()) {
            hashedPassword = passwordEncoder.encode(credential.getPassword());
        } else {
            // For OAuth users, we can use a placeholder or handle it differently
            hashedPassword = "OAUTH_USER_" + java.util.UUID.randomUUID().toString();
        }

        // 3. SAVE PERSON FIRST
        PersonEntity person = new PersonEntity();
        person.setFirstName(credential.getFirstName());
        person.setLastName(credential.getLastName() != null ? credential.getLastName() : "");
        person.setIsActive(true);
        if (credential.getAlias() != null && !credential.getAlias().isBlank()) {
            person.setAlias(credential.getAlias());
        }
        PersonEntity savedPerson = personDAO.saveAndFlush(person);

        Integer generatedId = savedPerson.getId();

        // 4. CREATE EMAIL
        EmailEntity email = new EmailEntity();
        email.setEmailAddress(credential.getEmail());
        email.setIsPrimary(true);
        email.setIdentity(savedPerson);
        EmailEntity savedEmail = emailDAO.saveAndFlush(email);

        // 5. CREATE ACCOUNT
        AccountEntity account = new AccountEntity();
        account.setUsername(credential.getUserName());
        account.setPasswordHash(hashedPassword);
        
        UserRole role = credential.getRole() != null ? credential.getRole() : UserRole.USER;
        account.setRole(role);
        account.setPrimaryEmailId(savedEmail.getId());
        account.setDeviceIP(credential.getDeviceIP() != null ? credential.getDeviceIP() : "");
        account.setIdentity(savedPerson);
        accountDAO.saveAndFlush(account);

        // 6. CREATE INFORMATION
        InformationEntity info = new InformationEntity();
        info.setBio(credential.getBio() != null ? credential.getBio() : "");
        String location = credential.getLocation();
        if (location == null || location.isEmpty()) {
            location = "HaNoi";
        }
        info.setLocation(location);
        info.setIdentity(savedPerson);
        informationDAO.saveAndFlush(info);

        // 7. INITIALIZE STORAGE
        try {
            minIOService.initializeUserStorage(credential.getAlias() != null ? credential.getAlias() : credential.getUserName());
        } catch (Exception e) {
            System.err.println("Failed to initialize MinIO storage for user: " + e.getMessage());
            // We don't fail the whole registration if MinIO is down, but we log it.
        }

        // 8. INITIALIZE SCORE
        scoringService.initializeScore(savedPerson);

        return generatedId;
    }
}
