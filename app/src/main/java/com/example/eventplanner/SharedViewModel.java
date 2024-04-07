/**
 * A ViewModel class for sharing data between fragments.
 */
package com.example.eventplanner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> eventUpdated = new MutableLiveData<>();
    /**
     * Sets the value of eventUpdated LiveData.
     *
     * @param isUpdated Boolean value indicating whether the event is updated.
     */
    public void setEventUpdated(boolean isUpdated) {
        eventUpdated.setValue(isUpdated);
    }
    /**
     * Retrieves the LiveData object containing the update status of the event.
     *
     * @return LiveData<Boolean> LiveData object containing the update status of the event.
     */
    public LiveData<Boolean> isEventUpdated() {
        return eventUpdated;
    }
}

