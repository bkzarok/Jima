package bkz.app.jima;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import bkz.app.jima.data.model.User;
import bkz.app.jima.services.FireBaseServices;

public class MainActivity extends AppCompatActivity {

    private User user;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);


        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("userId");
           // mainActivityViewModel.loadUserFromFireBase(value);
        }

        String name = mainActivityViewModel.getUser().getValue().getUserName();

        Log.d("MainActivityViewModel: ", name);
    }


    public void loadUserToViewModel()
    {
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("userId");
          //  mainActivityViewModel.loadUserFromFireBase(value);
            //The key argument here must match that used in the other activity
        }
    }

}