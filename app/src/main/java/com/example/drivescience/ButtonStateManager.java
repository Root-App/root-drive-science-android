package com.example.drivescience;

import android.widget.Button;

public class ButtonStateManager {
    private Button generateToken;
    private Button startTracking;
    private Button stopTracking;

    public ButtonStateManager(Button generateToken, Button startTracking, Button stopTracking) {
        this.generateToken = generateToken;
        this.startTracking = startTracking;
        this.stopTracking = stopTracking;
    }

    public void setButtonStateNoToken() {
        startTracking.setEnabled(false);
        stopTracking.setEnabled(false);
        generateToken.setEnabled(true);
    }

    public void setButtonStateHasToken() {
        startTracking.setEnabled(true);
        stopTracking.setEnabled(false);
        generateToken.setEnabled(true);
    }

    public void setButtonStateShouldBeActive() {
        startTracking.setEnabled(false);
        stopTracking.setEnabled(true);
        generateToken.setEnabled(false);
    }
}
