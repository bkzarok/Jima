package bkz.app.jima.register_ui.ui.register;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class RegisterFormState {
    @Nullable
    private Integer firstNameError;
    @Nullable
    private Integer lastNameError;
    @Nullable
    private Integer userNameError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer rePasswordError;

    private boolean isDataValid;

    RegisterFormState(
                      @Nullable Integer firstNameError,
                      @Nullable Integer lastNameError,
                      @Nullable Integer userNameError,
                      @Nullable Integer emailError,
                      @Nullable Integer passwordError,
                      @Nullable Integer rePasswordError) {
        this.firstNameError = firstNameError;
        this.lastNameError = lastNameError;
        this.userNameError = userNameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.rePasswordError = rePasswordError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.firstNameError = null;
        this.lastNameError = null;
        this.userNameError = null;
        this.emailError = null;
        this.passwordError = null;
        this.rePasswordError = null;

        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getFirstNameError() {
        return firstNameError;
    }
    @Nullable
    Integer getLastNameError() {
        return lastNameError;
    }

    @Nullable
    Integer getUserNameError() {
        return userNameError;
    }
    @Nullable
    Integer getEmailError() {
        return emailError;
    }


    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }
    @Nullable
    Integer getRePasswordError() {
        return rePasswordError;
    }


    boolean isDataValid() {
        return isDataValid;
    }
}