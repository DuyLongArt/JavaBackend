package backend.SecurityLayer.Middleware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleFilter extends OncePerRequestFilter {

    // Define endpoints as static constants for performance
    private static final Set<String> ADMIN_ENDPOINTS = Set.of(
            "admin", "object", "information", "person", "account", "auth", "report", "dashboard"
    );

    private static final Set<String> USER_ENDPOINTS = Set.of(
            "profile", "user", "object", "information", "person", "report","account"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. Get Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Check if user is not authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not authenticated");
            return;
        }

        // 3. Extract Roles into a Set for easy lookup
        Set<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // 4. Handle "First Time" / Anonymous logic
        if (userRoles.contains("ROLE_ANONYMOUS")) {
            // Allow access logic for anonymous/first-time users
            chain.doFilter(request, response);
            return;
        }

        // 5. Parse the URI safely
        // Your logic assumed split("/")[2]. Example: /backend/admin/... -> "admin" is index 2.
        // We add a check to prevent crashes on root or short paths.
        boolean isAuthorized = isIsAuthorized(request, userRoles);

        // 7. Final Decision
        if (isAuthorized) {
            chain.doFilter(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: You do not have permission for this endpoint.");
        }
    }

    private static boolean isIsAuthorized(HttpServletRequest request, Set<String> userRoles)
    {
        String[] pathParts = request.getRequestURI().split("/");
        String uriPrefix = (pathParts.length > 2) ? pathParts[2] : "";

        // 6. Check Permissions
        boolean isAuthorized = false;

        // CHECK ADMIN
        if (userRoles.contains("ROLE_ADMIN")) {
            if (ADMIN_ENDPOINTS.contains(uriPrefix)) {
                isAuthorized = true;
            }
        }

        // CHECK USER (If not already authorized as Admin)
        if (!isAuthorized && userRoles.contains("ROLE_USER")) {
            // Check against set OR specific startsWith logic you had
            if (USER_ENDPOINTS.contains(uriPrefix) ){
                isAuthorized = true;
            }
        }

        // CHECK VIEWER (If you have logic for this)
        if (!isAuthorized && userRoles.contains("ROLE_VIEWER")) {
            // Add viewer logic here if needed
            // isAuthorized = true;
        }
        return isAuthorized;
    }
}