package com.example.drivescience;

import com.joinroot.roottriptracking.lifecycle.ITripTrackerLifecycleHandler;
import com.joinroot.roottriptracking.lifecycle.TripEvent;
import com.joinroot.roottriptracking.lifecycle.LifecycleEvent;

import static com.joinroot.roottriptracking.lifecycle.LifecycleEvent.Name.ACTIVATION;
import static com.joinroot.roottriptracking.lifecycle.LifecycleEvent.Name.DEACTIVATION;
import static com.joinroot.roottriptracking.lifecycle.LifecycleEvent.Name.TRIP_CANCELED;
import static com.joinroot.roottriptracking.lifecycle.LifecycleEvent.Name.TRIP_ENDED;
import static com.joinroot.roottriptracking.lifecycle.LifecycleEvent.Name.TRIP_STARTED;

public class TripLifecycleResponder implements ITripTrackerLifecycleHandler {

    private LogManager logManager;

    public TripLifecycleResponder(LogManager logManager) {
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
