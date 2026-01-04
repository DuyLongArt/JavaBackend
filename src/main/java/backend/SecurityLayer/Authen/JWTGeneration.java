package backend.SecurityLayer.Authen;

import backend.DataLayer.protocol.Account.AccountDAO;
import backend.DataLayer.protocol.Account.AccountEntity;
import backend.DataLayer.protocol.Account.UserRole;
import backend.DataLayer.protocol.Credential.LoginCredential;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JWTGeneration {

    private final SecretKey signingKey;
    private final Long jwtExpiration;
    private final Long refreshExpiration;

    @Autowired
    AccountDAO accountDAO;
    /**
     * Constructs the JWTUtility with required configuration properties.
     * This uses constructor injection, which is a Spring best practice.
     *
     * @param secret            The secret key for signing tokens, from application
     *                          properties.
     * @param jwtExpiration     The expiration time for access tokens, from
     *                          application properties.
     * @param refreshExpiration The expiration time for refresh tokens, from
     *                          application properties.
     */
    public JWTGeneration(@Value("${app.jwt.secret}") String secret,
                         @Value("${app.jwt.expiration}") Long jwtExpiration,
                         @Value("${app.jwt.refresh-expiration}") Long refreshExpiration) {
        // Specify StandardCharsets.UTF_8 to ensure consistency between signing and parsing
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        this.jwtExpiration = jwtExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    /**
     * Get secret key for signing JWT tokens.
     */
    private SecretKey getSigningKey() {
        return this.signingKey;
    }

    /**
     * Extracts the username (subject) from a JWT token.
     *
     * @param token The JWT token.
     * @return The username.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // This looks for the "sub" field
    }
    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param token The JWT token.
     * @return The expiration date.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * A generic function to extract a specific claim from a JWT token.
     *
     * @param token          The JWT token.
     * @param claimsResolver A function to resolve the desired claim from the Claims
     *                       object.
     * @param <T>            The type of the claim.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses a JWT token and verifies its signature to extract all claims.
     * This method uses the modern jjwt parser builder.
     *
     * @param token The JWT token string.
     * @return The claims contained in the token.
     * @throws JwtException if the token is invalid, expired, or malformed.
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token) // Verifies the signature
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token is expired", e);
        } catch (UnsupportedJwtException e) {
            throw new JwtException("JWT token is unsupported", e);
        } catch (MalformedJwtException e) {
            throw new JwtException("Invalid JWT token", e);
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature", e);
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT claims string is empty or invalid", e);
        }
    }

    /**
     * Checks if a JWT token has expired.
     *
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    /**
     * Generates a JWT access token for an authenticated user.
     *
     * @param authentication  The Authentication object from Spring Security.
     * @param loginCredential Additional details from the login request.
     * @return A signed JWT access token.
     */
//    public String generateToken(Authentication authentication, LoginCredential loginCredential) {
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String username = userDetails.getUsername();
//
//        String role = userDetails.getAuthorities().stream()
//                .filter(a -> a.getAuthority().startsWith("ROLE_"))
//                .findFirst()
//                .map(a -> a.getAuthority().substring(5)) // Strip "ROLE_" prefix
//                .orElse("USER");
//
//        return generateToken( loginCredential);
//    }

    /**
     * Generates a JWT access token given username and role directly.
     * Useful for mocking or when Authentication object is not available.
     *
     * @param username        The username.
     * @param role            The user's role.
     * @param loginCredential Additional details from the login request.
     * @return A signed JWT access token.
     */
    public String generateToken(LoginCredential loginCredential) {
        Map<String, Object> claims = new HashMap<>();
        String username = loginCredential.getUserName();
        String userRole=accountDAO.findAccountlByUsername(username).get().getRole().toString();



        claims.put("username", username);
        claims.put("role", userRole);
//        claims.put("role", loginCredential.getUserRole().toString());



        return createToken(claims, username, jwtExpiration);
    }

    /**
     * Generates a JWT refresh token for a user.
     *
     * @param userDetails The UserDetails object for the user.
     * @return A signed JWT refresh token.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return createToken(claims, userDetails.getUsername(), refreshExpiration);
    }

    /**
     * Helper method to create a JWT token with the given claims, subject, and
     * expiration.
     *
     * @param claims     The claims to include in the token body.
     * @param subject    The subject of the token (usually the username).
     * @param expiration The expiration time in milliseconds.
     * @return The compacted, signed JWT string.
     */
    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Use a specific signature algorithm
                .compact();
    }

    /**
     * Validates a JWT token against a user's details.
     *
     * @param token         The JWT token.
     * @param accountEntity The account entity to validate against.
     * @return True if the token is valid for the user and not expired.
     */
    public boolean validateToken(String token, AccountEntity accountEntity) {
        try {
            final String username = extractUsername(token);
            return (username.equals(accountEntity.getUsername()) && !isTokenExpired(token));
        } catch (JwtException e) {
            // Log the exception if you have a logger
            return false;
        }
    }

    /**
     * Validates a JWT token against a UserDetails object.
     *
     * @param token       The JWT token.
     * @param userDetails The UserDetails to validate against.
     * @return True if the token is valid for the user and not expired.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Validates a JWT token's integrity and expiration without checking against
     * user details.
     *
     * @param token The JWT token.
     * @return True if the token is well-formed and not expired.
     */
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException e) {
            // Log the exception if you have a logger
            return false;
        }
    }

    /**
     * Calculates the remaining time until the token expires.
     *
     * @param token The JWT token.
     * @return The remaining time in milliseconds.
     */
    public Long getExpirationTime(String token) {
        Date expiration = extractExpiration(token);
        return expiration.getTime() - new Date().getTime();
    }
}
