package backend.DataLayer.protocol.Account;

import backend.DataLayer.protocol.RoleTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDAO extends JpaRepository<AccountEntity, Integer> {

    /**
     * Find account by username.
     * 
     * @param username the username to search for
     * @return AccountEntity matching the username, or null if not found
     */
//    AccountEntity findByUsername(String username);

    /**
     * Check if an account exists with the given username.
     * 
     * @param username the username to check
     * @return true if account exists
     */
    boolean existsByUsername(String username);

    /**
     * Find account by its associated person's identity ID.
     * 
     * @param identityId the identity ID to search for
     * @return Optional containing the account if found
     */
    @Query("SELECT a FROM AccountEntity a WHERE a.identity.id = :identityId")
    Optional<AccountEntity> findByPersonIdentityId(@Param("identityId") Integer identityId);

    @Query("SELECT a FROM AccountEntity a WHERE a.username = :username")
    Optional<AccountEntity> findAccountlByUsername(@Param("username") String username);

    @Query("UPDATE AccountEntity a SET a.role = :role WHERE a.username=:username")
    void updateAccountRoleByUsername(@Param("username") String username, @Param("role")RoleTypes role);
    /**
     * Find account by primary email ID.
     * 
     * @param emailId the primary email ID
     * @return Optional containing the account if found
     */
    Optional<AccountEntity> findByPrimaryEmailId(Integer emailId);

    /**
     * Find all locked accounts.
     * 
     * @return List of locked accounts
     */
    @Query("SELECT a FROM AccountEntity a WHERE a.isLocked = true")
    java.util.List<AccountEntity> findAllLockedAccounts();

    /**
     * Find accounts with failed login attempts greater than specified count.
     * 
     * @param count the minimum number of failed login attempts
     * @return List of accounts with failed login attempts exceeding count
     */
    @Query("SELECT a FROM AccountEntity a WHERE a.failedLoginAttempts >= :count")
    java.util.List<AccountEntity> findByFailedLoginAttemptsGreaterThanEqual(@Param("count") Integer count);

    /**
     * Find accounts that have never logged in.
     * 
     * @return List of accounts with no login history
     */
    @Query("SELECT a FROM AccountEntity a WHERE a.lastLoginAt IS NULL")
    java.util.List<AccountEntity> findAccountsNeverLoggedIn();

    /**
     * Find accounts by role.
     * 
     * @param role the user role
     * @return List of accounts with the specified role
     */
    @Query("SELECT a FROM AccountEntity a WHERE a.role = :role")
    java.util.List<AccountEntity> findByRole(@Param("role") Object role);

    /**
     * Count total locked accounts.
     * 
     * @return count of locked accounts
     */
    @Query("SELECT COUNT(a) FROM AccountEntity a WHERE a.isLocked = true")
    Long countLockedAccounts();

    AccountEntity findAccountEntityByUsername(String username);
    
    @Query("SELECT a FROM AccountEntity a JOIN EmailEntity e ON a.identity.id = e.identity.id WHERE e.emailAddress = :email")
    Optional<AccountEntity> findByEmailAddress(@Param("email") String email);

}