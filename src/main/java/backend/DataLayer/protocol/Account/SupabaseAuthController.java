package backend.DataLayer.protocol.Account;

import backend.DataLayer.protocol.Credential.RegistrationCredentials;
import backend.SecurityLayer.Authen.SupabaseJWTUtility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/backend/auth/supabase")
public class SupabaseAuthController {

    private final SupabaseJWTUtility supabaseJWTUtility;
    private final AccountDAO accountDAO;
    private final UserRegistrationService registrationService;

    public SupabaseAuthController(SupabaseJWTUtility supabaseJWTUtility, 
                                 AccountDAO accountDAO, 
                                 UserRegistrationService registrationService) {
        this.supabaseJWTUtility = supabaseJWTUtility;
        this.accountDAO = accountDAO;
        this.registrationService = registrationService;
    }

    @PostMapping("/sync")
    public ResponseEntity<?> syncUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        if (!supabaseJWTUtility.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Supabase token");
        }

        String email = supabaseJWTUtility.extractEmail(token);
        String fullName = supabaseJWTUtility.extractFullName(token);
        
        Optional<AccountEntity> existingAccount = accountDAO.findByEmailAddress(email);
        
        if (existingAccount.isPresent()) {
            return ResponseEntity.ok(Map.of(
                "message", "User already exists",
                "username", existingAccount.get().getUsername(),
                "status", "existing"
            ));
        }

        // Auto-register user if they don't exist
        try {
            RegistrationCredentials credentials = new RegistrationCredentials();
            credentials.setEmail(email);
            // Use email as username prefix if full name not available
            String username = email.split("@")[0] + "_" + java.util.UUID.randomUUID().toString().substring(0, 5);
            credentials.setUserName(username);
            
            if (fullName != null && !fullName.isBlank()) {
                String[] parts = fullName.split(" ", 2);
                credentials.setFirstName(parts[0]);
                if (parts.length > 1) {
                    credentials.setLastName(parts[1]);
                }
            } else {
                credentials.setFirstName(username);
            }
            
            // Password logic is handled by the service for OAuth users (it generates a dummy one)
            registrationService.registerUser(credentials);
            
            return ResponseEntity.ok(Map.of(
                "message", "User synced and created successfully",
                "username", username,
                "status", "created"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to sync user: " + e.getMessage());
        }
    }
}
