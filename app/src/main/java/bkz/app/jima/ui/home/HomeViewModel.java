package bkz.app.jima.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }
    public void setmText(String text)
    {
        this.mText.setValue(text);
    }

    public LiveData<String> getText() {
        return mText;
    }
}