package backend.SecurityLayer.Middleware;

import backend.DataLayer.protocol.Account.AccountDAO;
import backend.SecurityLayer.Authen.JWTGeneration;
import backend.SecurityLayer.Authen.SupabaseJWTUtility;
import backend.SecurityLayer.Authen.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    // AccountDAO appears unused in this logic, but keeping it if you need it later
    private final AccountDAO accountDAO;

    public UserJWTFilter(JWTGeneration jwtGeneration, SupabaseJWTUtility supabaseJWTUtility, AccountDAO accountDAO, UserService userDetailsService) {
        this.jwtGeneration = jwtGeneration;
        this.supabaseJWTUtility = supabaseJWTUtility;
        this.accountDAO = accountDAO;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if(request.getRequestURI().contains("signup")){
            filterChain.doFilter(request, response);
            return;
        }
        // 1. Check if token is missing
        if (authHeader == null || !authHeader.startsWith("Bearer ")
               ) {
            // FIX: Pass the request down the chain instead of just returning
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
            if (userDetails == null && supabaseJWTUtility.validateToken(jwt)) {
                String email = supabaseJWTUtility.extractEmail(jwt);
                System.out.println("Supabase token valid for email: " + email);
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        userDetails = this.userDetailsService.loadUserByEmail(email);
                    } catch (Exception e) {
                        System.err.println("User not found for Supabase email: " + email);
                    }
                }
            }

            // 3. Set Authentication if user found
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication set for user: " + userDetails.getUsername());
            }

        } catch (Exception e) {
            System.err.println("JWT processing failed: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        // 3. Continue the chain
        filterChain.doFilter(request, response);
    }
}