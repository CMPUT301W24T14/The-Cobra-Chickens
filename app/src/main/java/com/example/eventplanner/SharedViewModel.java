package com.example.eventplanner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> eventUpdated = new MutableLiveData<>();

    public void setEventUpdated(boolean isUpdated) {
        eventUpdated.setValue(isUpdated);
    }

    public LiveData<Boolean> isEventUpdated() {
        return eventUpdated;
    }
}

