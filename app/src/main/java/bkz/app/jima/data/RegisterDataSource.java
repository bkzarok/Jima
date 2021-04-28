package bkz.app.jima.data;

import java.io.IOException;

import bkz.app.jima.data.model.RegisteredUser;

/**
 * Class that handles authentication w/ register credentials and retrieves user information.
 */
public class RegisterDataSource {

    public RegisterResult<RegisteredUser> register(String firstName, String lastName, String username,
                                                   String email, String password, String rePassword) {

        try {
            // TODO: handle loggedInUser authentication
            RegisteredUser fakeUser =
                    new RegisteredUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new RegisterResult.Success<>(fakeUser);
        } catch (Exception e) {
            return new RegisterResult.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}