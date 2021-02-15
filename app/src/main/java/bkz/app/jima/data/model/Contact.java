package bkz.app.jima.data.model;

import androidx.annotation.Nullable;

public class Contact {
    String userId;
    String userName;
    String name;
    String accountId;
    public Contact(String userId, String userName,@Nullable String name,
                   String accountId)
    {
        this.userId = userId;
        this.userName = userName;
        this.name = name;
        this.accountId = accountId;
    }
    public String getUserId(){return  userId;}

    public String getUserName(){return  userName;}

    public String getName(){return name;}

    public  String getAccountId(){return accountId; }


}