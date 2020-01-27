package com.example.drivescience;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.joinroot.roottriptracking.RootTripTracking;
import com.joinroot.roottriptracking.environment.Environment;

public class MainActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "t56482015d476dd7434f7da4b";

    private ButtonStateManager buttonStateManager;

    private Button generateToken;
    private Button startTracking;
    private Button stopTracking;
    private Button clearLog;
    private TextView authToken;
    private TextView eventLog;
    private TextView tripTrackerVersion;

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

        generateToken = findViewById(R.id.generateToken);
        startTracking = findViewById(R.id.startTracking);
        stopTracking = findViewById(R.id.stopTracking);
        clearLog = findViewById(R.id.clearLog);
        authToken = findViewById(R.id.authToken);
        eventLog = findViewById(R.id.eventLog);
        tripTrackerVersion = findViewById(R.id.tripTrackerVersion);

        buttonStateManager = new ButtonStateManager(generateToken, startTracking, stopTracking);

        RootTripTracking.getInstance().initialize(this, CLIENT_ID, Environment.STAGING);

        TripLifecycleResponder tripLifecycleResponder = new TripLifecycleResponder(eventLog);
        RootTripTracking.getInstance().setTripLifecycleHandler(tripLifecycleResponder);

        String token = RootTripTracking.getInstance().getCurrentAccessToken();
        if (token != null) {
            authToken.setText("Current token: " + RootTripTracking.getInstance().getCurrentAccessToken());
            authToken.setVisibility(View.VISIBLE);
            buttonStateManager.setButtonStateHasToken();
        } else {
            buttonStateManager.setButtonStateNoToken();
        }

        if (RootTripTracking.getInstance().shouldReactivate()) {
            buttonStateManager.setButtonStateShouldBeActive();
        }

        generateToken.setOnClickListener(view -> generateAccessToken());

        startTracking.setOnClickListener(view -> {
            RootTripTracking.getInstance().activate(getApplicationContext());
            buttonStateManager.setButtonStateShouldBeActive();
        });

        stopTracking.setOnClickListener(view -> {
            RootTripTracking.getInstance().deactivate(getApplicationContext());
            buttonStateManager.setButtonStateHasToken();
        });

        clearLog.setOnClickListener(view -> eventLog.setText(""));

        tripTrackerVersion.setText(String.format("Trip Tracker version: %s", com.joinroot.roottriptracking.BuildConfig.SDK_VERSION));
    }

    public void generateAccessToken() {
        authToken.setText("Requesting token...");
        RootTripTracking.getInstance().generateAccessToken(new RootTripTracking.IDriverTokenRequestHandler() {
            @Override
            public void onSuccess(String newToken) {
                RootTripTracking.getInstance().setAccessToken(newToken);
                authToken.setText("Current token: " + RootTripTracking.getInstance().getCurrentAccessToken());
                authToken.setVisibility(View.VISIBLE);
                buttonStateManager.setButtonStateHasToken();
            }

            @Override
            public void onFailure() {
                authToken.setText("Could not acquire token.");
                authToken.setVisibility(View.VISIBLE);
            }
        });
    }
}
