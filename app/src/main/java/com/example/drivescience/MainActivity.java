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
import com.joinroot.roottriptracking.environment.IDriverTokenHandler;

public class MainActivity extends AppCompatActivity {

    private UserManager userManager;
    private Button newUser;
    private Button existingUser;
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

        newUser = findViewById(R.id.newUser);
        existingUser = findViewById(R.id.existingUser);
        textView = findViewById(R.id.authToken);

        userManager = new UserManager(getApplicationContext());
        if (userManager.hasRootDriverToken()) {
            existingUser.setVisibility(View.VISIBLE);
            textView.setText(userManager.getRootDriverToken());
            textView.setVisibility(View.VISIBLE);
        }

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userManager.clearRootDriverToken();
                initializeTripTracking();
            }
        });

        existingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeTripTracking();
            }
        });
    }

    void initializeTripTracking() {
        RootTripTracking.getInstance().initialize(getApplicationContext(), new IDriverTokenHandler() {
            @Override
            public String getClientId() {
                return "c2d32e9c-48d9-40b6-9eea-0b507775e443";
            }

            @Override
            public boolean hasDriverToken() {
                return userManager.hasRootDriverToken();
            }

            @Override
            public String getDriverToken() {
                return userManager.getRootDriverToken();
            }

            @Override
            public void setDriverToken(String token) {
                userManager.setRootDriverToken(token);
                textView.setText(userManager.getRootDriverToken());
                existingUser.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure() {

            }
        }, Environment.LOCAL);

    }
}
