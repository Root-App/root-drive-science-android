package com.example.drivescience;

import com.joinroot.roottriptracking.lifecycle.ITripTrackerLifecycleHandler;
import com.joinroot.roottriptracking.lifecycle.TripEvent;
import com.joinroot.roottriptracking.lifecycle.LifecycleEvent;

public class TripLifecycleResponder implements ITripTrackerLifecycleHandler {

    private LogManager logManager;

    TripLifecycleResponder(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public void onEvent(LifecycleEvent event) {
        switch (event.getName()) {
            case ACTIVATION:
                logManager.addToLog("Activated!");
                break;
            case TRIP_STARTED:
                logManager.addToLog(String.format("Trip %s started", ((TripEvent)event).getTrip().getIdentifier()));
                break;
            case TRIP_CANCELED:
                logManager.addToLog(String.format("Trip %s canceled", ((TripEvent)event).getTrip().getIdentifier()));
                break;
            case TRIP_ENDED:
                logManager.addToLog(String.format("Trip %s ended", ((TripEvent)event).getTrip().getIdentifier()));
                break;
            case DEACTIVATION:
                logManager.addToLog("Deactivated");
                break;
        }
    }
}
