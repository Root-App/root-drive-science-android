package com.example.drivescience;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.joinroot.roottriptracking.BuildConfig;
import com.joinroot.roottriptracking.RootTripTracking;
import com.joinroot.roottriptracking.environment.Environment;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCES_KEY = "Root-demo-preferences";
    private static final String ACTIVE_DRIVER_ID_PREFERENCE = "activeDriverId";

    public static final String CLIENT_ID = "d2ca8c3d33b7985c4b8d0fc8f";

    private ToggleButton clearOrRegisterDriver;
    private Switch tripTrackingActivation;
    private Button copyLog;
    private Button clearLog;

    private TextView activeDriverIdView;
    private TextView driverIdInput;
    private TextView tripTrackerVersion;

    private SharedPreferences sharedPreferences;

    private LogManager logManager;

    @Override
    protected void onStart() {
        super.onStart();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);

        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + this.getPackageName()));
        this.startActivityForResult(intent, 100, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clearOrRegisterDriver = findViewById(R.id.clearOrRegisterDriver);
        tripTrackingActivation = findViewById(R.id.tripTrackingActivation);
        copyLog = findViewById(R.id.copyLog);
        clearLog = findViewById(R.id.clearLog);

        activeDriverIdView = findViewById(R.id.activeDriverId);
        driverIdInput = findViewById(R.id.driverIdInput);
        tripTrackerVersion = findViewById(R.id.tripTrackerVersion);

        sharedPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        logManager = new LogManager(this, findViewById(R.id.eventLog));

        initializeTripTrackerAndSetButtonState();

        clearOrRegisterDriver.setOnClickListener(view -> setActiveDriverId());
        tripTrackingActivation.setOnCheckedChangeListener((view, isChecked) -> setTripTrackingActivation());

        copyLog.setOnClickListener(view -> logManager.copyLogToClipboard(this));
        clearLog.setOnClickListener(view -> logManager.clearLog());

        tripTrackerVersion.setText(String.format("Trip Tracker version: %s-%s", BuildConfig.FLAVOR, com.joinroot.roottriptracking.BuildConfig.SDK_VERSION));
    }

    private void initializeTripTrackerAndSetButtonState() {
        RootTripTracking.getInstance().initialize(this, CLIENT_ID, Environment.STAGING);

        Intent trackingIntent = new Intent(this, MainActivity.class);
        PendingIntent trackingActivity = PendingIntent.getActivity(this, 0, trackingIntent, 0);

        Notification trackingNotification = new Notification.Builder(this, "demo_app_channel")
                .setContentTitle("Drive Science crunching numbers!")
                .setSmallIcon(R.drawable.ic_driving_notification)
                .setContentIntent(trackingActivity)
                .build();
        RootTripTracking.getInstance().setTrackingNotification(this, trackingNotification, "demo_app_channel");

        TripLifecycleResponder tripLifecycleResponder = new TripLifecycleResponder(logManager);
        RootTripTracking.getInstance().setTripLifecycleHandler(tripLifecycleResponder);

        String activeDriverId = sharedPreferences.getString(ACTIVE_DRIVER_ID_PREFERENCE, "");

        updateDriverIdUi();

        if (activeDriverId != "") {
            clearOrRegisterDriver.setChecked(true);
            tripTrackingActivation.setVisibility(View.VISIBLE);
        }

        if (RootTripTracking.getInstance().configuredToAutoActivate()) {
            tripTrackingActivation.setChecked(true);
        }
    }

    private void setActiveDriverId() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        if (clearOrRegisterDriver.isChecked()) {
            String driverId = driverIdInput.getText().toString();

            RootTripTracking.getInstance().createDriver(driverId, null, null, new RootTripTracking.ICreateDriverRequestHandler() {
                @Override
                public void onSuccess(String driverId) {
                    sharedPreferences.edit().putString(ACTIVE_DRIVER_ID_PREFERENCE, driverId).commit();
                    updateDriverIdUi();

                    logManager.addToLog(String.format("Registered driver with id: %s", driverId));
                    tripTrackingActivation.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(String error) {
                    logManager.addToLog(String.format("Failed to register driver with error: %s", error));
                }
            });
        } else {
            RootTripTracking.getInstance().deactivate(getApplicationContext());
            sharedPreferences.edit().putString(ACTIVE_DRIVER_ID_PREFERENCE, "").commit();
            tripTrackingActivation.setChecked(false);
            tripTrackingActivation.setVisibility(View.INVISIBLE);

            updateDriverIdUi();
        }
    }

    private void setTripTrackingActivation() {
        if (tripTrackingActivation.isChecked()) {
            activateTripTracking();
        } else {
            deactivateTripTracking();
        }
    }

    private void activateTripTracking() {
        RootTripTracking.getInstance().activate(getApplicationContext(), sharedPreferences.getString(ACTIVE_DRIVER_ID_PREFERENCE, ""), new RootTripTracking.ITripTrackingActivateSuccessHandler() {
            @Override
            public void onSuccess() {
                logManager.addToLog("Trip Tracker activating");
                tripTrackingActivation.setChecked(true);
            }

            @Override
            public void onFailure(String error) {
                logManager.addToLog(String.format("Trip Tracker failed to activate with error: %s", error));
                tripTrackingActivation.setChecked(false);
            }
        });
    }

    private void deactivateTripTracking() {
        logManager.addToLog("Trip Tracker deactivating");
        RootTripTracking.getInstance().deactivate(getApplicationContext());
        tripTrackingActivation.setChecked(false);
    }

    private void updateDriverIdUi() {
        String driverId = sharedPreferences.getString(ACTIVE_DRIVER_ID_PREFERENCE, "");
        driverIdInput.setText(driverId);

        if (driverId != "") {
            activeDriverIdView.setText("Driver Registered\nDriver ID: " + driverId);
            driverIdInput.setEnabled(false);
        } else {
            activeDriverIdView.setText("No Driver Registered");
            driverIdInput.setEnabled(true);
        }
    }
}
