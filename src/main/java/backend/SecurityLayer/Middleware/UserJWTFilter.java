package backend.SecurityLayer.Middleware;

import backend.DataLayer.protocol.Account.AccountDAO;
import backend.SecurityLayer.Authen.JWTGeneration;
//import backend.SecurityLayer.Authen.JWTUtility;
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
    private final UserService userDetailsService;
    // AccountDAO appears unused in this logic, but keeping it if you need it later
    private final AccountDAO accountDAO;

    public UserJWTFilter(JWTGeneration jwtGeneration, AccountDAO accountDAO, UserService userDetailsService) {
        this.jwtGeneration = jwtGeneration;
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

        // 1. Check if token is missing
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // FIX: Pass the request down the chain instead of just returning
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract Token
        jwt = authHeader.substring(7);

        try {
            System.out.println("Extracting jwt from token"+jwt);
            System.out.println(jwt.getBytes(StandardCharsets.UTF_8));
            username = jwtGeneration.extractUsername(jwt);
//            username=jwtGeneration.e

            System.out.println("Extracting username from token: "+username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                System.out.println("Is active: "+userDetails.isEnabled());
                if (jwtGeneration.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    // Ideally, add WebAuthenticationDetails here
                    // authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Log error but don't crash; let Spring Security handle the 403/401 later if context is empty
            System.err.println("JWT processing failed: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        // 3. Continue the chain
        filterChain.doFilter(request, response);
    }
}