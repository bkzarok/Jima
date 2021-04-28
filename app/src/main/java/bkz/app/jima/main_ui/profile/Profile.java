package bkz.app.jima.main_ui.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import bkz.app.jima.main_ui.MainActivityViewModel;
import bkz.app.jima.R;
import bkz.app.jima.data.model.User;
import bkz.app.jima.login_ui.login.LoginActivity;

public class Profile extends Fragment {

    private ProfileViewModel mViewModel;

    private Button editProfileButtton;
    private Button signOutButton;
    private TextView profile_name;
    private TextView profile_email;
    private MainActivityViewModel mainActivityViewModel;
    private User user;

    public static Profile newInstance() {
        return new Profile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container, false);

        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);


        editProfileButtton = (Button) root.findViewById(R.id.profile_edit_button);
        profile_name = (TextView) root.findViewById(R.id.profile_name);
        profile_email = (TextView) root.findViewById(R.id.profile_email);
        signOutButton = (Button)     root.findViewById(R.id.profile_sign_out);
       // profile_email.setText(mainActivityViewModel.getUser().getValue().getEmail());
        editProfileButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogButtonClicked(root);
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        mainActivityViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user1) {
                user = user1;
                Log.d("Profile User:" +
                        ":", "#####****************################"+ user1.getEmail());
                profile_email.setText(user.getEmail());
            }
        });
        return  root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    public void showAlertDialogButtonClicked(View view) {
        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Profile Name");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(
                R.layout.dialog_profile_info, null);
        builder.setView(customLayout);
        // add a button

        builder.setPositiveButton(R.string.save_profile, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // sign in the user ...
                EditText dialogName = customLayout.findViewById(R.id.dialog_name);
                if(!dialogName.getText().toString().isEmpty()) {
                    profile_name.setText(dialogName.getText().toString());
                }
                else if(profile_name.getText().toString().equals(R.string.empty_string))
                {profile_name.setText(R.string.empty_string);}

                Log.d("Name is ", ""+dialogName.getText().toString());
            }})
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // create and show
        // the alert dialog
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivityViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user1) {
                user = user1;
                Log.d("Profile User:" +
                        ":", "#####****************################"+ user1.getEmail());
                profile_email.setText(user.getEmail());
            }
        });
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        updateUI();
    }

    public void updateUI() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        this.getActivity().finish();
    }

}