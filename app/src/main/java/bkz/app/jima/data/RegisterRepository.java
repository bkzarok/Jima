package bkz.app.jima.data;

import bkz.app.jima.data.model.RegisteredUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of register status and user credentials information.
 */
public class RegisterRepository {

    private static volatile RegisterRepository instance;

    private RegisterDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private RegisteredUser user = null;

    // private constructor : singleton access
    private RegisterRepository(RegisterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RegisterRepository getInstance(RegisterDataSource dataSource) {
        if (instance == null) {
            instance = new RegisterRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(RegisteredUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public RegisterResult<RegisteredUser> register(String firstName, String lastName, String username,
                                                   String email, String password, String rePassword) {
        // handle register
        RegisterResult<RegisteredUser> registerResult = dataSource.register(firstName, lastName,
                username, email, password, rePassword);
        if (registerResult instanceof RegisterResult.Success) {
            setLoggedInUser(((RegisterResult.Success<RegisteredUser>) registerResult).getData());
        }
        return registerResult;
    }
}