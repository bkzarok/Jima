package bkz.app.jima.main_ui.contacts;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bkz.app.jima.data.model.Contact;
import bkz.app.jima.main_ui.MainActivityViewModel;
import bkz.app.jima.R;
import bkz.app.jima.data.model.User;
import bkz.app.jima.main_ui.MyItemRecyclerViewAdapter;
import bkz.app.jima.data.dummy.DummyContent;



public class ContactsFragment extends Fragment {

    private ContactsViewModel homeViewModel;
    private MainActivityViewModel mainActivityViewModel;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_ContactsFragment = "ContactFragment ARGS";
    private static final String TAG_CONTACTS = "ContactsFragment";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<Contact> contacts;
    private RecyclerView recyclerView;
    private ContactsReclyclerViewAdapter contactsReclyclerViewAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private User user;
    private TextView contactsEditText;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ContactsFragment newInstance(int columnCount) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(ContactsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_contacts, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        recyclerView = (RecyclerView) root.findViewById(R.id.contacts_recyclerview);
        contactsEditText = (TextView) root.findViewById(R.id.contactTextView);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });


        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        contacts = new ArrayList<>();
       // contacts.add(new Contact("","","","","",""));

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), mColumnCount));
        }

        contactsReclyclerViewAdapter = new ContactsReclyclerViewAdapter(contacts);
        recyclerView.setAdapter(contactsReclyclerViewAdapter);


        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        mainActivityViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                contacts.addAll(user.getContacts().values());
                if(!contacts.isEmpty()){
                    contactsEditText.setVisibility(View.GONE);
                }
                contactsReclyclerViewAdapter.notifyDataSetChanged();
            }
        });


        // enable pull down to refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.contacts_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // do something
                mSwipeRefreshLayout.setRefreshing(true);
                myRef.child("users").child(mUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()!=null)
                        {
                            user = dataSnapshot.getValue(User.class);
                            contacts.clear();
                            contacts.addAll(user.getContacts().values());
                            if(!contacts.isEmpty()){
                                contactsEditText.setVisibility(View.GONE);
                            }
                            Log.d(TAG_CONTACTS, "Successfully get user"+dataSnapshot.getValue());
                            contactsReclyclerViewAdapter.notifyDataSetChanged();
                            // after refresh is done, remember to call the following code
                            stopRefreshing();
                        }
                        else
                        {
                            Log.d(TAG_CONTACTS, "Snapshot doesn't exist on succes is null");
                            stopRefreshing();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        stopRefreshing();
                        Log.d(TAG_CONTACTS, "ON failurelistener Snapshot doesn't exist");
                    }
                });



            }
        });

        return root;
    }


    public void stopRefreshing() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);  // This hides the spinner
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

