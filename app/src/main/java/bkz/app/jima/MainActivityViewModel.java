package bkz.app.jima;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import bkz.app.jima.data.model.User;
import bkz.app.jima.services.FireBaseServices;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<User> mutableLiveData;
    private FireBaseServices fireBaseServices;

    public MainActivityViewModel()
    {
        mutableLiveData = new MutableLiveData<>();
        fireBaseServices = new FireBaseServices();
    }

    public void setUser(User user)
    {
        this.mutableLiveData.setValue(user);
    }

    public LiveData<User> getUser()
    {
        return  mutableLiveData;
    }

    public void saveUserToFireBase()
    {
        try {
            fireBaseServices.updateUser(mutableLiveData.getValue());
        }
        catch (Exception e)
        { Log.e("saveUserToFireBase: ", e.getMessage());}
    }
 }