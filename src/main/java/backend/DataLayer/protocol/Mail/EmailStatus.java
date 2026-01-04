package backend.DataLayer.protocol.Mail;

/**
 * Enum representing the status of an email address in the system.
 * Maps to PostgreSQL custom type: users.email_status
 */
public enum EmailStatus {
    /**
     * Email address has been created but not yet verified
     */
    PENDING,

    /**
     * Email address has been verified by the user
     */
    VERIFIED,

    /**
     * Email address has been marked as deleted
     */
    DELETED
}
