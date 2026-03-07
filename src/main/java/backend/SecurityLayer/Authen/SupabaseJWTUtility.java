package backend.SecurityLayer.Authen;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class SupabaseJWTUtility {

    private final SecretKey symmetricKey;
    private final java.util.Map<String, java.security.PublicKey> publicKeys = new java.util.HashMap<>();

    public SupabaseJWTUtility(@Value("${supabase.jwt.secret}") String secret) {
        // Legacy HS256 support
        this.symmetricKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        
        // Register current Supabase ES256 Public Keys from your project
        // These coordinates (x, y) were extracted from your project's JWKS endpoint
        try {
            registerPublicKey(
                "d47bc608-6587-485c-9bfc-013797897497", 
                "Xg8nvlfKNBkh1vMPEnyS9GqTsf5emdhJziokeXWrwaw", 
                "5UAafvFgfmydLf4loKsgwvH6H0EvTaB6XSZWcg8QOnA"
            );
            registerPublicKey(
                "0961166b-f173-40f6-8ee8-1aae5cf3a952", 
                "su4FsRgH7nUYGiVW4MyraECLIBHk3Q47kkZW7A6_eWY", 
                "qxDPppjVW4A4bl-GqP5aJ6SdPQEcjZHzX9ZzIDhVIhQ"
            );
        } catch (Exception e) {
            System.err.println("Failed to initialize ES256 public keys: " + e.getMessage());
        }
    }

    private void registerPublicKey(String kid, String xBase64, String yBase64) throws Exception {
        byte[] xBytes = java.util.Base64.getUrlDecoder().decode(xBase64);
        byte[] yBytes = java.util.Base64.getUrlDecoder().decode(yBase64);
        
        java.math.BigInteger x = new java.math.BigInteger(1, xBytes);
        java.math.BigInteger y = new java.math.BigInteger(1, yBytes);
        
        java.security.spec.ECPoint w = new java.security.spec.ECPoint(x, y);
        
        // Use standard Java security to get P-256 (secp256r1) parameters
        java.security.AlgorithmParameters params = java.security.AlgorithmParameters.getInstance("EC");
        params.init(new java.security.spec.ECGenParameterSpec("secp256r1"));
        java.security.spec.ECParameterSpec ecParams = params.getParameterSpec(java.security.spec.ECParameterSpec.class);
        
        java.security.spec.ECPublicKeySpec spec = new java.security.spec.ECPublicKeySpec(w, ecParams);
        java.security.KeyFactory kf = java.security.KeyFactory.getInstance("EC");
        publicKeys.put(kid, kf.generatePublic(spec));
    }


    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public String extractFullName(String token) {
        return extractClaim(token, claims -> {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> metadata = claims.get("user_metadata", java.util.Map.class);
            if (metadata != null) {
                return (String) metadata.get("full_name");
            }
            return null;
        });
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKeyResolver(new io.jsonwebtoken.SigningKeyResolverAdapter() {
                    @Override
                    public java.security.Key resolveSigningKey(io.jsonwebtoken.JwsHeader header, Claims claims) {
                        String alg = header.getAlgorithm();
                        String kid = (String) header.get("kid");
                        
                        if ("ES256".equals(alg)) {
                            if (publicKeys.containsKey(kid)) {
                                return publicKeys.get(kid);
                            }
                            System.err.println("Unknown kid for ES256: " + kid);
                        }
                        
                        // Default to symmetric key for HS256 or unknown
                        return symmetricKey;
                    }
                })
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            final Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            System.err.println("Supabase JWT Validation Error: " + e.getMessage());
            return false;
        }
    }
}

