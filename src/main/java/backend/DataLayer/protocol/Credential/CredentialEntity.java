package backend.DataLayer.protocol.Credential;

import backend.DataLayer.protocol.RoleTypes;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class CredentialEntity implements Credential {
    @Id
    private BigInteger credentialId;
    private String username;
    private String password;
    private boolean rememberMe;
    private String deviceid;
    private String ipAddress;
    private String accessJWT;
    private String jsonWebToken;
    private LocalDateTime createdAt = LocalDateTime.now();

    public CredentialEntity(String username, String password, boolean rememberMe, String deviceid, String ipAddress) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
        this.deviceid = deviceid;
        this.ipAddress = ipAddress;
    }

    public CredentialEntity() {

    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

  

    public RoleTypes getRole() {
        return null;
    }

    public String jsonWebToken() {
        return "";
    }

    public boolean getRememberMe() {
        return rememberMe;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getAccessJWT() {
        return accessJWT;
    }

    public void setAccessJWT(String accessJWT) {
        this.accessJWT = accessJWT;
    }
}
