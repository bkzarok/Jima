package bkz.app.jima.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class RegisterFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer repasswordError;
    private boolean isDataValid;

    RegisterFormState(@Nullable Integer usernameError, @Nullable Integer passwordError ,@Nullable Integer repasswordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.repasswordError = repasswordError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.repasswordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getRePasswordError() {
        return repasswordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}