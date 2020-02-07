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

        buttonStateManager = new ButtonStateManager(startTracking, stopTracking);
        sharedPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        initializeTripTrackerAndSetButtonState();

        phoneInput.addTextChangedListener(MaskPhoneWatcher.getWatcher());
        phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (phoneInput.getText().toString().replaceAll("[^0-9]", "").length() >= 10) {
                    confirmNumber.setEnabled(true);
                } else {
                    confirmNumber.setEnabled(false);
                }
            }
        });

        confirmNumber.setOnClickListener(view -> setActivePhoneNumber());

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

        String activePhoneNumber = sharedPreferences.getString(ACTIVE_PHONE_PREFERENCE, "");
        String activeDriverId = sharedPreferences.getString(ACTIVE_DRIVER_ID_PREFERENCE, "");

        if (activeDriverId != "") {
            activeDriverIdView.setText("Active Driver ID: " + activeDriverId);

            activePhoneNumberView.setText("Active Phone Number: " + activePhoneNumber);

            buttonStateManager.setButtonStateCanStartTracking();
        } else {
            activeDriverIdView.setText("Active Driver ID: not set");
            activePhoneNumberView.setText("Active Phone Number: not set");
            buttonStateManager.setButtonStateCannotStartTracking();
        }

        if (RootTripTracking.getInstance().shouldReactivate()) {
            buttonStateManager.setButtonStateShouldBeTracking();
        }
    }

    private void setActivePhoneNumber() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        String phoneNumber = phoneInput.getText().toString();
        HashMap<String, String> driver = new HashMap<String, String>();
        driver.put("phone_number", phoneNumber);

        RootTripTracking.getInstance().createDriver(driver, new RootTripTracking.ICreateDriverRequestHandler() {
            @Override
            public void onSuccess(String driverId) {
                sharedPreferences.edit().putString(ACTIVE_DRIVER_ID_PREFERENCE, driverId).commit();
                sharedPreferences.edit().putString(ACTIVE_PHONE_PREFERENCE, phoneNumber).commit();

                activeDriverIdView.setText("Active Driver ID: " + driverId);
                activePhoneNumberView.setText("Active Phone Number: " + phoneNumber);
                phoneInput.setText("");
                buttonStateManager.setButtonStateCanStartTracking();
            }

            @Override
            public void onFailure() { }
        });
    }

    private void triggerStartTracking() {
        RootTripTracking.getInstance().activate(getApplicationContext(), sharedPreferences.getString(ACTIVE_DRIVER_ID_PREFERENCE, ""), success -> {
            if (success) {
                eventLog.setText(String.format("%sTrip Tracker successfully activated\n", eventLog.getText()));
                buttonStateManager.setButtonStateShouldBeTracking();
            } else {
                eventLog.setText(String.format("%sTrip Tracker failed to successfully activate\n", eventLog.getText()));
                buttonStateManager.setButtonStateCanStartTracking();
            }
        });
    }
}
