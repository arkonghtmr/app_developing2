package ru.mirea.fedorov.fragmentmanagerapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShareViewModel extends ViewModel {
    private final MutableLiveData<Country> selectedItem = new MutableLiveData<>();

    public void selectItem(Country item) {
        selectedItem.setValue(item);
    }

    public LiveData<Country> getSelectedItem() {
        return selectedItem;
    }
}
