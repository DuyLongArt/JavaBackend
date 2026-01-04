package backend.DataLayer.protocol.Account;

/**
 * Enum representing user roles in the system.
 * Maps to PostgreSQL custom type: users.user_role
 */
public enum UserRole {
    /**
     * Standard user with basic permissions
     */
    USER,

    /**
     * Administrator with full system access
     */
    ADMIN,

    /**
     * Viewer with read-only permissions
     */
    VIEWER
}
