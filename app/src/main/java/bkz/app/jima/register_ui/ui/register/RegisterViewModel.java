package bkz.app.jima.register_ui.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import bkz.app.jima.data.RegisterRepository;
import bkz.app.jima.data.RegisterResult;
import bkz.app.jima.data.model.RegisteredUser;
import bkz.app.jima.R;
import java.util.regex.*;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<bkz.app.jima.register_ui.ui.register.RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;

    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    LiveData<RegisterFormState> getregisterFormState() {
        return registerFormState;
    }

    LiveData<bkz.app.jima.register_ui.ui.register.RegisterResult> getregisterResult() {
        return registerResult;
    }

    public void register(String firstName, String lastName,
                         String username, String email, String password, String rePassword) {
        // can be launched in a separate asynchronous job
        RegisterResult<RegisteredUser> registerResult = registerRepository.register(firstName,lastName,
                username, email,password,rePassword);

        if (registerResult instanceof RegisterResult.Success) {
            RegisteredUser data = ((RegisterResult.Success<RegisteredUser>) registerResult).getData();
            this.registerResult.setValue(new bkz.app.jima.register_ui.ui.register.RegisterResult(new RegisterInUserView(data.getDisplayName())));
        } else {
            this.registerResult.setValue(new bkz.app.jima.register_ui.ui.register.RegisterResult(R.string.register_failed));
        }
    }

    public void registerDataChanged(String firstName, String lastName,
                                    String username, String email, String password, String rePassword) {
        if (!isFirstNameValid(firstName)) {
            Log.d("firstName", "in regsiter data changed first name");
            registerFormState.setValue(new RegisterFormState(R.string.invalid_first_name,
                    null,null,null,null,null));
        } else if (!isLastNameValid(lastName)) {
            registerFormState.setValue(new RegisterFormState(null,
                    R.string.invalid_last_name,null,null,null,null));
        } else if (!isUserNameValid(username)) {
            registerFormState.setValue(new RegisterFormState(null,
                    null,R.string.invalid_username,null,null,null));
        } else if (!isEmailValid(email)) {
            registerFormState.setValue(new RegisterFormState(null,
                    null,null,R.string.invalid_email,null,null));
        } else  if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null,
                    null,null,null, R.string.invalid_password,null));
        }
        else  if (!isRePasswordValid(rePassword,password)) {
            registerFormState.setValue(new RegisterFormState(null,
                    null,null,null, null,R.string.invalid_re_password));
        }else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    private boolean isFirstNameValid(String firstName) {
        if (firstName == null) {
            return false;
        }
        return Pattern.matches("[a-zA-Z0-9]{1,9}", firstName);
    }
    private boolean isLastNameValid(String lastName) {
        if (lastName == null) {
            return false;
        }
       return Pattern.matches("[a-zA-Z0-9]{1,9}", lastName);
    }



    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        return Pattern.matches("[@]{1}[a-zA-Z0-9_-]{1,20}", username);
    }

    // A placeholder username validation check
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return  false;
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    // A placeholder password validation check
    private boolean isRePasswordValid(String rePassword, String password) {
        return rePassword.equals(password);
    }

}