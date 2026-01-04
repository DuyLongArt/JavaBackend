package backend.SecurityLayer.protocol;

import backend.DataLayer.protocol.Account.AccountEntity;

public class AuthenticationResponseEntity {

    public AuthenticationResponseEntity(AccountEntity account) {
        this.accountEntity = account;
    }

    private final AccountEntity accountEntity;

    String getAccessToken() {
        return null;
    }

    // String getRefreshToken();
    // String getTokenType();
    // long getExpiresIn();

    String getInformation() {
        String username = accountEntity.getUsername();
        String personName = accountEntity.getIdentity()!= null ? accountEntity.getIdentity().getFirstName(): "No name";
        return "Username: " + username + ", Name: " + personName;
    }
}
