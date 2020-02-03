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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.joinroot.roottriptracking.BuildConfig;
import com.joinroot.roottriptracking.RootTripTracking;
import com.joinroot.roottriptracking.environment.Environment;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCES_KEY = "Root-demo-preferences";
    private static final String ACTIVE_PHONE_PREFERENCE = "activePhoneNumber";
    private static final String ACTIVE_DRIVER_ID_PREFERENCE = "activeDriverId";

    public static final String CLIENT_ID = "d2ca8c3d33b7985c4b8d0fc8f";

    private ButtonStateManager buttonStateManager;

    private Button confirmNumber;
    private Button startTracking;
    private Button stopTracking;
    private Button clearLog;

    private TextView activeDriverIdView;
    private TextView activePhoneNumberView;
    private TextView eventLog;
    private TextView phoneInput;
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
        activePhoneNumberView = findViewById(R.id.activePhoneNumber);
        eventLog = findViewById(R.id.eventLog);
        phoneInput = findViewById(R.id.phoneInput);
        tripTrackerVersion = findViewById(R.id.tripTrackerVersion);

        buttonStateManager = new ButtonStateManager(confirmNumber, startTracking, stopTracking);
        sharedPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        RootTripTracking.getInstance().initialize(this, CLIENT_ID, Environment.STAGING);

        TripLifecycleResponder tripLifecycleResponder = new TripLifecycleResponder(eventLog);
        RootTripTracking.getInstance().setTripLifecycleHandler(tripLifecycleResponder);

        Boolean hasToken = RootTripTracking.getInstance().hasAccessToken();
        String activePhoneNumber = sharedPreferences.getString(ACTIVE_PHONE_PREFERENCE, "");
        String activeDriverId = sharedPreferences.getString(ACTIVE_DRIVER_ID_PREFERENCE, "");

        if (activeDriverId != "") {
            activeDriverIdView.setText("Active Driver ID: " + activeDriverId);
            activeDriverIdView.setVisibility(View.VISIBLE);

            activePhoneNumberView.setText("Active Phone Number: " + activePhoneNumber);
            activePhoneNumberView.setVisibility(View.VISIBLE);

            if (hasToken) {
                buttonStateManager.setButtonStateCanStartTracking();
            } else {
                buttonStateManager.setButtonStateCannotStartTracking();
            }
        } else {
            buttonStateManager.setButtonStateCannotStartTracking();
        }

        if (RootTripTracking.getInstance().shouldReactivate()) {
            buttonStateManager.setButtonStateShouldBeTracking();
        }

        confirmNumber.setOnClickListener(view -> setActivePhoneNumber());

        startTracking.setOnClickListener(view -> {
            RootTripTracking.getInstance().activate(getApplicationContext(), activeDriverId);
            buttonStateManager.setButtonStateShouldBeTracking();
        });

        stopTracking.setOnClickListener(view -> {
            RootTripTracking.getInstance().deactivate(getApplicationContext());
            buttonStateManager.setButtonStateCanStartTracking();
        });

        clearLog.setOnClickListener(view -> eventLog.setText(""));

        tripTrackerVersion.setText(String.format("Trip Tracker version: %s-%s", BuildConfig.FLAVOR, com.joinroot.roottriptracking.BuildConfig.SDK_VERSION));
    }

    private void setActivePhoneNumber() {
        String phoneNumber = phoneInput.getText().toString();
        HashMap<String, String> driver = new HashMap<String, String>();
        driver.put("phone_number", phoneNumber);

        RootTripTracking.getInstance().createDriver(driver, new RootTripTracking.ICreateDriverRequestHandler() {
            @Override
            public void onSuccess(String driverId) {
                activeDriverIdView.setText("Active Driver ID: " + driverId);
                activeDriverIdView.setVisibility(View.VISIBLE);
                activePhoneNumberView.setText("Active Phone Number: " + phoneNumber);
                activePhoneNumberView.setVisibility(View.VISIBLE);
                phoneInput.setText("");
            }

            @Override
            public void onFailure() {
                activePhoneNumberView.setText("");
                activeDriverIdView.setText("");
            }
        });
    }
}
