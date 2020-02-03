package com.example.drivescience;

import android.widget.Button;

public class ButtonStateManager {
    private Button confirmNumber;
    private Button startTracking;
    private Button stopTracking;

    public ButtonStateManager(Button confirmNumber, Button startTracking, Button stopTracking) {
        this.confirmNumber = confirmNumber;
        this.startTracking = startTracking;
        this.stopTracking = stopTracking;
    }

    public void setButtonStateCannotStartTracking() {
        startTracking.setEnabled(false);
        stopTracking.setEnabled(false);
        confirmNumber.setEnabled(true);
    }

    public void setButtonStateCanStartTracking() {
        startTracking.setEnabled(true);
        stopTracking.setEnabled(false);
        confirmNumber.setEnabled(true);
    }

    public void setButtonStateShouldBeTracking() {
        startTracking.setEnabled(false);
        stopTracking.setEnabled(true);
        confirmNumber.setEnabled(false);
    }
}
