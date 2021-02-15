package bkz.app.jima.data.model;

import androidx.annotation.Nullable;


public class User {
    String userId;
    String userName;
    String name;
    String accountId;
    Contact[] contacts;

    public User(String userId, String userName,@Nullable String name,
                String accountId,@Nullable Contact[] contacts)
    {
        this.userId = userId;
        this.userName = userName;
        this.name = name;
        this.accountId = accountId;
        this.contacts = contacts;
    }

    public String getUserId(){return  userId;}

    public String getUserName(){return  userName;}

    public String getName(){return name;}

    public  String getAccountId(){return accountId; }

    public Contact[] getContacts(){return contacts;}
}
