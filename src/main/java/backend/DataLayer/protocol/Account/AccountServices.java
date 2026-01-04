package backend.DataLayer.protocol.Account;

import java.time.LocalDateTime; // Import the proper class
import java.time.format.DateTimeFormatter; // For optional formatting

// Assuming AccountEntity is a separate class defined elsewhere

public class AccountServices {

    /**
     * Creates and formats a String containing account credentials and an update
     * timestamp.
     * Note: This method should ideally be used for logging or display,
     * not for storing the credentials themselves.
     *
     * @param accountEntity The AccountEntity object containing the credentials.
     * @return A formatted String of the credentials and the current timestamp.
     */
    static String createAccountCredentials(AccountEntity accountEntity) {

        // --- Handle the "updatedAt now" request ---
        // 1. Get the current time
        LocalDateTime now = LocalDateTime.now();

        // 2. Define a simple format for the timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        // 3. Optional: Update the timestamp in the entity (assuming a setter exists)
        // accountEntity.setUpdatedAt(now);

        // --- Return the formatted string ---
        return "Username: " + accountEntity.getUsername() + "\n" +
                "PasswordHash: " + accountEntity.getPasswordHash() + "\n" +
                "UpdatedAt: " + formattedDateTime;
    }
}