package backend.SecurityLayer.protocol;

public class RegistrationResponse {
    private String message;
    private String userName;
    private String email;

    public RegistrationResponse(String message, String userName, String email) {
        this.message = message;
        this.userName = userName;
        this.email = email;
    }
}