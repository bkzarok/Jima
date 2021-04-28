package bkz.app.jima.data.model;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class User {
    String userId;
    String email;
    String name;
    String userName;
    String pictureUrl;
    String accountId;
    HashMap<String,Contact> contacts;

    public User(String userId, @Nullable String accountId,@Nullable String email,
                @Nullable String userName,@Nullable String name,
                @Nullable  String pictureUrl,@Nullable HashMap<String,Contact> contacts)
    {

        this.userId = userId;
        this.accountId = accountId;
        this.userName = userName;
        this.email = email;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.contacts = contacts;
    }

    public User()
    {}
    public String getUserId(){return  userId;}

    public String getEmail(){return  email;}

    public String getName(){return name;}

    public String getPictureUrl(){return  pictureUrl;}

    public  String getAccountId(){return accountId; }

    public String getUserName(){return userName;}
    public HashMap<String,Contact> getContacts(){return contacts;}
}
