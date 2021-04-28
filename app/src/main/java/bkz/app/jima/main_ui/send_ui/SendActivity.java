
package bkz.app.jima.main_ui.send_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import bkz.app.jima.R;
import bkz.app.jima.data.model.Contact;
import bkz.app.jima.data.model.User;

public class SendActivity extends AppCompatActivity {

    Contact contact;
    private static final String TAG = "SendActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        Intent intent = getIntent();

        contact = new Contact(intent.getStringExtra("userId"),null,null,
                intent.getStringExtra("userName"),intent.getStringExtra("name"),
                intent.getStringExtra("pictureUrl"));

        Log.d(TAG, ""+contact.getUserId()+ contact.getName()+contact.getUserName()+contact.getPictureUrl());
    }
}