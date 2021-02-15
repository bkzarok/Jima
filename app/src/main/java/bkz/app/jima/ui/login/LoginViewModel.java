package bkz.app.jima.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import bkz.app.jima.data.LoginRepository;
import bkz.app.jima.data.Result;
import bkz.app.jima.data.model.LoggedInUser;
import bkz.app.jima.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<ResetFormState> resetFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }
    LiveData<ResetFormState> getResetFormState() {
        return resetFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }
    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(username,password)));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }
    public void register(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            registerResult.setValue(new RegisterResult(new LoggedInUserView(username,password)));
        } else {
            registerResult.setValue(new RegisterResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        }
        else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    public void registerDataChanged(String password, String repassword)
    {
        if(!isPasswordSame(password, repassword ))
        {
            registerFormState.setValue(new RegisterFormState(null, null, R.string.different_password));
        }
        else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }
    public void resetDataChanged(String username)
    {
        if (!isUserNameValid(username)) {
            resetFormState.setValue(new ResetFormState(R.string.invalid_username));
        }
        else {
            resetFormState.setValue(new ResetFormState(true));
        }
    }
    private boolean isPasswordSame(String pass1, String pass2)
    {
        return pass1.equals(pass2);
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}