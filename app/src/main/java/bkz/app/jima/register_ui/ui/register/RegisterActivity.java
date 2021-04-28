package bkz.app.jima.register_ui.ui.register;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.Selection;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.regex.Pattern;

import bkz.app.jima.R;
import bkz.app.jima.data.model.Contact;
import bkz.app.jima.data.model.User;
import bkz.app.jima.main_ui.MainActivity;
import bkz.app.jima.services.FireBaseServices;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG_REGISTER = "Register Activity" ;
    private RegisterViewModel registerViewModel;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    private FireBaseServices fireBaseServices;
    FirebaseUser user;
    ProgressBar loadingProgressBar;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText userNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText rePasswordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);

        firstNameEditText = findViewById(R.id.editTextRegisteFirstName);
        lastNameEditText = findViewById(R.id.editTextRegisterLastName);
        userNameEditText = findViewById(R.id.userNameRegisterEditText);
        emailEditText = findViewById(R.id.emailRegisterEditText);
        passwordEditText = findViewById(R.id.passwordRegisterEditText);
        rePasswordEditText = findViewById(R.id.rePasswordRegisterEditText);
        final Button registerButton = findViewById(R.id.register2);
        loadingProgressBar = findViewById(R.id.loading);
        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        fireBaseServices = new FireBaseServices();
        user = mAuth.getCurrentUser();

        if(user!=null)
        {
            String[] displayName = user.getDisplayName().split(" ");
            firstNameEditText.setText(displayName[0]);
            lastNameEditText.setText(displayName[1]);
            emailEditText.setText("dummyemail@email.com");
            passwordEditText.setText("dummypassword");
            rePasswordEditText.setText("dummypassword");
        }
        else {
            emailEditText.setVisibility(View.VISIBLE);
            passwordEditText.setVisibility(View.VISIBLE);
            rePasswordEditText.setVisibility(View.VISIBLE);
        }

        registerViewModel.getregisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                Log.d(TAG_REGISTER, "Formstatechanged");
                if (registerFormState == null) {
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());

                if (registerFormState.getFirstNameError() != null) {
                    firstNameEditText.setError(getString(registerFormState.getFirstNameError()));
                }
                if (registerFormState.getLastNameError() != null) {
                    lastNameEditText.setError(getString(registerFormState.getLastNameError()));
                }
                if (registerFormState.getUserNameError() != null) {
                    userNameEditText.setError(getString(registerFormState.getUserNameError()));
                }
                if (registerFormState.getEmailError() != null) {
                    emailEditText.setError(getString(registerFormState.getEmailError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
                if (registerFormState.getRePasswordError() != null) {
                    rePasswordEditText.setError(getString(registerFormState.getRePasswordError()));
                }
            }
        });
        registerViewModel.getregisterResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult registerResult) {
                if (registerResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (registerResult.getError() != null) {
                    showregisterFailed(registerResult.getError());
                }
                if (registerResult.getSuccess() != null) {
                    updateUiWithUser(registerResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy register activity once successful
                finish();
            }
        });

        TextWatcher userNameTextChangeListener = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(Pattern.matches("[@]{1}[a-zA-Z0-9_-]{1,9}", s.toString())) {
                    Log.d(TAG_REGISTER, "pattern doesnt contain");
                    myRef.child("users").orderByChild("userName").equalTo(s.toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Log.d("USER GET USER", "Success getting user exist");
                                        registerButton.setEnabled(false);
                                        userNameEditText.setError("UserName Already Exist");

                                    } else {
                                        Log.d("USER GET USER", "Failure getting user doesn't exist");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("USER GET USER", "Failure getting user");
                                    registerButton.setEnabled(true);
                                }
                            });
                }else
                {
                    Log.d(TAG_REGISTER, "pattern does contain");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("@")){
                    userNameEditText.setText("@"+s.toString());
                    Selection.setSelection(userNameEditText.getText(), userNameEditText.getText().length());

                }
            }
        };
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
                registerViewModel.registerDataChanged(firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(), userNameEditText.getText().toString(),
                        emailEditText.getText().toString(), passwordEditText.getText().toString(),
                        rePasswordEditText.getText().toString());

            }
        };
        firstNameEditText.addTextChangedListener(afterTextChangedListener);
        lastNameEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        userNameEditText.addTextChangedListener(afterTextChangedListener);
        userNameEditText.addTextChangedListener(userNameTextChangeListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        rePasswordEditText.addTextChangedListener(afterTextChangedListener);
        rePasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.d(TAG_REGISTER, "repassword edittext entered");
                    //Register
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                //Register
                if(user!=null)
                {
                    HashMap<String, Contact> contacts = new HashMap<>();
                    contacts.put("DUMMY123UID",new Contact("DUMMY123UID","DUMMY123ACCID",
                            "dummymail@mail.com", "@username1","dummyFirst dummyLast","https://graph.facebook.com/1803106206533156/picture"));
                    contacts.put("DUMMY1234UID",new Contact("DUMMY1234UID","DUMMY123ACCID",
                            "dummymail@mail.com", "username2","dummyFirst2 dummyLast2","https://graph.facebook.com/1803106206533156/picture"));
                    User user1 = new User(user.getUid(), "DUMMY123ACCID", user.getEmail(),
                            userNameEditText.getText().toString(),user.getDisplayName(),user.getPhotoUrl().toString(),contacts);

                    //ADD User info
                    myRef.child("users").child(user.getUid()).setValue(user1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("USER ADD", "Successfully added user");
                                    startMainActivityUI();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("USER ADD", "Failure to  add user");
                                }
                            });

                }
                Log.d(TAG_REGISTER, "Register Button Clicked");
            }
        });
    }

   private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_REGISTER, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            HashMap<String, Contact> contacts = new HashMap<>();
                            contacts.put("DUMMY123UID",new Contact("DUMMY123UID","DUMMY123ACCID",
                                    "dummymail@mail.com", "@username1","dummyFirst dummyLast","https://graph.facebook.com/1803106206533156/picture"));
                            contacts.put("DUMMY1234UID",new Contact("DUMMY1234UID","DUMMY123ACCID",
                                    "dummymail@mail.com", "username2","dummyFirst2 dummyLast2","https://graph.facebook.com/1803106206533156/picture"));
                            User user1 = new User(user.getUid(), "DUMMY123ACCID", user.getEmail(),
                                    userNameEditText.getText().toString(),firstNameEditText.getText().toString()+" "+
                                    lastNameEditText.getText().toString(),user.getPhotoUrl().toString(),contacts);


                            //ADD User info
                            myRef.child("users").child(user.getUid()).setValue(user1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("USER ADD", "Successfully added user");
                                            startMainActivityUI();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("USER ADD", "Failure to  add user");
                                        }
                                    });



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_REGISTER, "createUserWithEmail:failure", task.getException());

                            Toast.makeText(RegisterActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Failure Failure", "Failure Failure in createAccount in LoginActivity");
            }
        });
        // [END create_user_with_email]
    }


    public void startMainActivityUI()
    {
        Intent intent = new  Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void updateUiWithUser(RegisterInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showregisterFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}