package bkz.app.jima.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String email;
    private String password;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String email, String password) {
        this.email = email;
        this.password = password;
    }

    String getEmail() {
        return email;
    }
    String getPassword(){return password;}
}