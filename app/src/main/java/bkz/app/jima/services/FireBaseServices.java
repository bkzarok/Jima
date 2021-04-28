package bkz.app.jima.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import bkz.app.jima.data.model.User;

public class FireBaseServices {

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    User user;
    private DatabaseReference mDatabase;
// ...

    public FireBaseServices()
    {
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
    }

    public void addNewUser(User user)
    {
       myRef.child("users").child(user.getUserId()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("USER ADD", "Successfully added user");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("USER ADD", "Failure to  add user");
            }
        });
    }

    public void updateUser(User user)
    {
        myRef.child("users").child(user.getUserId()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("USER UPDATED", "Successfully update user");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("USER UPDATED", "Failure to  update user");
                    }
                });
    }

    public User getUser(String userId)
    {
        myRef.child("users").child(userId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               user = snapshot.getValue(User.class);
                Log.d("USER GET USER", "Successfully get user");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("USER GET USER", "Failure getting user");
            }
        });

        return  user;
    }

}
