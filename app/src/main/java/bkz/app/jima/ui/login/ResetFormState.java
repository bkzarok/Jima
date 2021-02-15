package bkz.app.jima.ui.login;

import androidx.annotation.Nullable;

class ResetFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

    ResetFormState(@Nullable Integer usernameError) {
        this.usernameError = usernameError;
        this.isDataValid = false;
    }

    ResetFormState(boolean isDataValid) {
        this.usernameError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}