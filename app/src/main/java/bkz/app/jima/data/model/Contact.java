package bkz.app.jima.data.model;

import androidx.annotation.Nullable;

import java.util.Map;

public class Contact {
    String userId;
    String userName;
    String email;
    String pictureUrl;
    String name;
    String accountId;
    public Contact(String userId,@Nullable String accountId,  @Nullable String email,
                   @Nullable String userName,@Nullable String name,@Nullable  String pictureUrl)
    {
        this.userId = userId;
        this.accountId = accountId;
        this.email = email;
        this.userName = userName;
        this.name = name;
        this.pictureUrl = pictureUrl;

    }
    public Contact()
    {}
    public String getUserId(){return  userId;}

    public String getUserName(){return  userName;}

    public String getName(){return name;}

    public  String getAccountId(){return accountId; }

    public String getPictureUrl() {
        return pictureUrl;
    }
}