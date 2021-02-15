package bkz.app.jima.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import bkz.app.jima.MainActivity;
import bkz.app.jima.R;
import bkz.app.jima.data.model.User;
import bkz.app.jima.services.FireBaseServices;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Auth Tag";
    private LoginViewModel loginViewModel;
    private FirebaseAuth mAuth;
    private LoginButton floginButton;
    private boolean firstClick = true;
    private boolean resetFirstClick = true;
    CallbackManager callbackManager;
    private FireBaseServices fireBaseServices;
    private  EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordEditText2;
    private Button loginButton;
    private Button registerButton;
    private Button backButton;
    private ProgressBar loadingProgressBar;
    private TextView forgotPassword;
    private Button resetPassword;
    private TextView resetInfo;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        usernameEditText =  (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        passwordEditText2 = (EditText) findViewById(R.id.re_password);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        loginButton = (Button) findViewById(R.id.login);
        registerButton = (Button) findViewById(R.id.register);
        backButton = (Button) findViewById(R.id.back);
        resetPassword = (Button) findViewById(R.id.resetPassword);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loading);
        resetInfo = (TextView) findViewById(R.id.resetinfo);
        FirebaseApp.initializeApp(this);

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        fireBaseServices = new FireBaseServices();
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });
        loginViewModel.getResetFormState().observe(this, new Observer<ResetFormState>() {
            @Override
            public void onChanged(@Nullable ResetFormState resetFormState) {
                if (resetFormState == null) {
                    return;
                }
                resetPassword.setEnabled(resetFormState.isDataValid());
                if (resetFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(resetFormState.getUsernameError()));
                }
            }
        });

        loginViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(RegisterFormState registerFormState) {

                if (registerFormState == null) {
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(registerFormState.getUsernameError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
                if (registerFormState.getRePasswordError() != null) {
                    passwordEditText2.setError(getString(registerFormState.getRePasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    Log.d("**loginResult: ", loginResult.getSuccess().getEmail() +" "+
                            loginResult.getSuccess().getPassword());
                    Toast.makeText(getApplicationContext(), "Signin", Toast.LENGTH_SHORT).show();
                    signIn(loginResult);
                }
                loadingProgressBar.setVisibility(View.GONE);
                setResult(Activity.RESULT_OK);
            }
        });

        loginViewModel.getRegisterResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult registerResult) {
                if (registerResult == null) {
                    return;
                }

                if (registerResult.getError() != null) {
                    showLoginFailed(registerResult.getError());
                }
                if (registerResult.getSuccess() != null) {
                    Log.d("registerResult: ", registerResult.getSuccess().getEmail() +" "+
                            registerResult.getSuccess().getPassword());
                    Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
                    createAccount(registerResult);

                }
                loadingProgressBar.setVisibility(View.GONE);
                setResult(Activity.RESULT_OK);
            }

        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                loginViewModel.resetDataChanged(usernameEditText.getText().toString());
            }
        };
        TextWatcher afterTextChangedListener2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.registerDataChanged( passwordEditText.getText().toString(),passwordEditText2.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText2.addTextChangedListener(afterTextChangedListener2);
        //MIGHT NEED TO DELETE THIS LATER
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        floginButton = (LoginButton) findViewById(R.id.login_button);
        floginButton.setReadPermissions("email", "public_profile");
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        floginButton.registerCallback(callbackManager, new FacebookCallback<com.facebook.login.LoginResult>() {
                    @Override
                    public void onSuccess(com.facebook.login.LoginResult loginResult) {

                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //User Experience Things
                if(firstClick) {
                    firstClick =false;
                    usernameEditText.setVisibility(View.VISIBLE);
                    passwordEditText.setVisibility(View.VISIBLE);
                    backButton.setVisibility(View.VISIBLE);
                    registerButton.setVisibility(View.GONE);
                    forgotPassword.setVisibility(View.VISIBLE);
                    loginButton.setEnabled(false);
                }
                else {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //User Experience Things
                if(firstClick) {
                    firstClick =false;
                    usernameEditText.setVisibility(View.VISIBLE);
                    passwordEditText.setVisibility(View.VISIBLE);
                    passwordEditText2.setVisibility(View.VISIBLE);
                    backButton.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.GONE);
                    registerButton.setEnabled(false);
                }
                else{
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    loginViewModel.register(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //User Experience Things
                {
                    firstClick = true;
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                    passwordEditText2.setText("");
                    usernameEditText.setVisibility(View.GONE);
                    passwordEditText.setVisibility(View.GONE);
                    passwordEditText2.setVisibility(View.GONE);
                    forgotPassword.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);
                    registerButton.setVisibility(View.VISIBLE);
                    resetInfo.setVisibility(View.GONE);
                    loginButton.setEnabled(true);
                    registerButton.setEnabled(true);
                    backButton.setVisibility(View.GONE);
                    resetPassword.setVisibility(View.GONE);
                    resetPassword.setText(R.string.send);
                    resetInfo.setText(R.string.resetinfo_emaillink);
                }
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resetFirstClick)
                {
                    resetFirstClick = false;
                    resetInfo.setText(String.format("%s%s%s",
                            getString(R.string.resetinf_sent_to),
                            usernameEditText.getText().toString(),
                            getString(R.string.resetinf_sent_to_two)));
                  //  resetPasswordSendEmail(usernameEditText.getText().toString());
                    usernameEditText.setVisibility(View.GONE);
                    resetPassword.setText(R.string.resend);
                }else
                {
                    resetFirstClick = true;
                    resetInfo.setText(R.string.resetinfo_emaillink);
                    usernameEditText.setVisibility(View.VISIBLE);
                    usernameEditText.setText("");
                    resetPassword.setEnabled(false);
                    resetPassword.setText(R.string.send);
                }
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FORGOT CLICK", "Forgot Clicked");
                usernameEditText.setText("");
                passwordEditText.setVisibility(View.GONE);
                loginButton.setVisibility(View.GONE);
                resetPassword.setVisibility(View.VISIBLE);
                resetPassword.setEnabled(false);
                forgotPassword.setVisibility(View.GONE);
                resetInfo.setVisibility(View.VISIBLE);
            }
        });

    }

    public void resetPasswordSendEmail(String emailAddress)
    {
        mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(getApplicationContext(), "Email sent to "+ task.getResult().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void showLoginFailed(@StringRes Integer errorString) {
        passwordEditText.setError("Wrong Password Try Again");

        Toast.makeText(this, "LoginFailed", Toast.LENGTH_SHORT).show();

    }

    private void signIn(LoginResult loginResult) {
        Log.d(TAG, "signIn:" + loginResult.getSuccess().getEmail());

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(loginResult.getSuccess().getEmail(), loginResult.getSuccess().getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Log.d("LOGIN RESULT RESSULT", ""+loginResult.getError());
                            showLoginFailed(-1);
                        }
                    }


                });
        // [END sign_in_with_email]
    }
    private void createAccount(RegisterResult loginResult) {
        Log.d(TAG, "createAccount:" + loginResult.getSuccess().getEmail());

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(loginResult.getSuccess().getEmail(), loginResult.getSuccess().getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            User user1 = new User(user.getUid(), user.getEmail(),
                                    null, null,null);
                            fireBaseServices.addNewUser(user1);
                            updateUI(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void updateUI(String userId)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
           updateUI(currentUser.getUid());
        }
    }
}