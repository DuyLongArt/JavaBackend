package backend.DataLayer.protocol.Credential;

import backend.DataLayer.protocol.Account.UserRole;
import backend.DataLayer.protocol.RoleTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.usertype.UserType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginCredential implements Credential {
    private String userName;
    private String password;
    private String email;
    private String jwt;
//   private UserRole userRole=UserRole.USER;

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

}
