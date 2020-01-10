package com.example.drivescience;

import android.widget.TextView;

import com.joinroot.roottriptracking.services.ITripLifecycleHandler;

public class TripLifecycleResponder implements ITripLifecycleHandler {

    private TextView eventLog;

    public TripLifecycleResponder(TextView log) {
        eventLog = log;
    }

    @Override
    public void onTripStarted(String tripId) {
        eventLog.setText(eventLog.getText() + "Trip " + tripId + " started\n");
    }

    @Override
    public void onTripEnded(String tripId) {
        eventLog.setText(eventLog.getText() + "Trip " + tripId + " ended\n");
    }
}
