package com.example.drivescience;

import android.widget.Button;

public class ButtonStateManager {
    private Button startTracking;
    private Button stopTracking;

    public ButtonStateManager(Button startTracking, Button stopTracking) {
        this.startTracking = startTracking;
        this.stopTracking = stopTracking;
    }

    public void setButtonStateCannotStartTracking() {
        startTracking.setEnabled(false);
        stopTracking.setEnabled(false);
    }

    public void setButtonStateCanStartTracking() {
        startTracking.setEnabled(true);
        stopTracking.setEnabled(false);
    }

    public void setButtonStateShouldBeTracking() {
        startTracking.setEnabled(false);
        stopTracking.setEnabled(true);
    }
}
