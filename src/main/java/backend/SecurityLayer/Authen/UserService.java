package backend.SecurityLayer.Authen;

import backend.DataLayer.protocol.Account.AccountDAO;
import backend.DataLayer.protocol.Account.AccountEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User; // Spring Security's built-in UserDetails implementation
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final AccountDAO accountDAO;

    public UserService(AccountDAO accountDAO)
    {
        this.accountDAO = accountDAO;
    }


    @Transactional()
//    @Transactional(readOnly = true) /
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Fetch the custom entity from the database
        AccountEntity accountEntity =
        accountDAO.findAccountEntityByUsername(username);

//        accountDAO.findAccountEntityByUsername


        if (accountEntity == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // 2. Map the AccountEntity to a Spring Security UserDetails object

        // A. Convert the custom roles into a List of GrantedAuthority
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + accountEntity.getRole()));

        // B. Create and return the UserDetails object

        return createSpringUser(accountEntity);
    }

    @Transactional
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        AccountEntity accountEntity = accountDAO.findByEmailAddress(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return createSpringUser(accountEntity);
    }

    private User createSpringUser(AccountEntity accountEntity) {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + accountEntity.getRole()));

        return new User(
                accountEntity.getUsername(),
                accountEntity.getPasswordHash(),
                accountEntity.getIdentity() != null && accountEntity.getIdentity().getIsActive(),
                true,
                true,
                !accountEntity.getIsLocked(),
                authorities
        );
    }
}