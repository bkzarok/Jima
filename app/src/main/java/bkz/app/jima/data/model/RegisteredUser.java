package bkz.app.jima.data.model;

/**
 * Data class that captures user information for logged in users retrieved from RegisterRepository
 */
public class RegisteredUser {

    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private String displayName;


    public RegisteredUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}