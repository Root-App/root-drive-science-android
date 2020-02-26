package com.example.drivescience;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.joinroot.roottriptracking.BuildConfig;
import com.joinroot.roottriptracking.RootTripTracking;
import com.joinroot.roottriptracking.environment.Environment;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCES_KEY = "Root-demo-preferences";
    private static final String ACTIVE_DRIVER_ID_PREFERENCE = "activeDriverId";

    public static final String CLIENT_ID = "my-client-id";

    private ButtonStateManager buttonStateManager;

    private Button confirmNumber;
    private Button startTracking;
    private Button stopTracking;
    private Button clearLog;

    private TextView activeDriverIdView;
    private TextView eventLog;
    private TextView driverIdInput;
    private TextView tripTrackerVersion;

    private SharedPreferences sharedPreferences;

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

        confirmNumber = findViewById(R.id.confirmNumber);
        startTracking = findViewById(R.id.startTracking);
        stopTracking = findViewById(R.id.stopTracking);
        clearLog = findViewById(R.id.clearLog);

        activeDriverIdView = findViewById(R.id.activeDriverId);
        eventLog = findViewById(R.id.eventLog);
        driverIdInput = findViewById(R.id.driverIdInput);
        tripTrackerVersion = findViewById(R.id.tripTrackerVersion);

        buttonStateManager = new ButtonStateManager(startTracking, stopTracking);
        sharedPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        initializeTripTrackerAndSetButtonState();

        confirmNumber.setOnClickListener(view -> setActiveDriverId());

        startTracking.setOnClickListener(view -> triggerStartTracking());

        stopTracking.setOnClickListener(view -> {
            RootTripTracking.getInstance().deactivate(getApplicationContext());
            buttonStateManager.setButtonStateCanStartTracking();
        });

        clearLog.setOnClickListener(view -> eventLog.setText(""));

        tripTrackerVersion.setText(String.format("Trip Tracker version: %s-%s", BuildConfig.FLAVOR, com.joinroot.roottriptracking.BuildConfig.SDK_VERSION));
    }

    private void initializeTripTrackerAndSetButtonState() {
        RootTripTracking.getInstance().initialize(this, CLIENT_ID, Environment.STAGING);

        TripLifecycleResponder tripLifecycleResponder = new TripLifecycleResponder(eventLog);
        RootTripTracking.getInstance().setTripLifecycleHandler(tripLifecycleResponder);

        String activeDriverId = sharedPreferences.getString(ACTIVE_DRIVER_ID_PREFERENCE, "");

        if (activeDriverId != "") {
            activeDriverIdView.setText("Active Driver ID: " + activeDriverId);

            driverIdInput.setText(activeDriverId);
            driverIdInput.setEnabled(false);

            confirmNumber.setText("Clear Registered Driver");

            buttonStateManager.setButtonStateCanStartTracking();
        } else {
            activeDriverIdView.setText("Active Driver ID: not set");
            buttonStateManager.setButtonStateCannotStartTracking();
        }

        if (RootTripTracking.getInstance().configuredToAutoActivate()) {
            buttonStateManager.setButtonStateShouldBeTracking();
        }
    }

    private void setActiveDriverId() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        String driverId = driverIdInput.getText().toString();

        RootTripTracking.getInstance().createDriver(driverId, null, null, new RootTripTracking.ICreateDriverRequestHandler() {
            @Override
            public void onSuccess(String driverId) {
                sharedPreferences.edit().putString(ACTIVE_DRIVER_ID_PREFERENCE, driverId).commit();

                activeDriverIdView.setText("Active Driver ID: " + driverId);
                buttonStateManager.setButtonStateCanStartTracking();
            }

            @Override
            public void onFailure(String error) { }
        });
    }

    private void triggerStartTracking() {
        RootTripTracking.getInstance().activate(getApplicationContext(), sharedPreferences.getString(ACTIVE_DRIVER_ID_PREFERENCE, ""), new RootTripTracking.ITripTrackingActivateSuccessHandler() {
            @Override
            public void onSuccess() {
                eventLog.setText(String.format("%sTrip Tracker successfully activated\n", eventLog.getText()));
                buttonStateManager.setButtonStateShouldBeTracking();
            }

            @Override
            public void onFailure(String error) {
                eventLog.setText(String.format("%sTrip Tracker failed to successfully activate with error: \n", eventLog.getText(), error));
                buttonStateManager.setButtonStateCanStartTracking();
            }
        });
    }
}
