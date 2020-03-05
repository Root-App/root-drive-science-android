package com.example.drivescience;

import com.joinroot.roottriptracking.services.ITripLifecycleHandler;

public class TripLifecycleResponder implements ITripLifecycleHandler {

    private LogManager logManager;

    public TripLifecycleResponder(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public void onTripStarted(String tripId) {
        logManager.addToLog(String.format("Trip %s started", tripId));
    }

    @Override
    public void onTripEnded(String tripId) {
        logManager.addToLog(String.format("Trip %s ended", tripId));
    }
}
