package bkz.app.jima.main_ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import bkz.app.jima.R;
import bkz.app.jima.data.model.User;
import bkz.app.jima.main_ui.contacts.ContactsFragment;

public class MainActivity extends AppCompatActivity {

    private User user;
    private MainActivityViewModel mainActivityViewModel;
    private static final String TAG = "MainActivity";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Please Wait");

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        progressDialog.show();
        loadUserToViewModel();
    }

    public void navstuff()
    {

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard,
                R.id.navigation_notifications, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    public void loadUserToViewModel()
    {
            myRef.child("users").child(mUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null)
                    {
                        user = dataSnapshot.getValue(User.class);
                        mainActivityViewModel.setUser(user);
                        Log.d(TAG, "Successfully get user"+dataSnapshot.getValue());
                        navstuff();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        Log.d(TAG, "Snapshot doesn't exist on succes is null");
                        navstuff();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    navstuff();
                    progressDialog.dismiss();
                    Log.d(TAG, "ON failurelistener Snapshot doesn't exist");
                }
            });

            //mainActivityViewModel.loadUserFromFireBase(value);
            // The key argument here must match that used in the other activity

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search_button:
                onSearchRequested();
            case R.id.search_user_button:
                onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}