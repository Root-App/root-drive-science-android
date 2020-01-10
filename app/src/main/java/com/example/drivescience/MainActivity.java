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

public class MainActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "t56482015d476dd7434f7da4b";

    private ButtonStateManager buttonStateManager;

    private Button generateToken;
    private Button startTracking;
    private Button stopTracking;
    private TextView textView;

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
        textView = findViewById(R.id.authToken);

        buttonStateManager = new ButtonStateManager(generateToken, startTracking, stopTracking);

        RootTripTracking.getInstance().initialize(this, CLIENT_ID);

        String token = RootTripTracking.getInstance().getCurrentAccessToken();
        if (token != null) {
            textView.setText("Current token: " + RootTripTracking.getInstance().getCurrentAccessToken());
            textView.setVisibility(View.VISIBLE);
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
    }

    public void generateAccessToken() {
        RootTripTracking.getInstance().generateAccessToken(new RootTripTracking.IDriverTokenRequestHandler() {
            @Override
            public void onSuccess(String newToken) {
                RootTripTracking.getInstance().setAccessToken(newToken);
                textView.setText("Current token: " + RootTripTracking.getInstance().getCurrentAccessToken());
                textView.setVisibility(View.VISIBLE);
                buttonStateManager.setButtonStateHasToken();
            }

            @Override
            public void onFailure() {
                textView.setText("Could not acquire token.");
                textView.setVisibility(View.VISIBLE);
            }
        });
    }
}
