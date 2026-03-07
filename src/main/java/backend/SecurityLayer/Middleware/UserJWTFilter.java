package backend.SecurityLayer.Middleware;

import backend.DataLayer.protocol.Account.AccountDAO;
import backend.DataLayer.protocol.Account.UserRegistrationService;
import backend.DataLayer.protocol.Credential.RegistrationCredentials;
import backend.SecurityLayer.Authen.JWTGeneration;
import backend.SecurityLayer.Authen.SupabaseJWTUtility;
import backend.SecurityLayer.Authen.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class UserJWTFilter extends OncePerRequestFilter {

    private final JWTGeneration jwtGeneration;
    private final SupabaseJWTUtility supabaseJWTUtility;
    private final UserService userDetailsService;
    private final UserRegistrationService registrationService;
    // AccountDAO appears unused in this logic, but keeping it if you need it later
    private final AccountDAO accountDAO;

    public UserJWTFilter(JWTGeneration jwtGeneration, 
                        SupabaseJWTUtility supabaseJWTUtility, 
                        AccountDAO accountDAO, 
                        UserService userDetailsService,
                        @Lazy UserRegistrationService registrationService) {
        this.jwtGeneration = jwtGeneration;
        this.supabaseJWTUtility = supabaseJWTUtility;
        this.accountDAO = accountDAO;
        this.userDetailsService = userDetailsService;
        this.registrationService = registrationService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (request.getRequestURI().contains("signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("--- Entering UserJWTFilter for URI: " + request.getRequestURI() + " ---");

        // 1. Check if token is missing
        if (authHeader == null) {
            System.out.println("No Authorization header found. Skipping JWT validation.");
            filterChain.doFilter(request, response);
            return;
        } else if (!authHeader.startsWith("Bearer ")) {
            System.out.println("Authorization header found but does not start with Bearer. Header: " + authHeader);
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract Token
        jwt = authHeader.substring(7);

        try {
            System.out.println("Processing token: " + jwt);
            UserDetails userDetails = null;

            // 1. Try Local JWT Verification
            try {
                username = jwtGeneration.extractUsername(jwt);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    userDetails = this.userDetailsService.loadUserByUsername(username);
                    if (!jwtGeneration.validateToken(jwt, userDetails)) {
                        userDetails = null;
                    }
                }
            } catch (Exception e) {
                System.out.println("Local JWT verification failed, trying Supabase...");
            }

            // 2. Try Supabase JWT Verification if local failed
            if (userDetails == null) {
                System.out.println("Local user lookup failed or token invalid, check Supabase...");
                if (supabaseJWTUtility.validateToken(jwt)) {
                    String userId = supabaseJWTUtility.extractUserId(jwt);
                    System.out.println("Supabase token is VALID. UID: " + userId);
                    
                    if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        try {
                            userDetails = this.userDetailsService.loadUserByAlias(userId);
                            System.out.println("Found existing local user for alias: " + userId);
                        } catch (Exception e) {
                            System.out.println("No local user found for alias: " + userId + ". Starting auto-registration...");
                            try {
                                String email = supabaseJWTUtility.extractEmail(jwt);
                                String fullName = supabaseJWTUtility.extractFullName(jwt);
                                System.out.println("Extracted from JWT - Email: " + email + ", Name: " + fullName);
                                
                                RegistrationCredentials credentials = new RegistrationCredentials();
                                credentials.setEmail(email);
                                credentials.setAlias(userId);
                                
                                // Generate a unique username based on email
                                String generatedUsername = email.split("@")[0] + "_" + java.util.UUID.randomUUID().toString().substring(0, 5);
                                credentials.setUserName(generatedUsername);
                                System.out.println("Generated Username: " + generatedUsername);
                                
                                if (fullName != null && !fullName.isBlank()) {
                                    String[] parts = fullName.split(" ", 2);
                                    credentials.setFirstName(parts[0]);
                                    if (parts.length > 1) {
                                        credentials.setLastName(parts[1]);
                                    }
                                } else {
                                    credentials.setFirstName(generatedUsername);
                                    credentials.setLastName("");
                                }
                                
                                // Trigger registration
                                System.out.println("Calling registrationService.registerUser...");
                                registrationService.registerUser(credentials);
                                System.out.println("User auto-registered successfully: " + generatedUsername);
                                
                                // Load the newly created user
                                userDetails = this.userDetailsService.loadUserByAlias(userId);
                                System.out.println("Loaded newly registered user Details.");
                            } catch (Exception regEx) {
                                System.err.println("CRITICAL: Auto-registration failed for UID " + userId);
                                regEx.printStackTrace();
                            }
                        }
                    }
                } else {
                    System.out.println("Supabase token validation FAILED for token: " + (jwt.length() > 20 ? jwt.substring(0, 20) + "..." : jwt));
                }
            }

            // 3. Set Authentication if user found
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Successfully authenticated user: " + userDetails.getUsername());
            } else {
                System.out.println("Authentication FAILED: No userDetails found.");
            }

        } catch (Exception e) {
            System.err.println("JWT processing block threw unexpected exception: " + e.getMessage());
            e.printStackTrace();
            SecurityContextHolder.clearContext();
        }


        // 3. Continue the chain
        filterChain.doFilter(request, response);
    }
}