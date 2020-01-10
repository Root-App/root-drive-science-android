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
        eventLog.setText(String.format("%sTrip %s started\n", eventLog.getText(), tripId));
    }

    @Override
    public void onTripEnded(String tripId) {
        eventLog.setText(String.format("%sTrip %s ended\n", eventLog.getText(), tripId));
    }
}
