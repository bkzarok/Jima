package bkz.app.jima.main_ui.contacts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContactsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ContactsViewModel() {
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