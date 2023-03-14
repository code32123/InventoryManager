package com.example.inventorymanager.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("No settings to change yet\n(Might come in a future update!)");
    }

    public LiveData<String> getText() {
        return mText;
    }
}