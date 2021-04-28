package bkz.app.jima.main_ui.search_ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import bkz.app.jima.R;
import bkz.app.jima.data.model.User;
import bkz.app.jima.main_ui.contacts.ContactsReclyclerViewAdapter;

public class SearchableActivity extends AppCompatActivity {

    private static final String TAG = "SearchableActivity";
    private RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    TextView searchResultTextView;
    User user;
    List<User> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Get the intent, verify the action and get the query

        recyclerView = (RecyclerView) findViewById(R.id.search_recyclerview);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Please Wait");
        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        users = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        searchResultTextView = (TextView) findViewById(R.id.search_result_textView);

        progressDialog.show();
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            String query = intent.getStringExtra(SearchManager.QUERY);
          Query query1 =  myRef.child("users").orderByChild("userName").equalTo(query);

          query1.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Cannot directly map to User class so have to access sub children
                  Log.d("DATA SNa", "Datasnapshot" + snapshot.toString());
                  if(snapshot.getValue()!=null)
                  {
                      for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                          user = snapshot1.getValue(User.class);
                          users.add(user);
                      }
                      searchResultTextView.setText("Results found for "+query);
                      recyclerView.setAdapter(new SearchReclyclerViewAdapter(users));
                      progressDialog.dismiss();
                  }
                  else {
                      searchResultTextView.setText("No results found for "+query);
                      progressDialog.dismiss();
                  }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {
                  Log.d(TAG, "user is not found");
                  searchResultTextView.setText("No result found for "+query);
                  progressDialog.dismiss();
              }
          });
            Log.d(TAG, "the query is " + query);
        }
    }
}