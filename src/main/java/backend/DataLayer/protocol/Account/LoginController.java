package backend.DataLayer.protocol.Account;

import backend.DataLayer.protocol.Credential.LoginCredential;
import backend.SecurityLayer.Authen.JWTGeneration;
//import backend.SecurityLayer.Authen.JWTUtility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController

@RequestMapping("/backend/")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final AuthenticationManager authenticationManager;
    private final JWTGeneration jwtGenerator;

    public LoginController(AuthenticationManager authenticationManager, JWTGeneration jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }

    @GetMapping("auth/test")
    public ResponseEntity<Map<String, String>> test() {
        logger.info("Test endpoint hit");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("message", "TEST SUCCESS"));
    }

    @PostMapping("auth/login")
    public ResponseEntity<?> login(@RequestBody LoginCredential loginCredential) {


        System.out.println(loginCredential);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("Start authentication");

//        System.out.println("Authentication successful: " + authentication);
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginCredential.getUserName(),
                             loginCredential.getPassword()));

//                System.out.println("Authentication successful: " + authentication);
            // Generate JWT token use the generateToken method that includes claims
            String jwt = jwtGenerator.generateToken( loginCredential);

            // Return success response with token
            return ResponseEntity.ok(
                    Map.of(
                            "message", "Login successful",
                            "token", jwt));

        } catch (BadCredentialsException e) {
            logger.warn("Authentication failed: Invalid credentials for user: {}", loginCredential.getUserName());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));

        } catch (Exception e) {
            logger.error("Authentication error: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An unexpected error occurred: " + e.getMessage()));
        }
    }
}