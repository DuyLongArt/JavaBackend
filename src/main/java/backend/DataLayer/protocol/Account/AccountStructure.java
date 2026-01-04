package backend.DataLayer.protocol.Account;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Core account structure interface defining user account properties and operations.
 * Implementations should ensure proper validation and security practices.
 */
public interface AccountStructure
{

    // mail validation pattern
    Pattern MAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Username validation pattern (alphanumeric, underscore, hyphen, 3-50 chars)
    Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{3,50}$");

    int getId();

    String getUserName();

    String getMail();

//    String getPasswordHash();

    String getRole();

    String getFullName();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    LocalDateTime getLastLogin();

    void setId(int id);

    void setUserName(String userName);

    void setMail(String mail);

    void setPasswordHash(String password);

    String getPasswordHash();

    void setRole(String role);

    void setFullName(String fullName);

    void setCreatedAt(LocalDateTime createdAt);

    void setUpdatedAt(LocalDateTime updatedAt);

    void setLastLogin(LocalDateTime lastLogin);
    String getAllInformation();

    // Default methods (validation, utility) are fine in the interface
    default boolean isValidUserName(String userName)
    {
        return userName != null && USERNAME_PATTERN.matcher(userName).matches();
    }

    default boolean isValidMail(String mail)
    {
        return mail != null && MAIL_PATTERN.matcher(mail).matches();
    }

    default boolean isValid()
    {
        return getId() > 0 &&
                isValidUserName(getUserName()) &&
                isValidMail(getMail()) &&
                getPasswordHash() != null && !getPasswordHash().trim().isEmpty() &&
                getRole() != null && !getRole().trim().isEmpty() &&
                getCreatedAt() != null;
    }

    default boolean isActive()
    {
        LocalDateTime now = LocalDateTime.now();
        return getCreatedAt() != null &&
                !getCreatedAt().isAfter(now) &&
                (getUpdatedAt() == null || !getUpdatedAt().isBefore(getCreatedAt()));
    }

    default void touch()
    {
        setUpdatedAt(LocalDateTime.now());
    }

    default boolean isAdmin()
    {
        String role = getRole();
        return role != null &&
                (role.equalsIgnoreCase("ADMIN") ||
                        role.equalsIgnoreCase("ADMINISTRATOR") ||
                        role.equalsIgnoreCase("ROOT"));
    }

    default long getAccountAgeInDays()
    {
        if (getCreatedAt() == null) return -1;
        return java.time.temporal.ChronoUnit.DAYS.between(getCreatedAt(), LocalDateTime.now());
    }

    default String toSafeString()
    {
        return String.format("Account{id=%d, userName='%s', mail='%s', role='%s', created=%s}",
                getId(),
                getUserName(),
                maskmail(getMail()),
                getRole(),
                getCreatedAt()
        );
    }

    default String maskmail(String mail)
    {
        if (mail == null || !mail.contains("@")) return "***";
        String[] parts = mail.split("@");
        String localPart = parts[0];
        String domain = parts[1];
        if (localPart.length() <= 2)
        {
            return "*".repeat(localPart.length()) + "@" + domain;
        }
        return localPart.charAt(0) + "*".repeat(localPart.length() - 2) +
                localPart.charAt(localPart.length() - 1) + "@" + domain;
    }
}