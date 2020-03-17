package com.example.drivescience;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class UIStateManager {
    SharedPreferencesManager sharedPreferencesManager;
    private TextView activeDriverIdView;
    private TextView driverIdInput;
    private ToggleButton clearOrRegisterDriver;
    private Switch tripTrackingActivation;
    private Switch tripTrackingReactivate;

    public UIStateManager(
            SharedPreferencesManager sharedPreferencesManager,
            TextView activeDriverIdView,
            TextView driverIdInput,
            ToggleButton clearOrRegisterDriver,
            Switch tripTrackingActivation,
            Switch tripTrackingReactivate
    ) {
        this.sharedPreferencesManager = sharedPreferencesManager;
        this.activeDriverIdView = activeDriverIdView;
        this.driverIdInput = driverIdInput;
        this.clearOrRegisterDriver = clearOrRegisterDriver;
        this.tripTrackingActivation = tripTrackingActivation;
        this.tripTrackingReactivate = tripTrackingReactivate;
    }

    public void initialize(String driverId, boolean active, boolean reactivateOnStart) {
        updateDriverIdUi();

        if (driverId != "") {
            clearOrRegisterDriver.setChecked(true);
            tripTrackingActivation.setVisibility(View.VISIBLE);
        }

        if (active) {
            tripTrackingActivation.setChecked(true);
            tripTrackingReactivate.setVisibility(View.VISIBLE);

            if (reactivateOnStart) {
                tripTrackingReactivate.setChecked(true);
            }
        }
    }

    public void setActiveDriverIdView(String driverId) {
        if (driverId != "") {
            tripTrackingActivation.setVisibility(View.VISIBLE);
        } else {
            tripTrackingActivation.setChecked(false);
            tripTrackingActivation.setVisibility(View.INVISIBLE);
        }
        updateDriverIdUi();
    }

    public void setActivated(boolean activated) {
        if (activated) {
            tripTrackingActivation.setChecked(true);
            tripTrackingReactivate.setVisibility(View.VISIBLE);
        } else {
            tripTrackingActivation.setChecked(false);
            tripTrackingReactivate.setVisibility(View.INVISIBLE);
        }
        updateDriverIdUi();
    }

    public void setReactivateOnStart(boolean reactivateOnStart) {
        if (reactivateOnStart) {
            tripTrackingReactivate.setChecked(true);
        } else {
            tripTrackingReactivate.setChecked(false);
        }
    }

    private void updateDriverIdUi() {
        String driverId = sharedPreferencesManager.getActiveDriverIdPreference();
        driverIdInput.setText(driverId);

        if (driverId != "") {
            driverIdInput.setEnabled(false);
            activeDriverIdView.setText("Driver Registered\nDriver ID: " + driverId);
        } else {
            driverIdInput.setEnabled(true);
            activeDriverIdView.setText("No Driver Registered");
        }
    }
}
