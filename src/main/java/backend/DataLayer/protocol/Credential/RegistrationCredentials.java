
package backend.DataLayer.protocol.Credential;

import backend.DataLayer.protocol.Account.UserRole;
import backend.DataLayer.protocol.RoleTypes;
import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;

@Getter
@Setter
public class RegistrationCredentials implements Credential {

    private String userName;
    private String password;

    private UserRole role;
    private String email;
    private String firstName;
    private String lastName;
    private String deviceIP;

    private String bio;
    private String location;

    public RegistrationCredentials() {
    }

    public RegistrationCredentials(String userName, String password, UserRole role, String email, String firstName,
            String bio,
            String location,
            String deviceIP,
            String lastName) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.deviceIP = deviceIP;
        this.location = location;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDeviceIP(String deviceIP) {
        this.deviceIP = deviceIP;
    }

    public String getDeviceIP() {
        return deviceIP;
    }

    public String getBio() {
        return bio;
    }

    public String getLocation() {
        return location;
    }
}