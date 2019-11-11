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

    public static final String CLIENT_ID = "a6d818d5-d6c0-40da-9bca-41d454f946c5";
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
            RootTripTracking.getInstance().setDriverToken(getApplicationContext(), userManager.getRootDriverToken());
            existingUser.setVisibility(View.VISIBLE);
            textView.setText(userManager.getRootDriverToken());
            textView.setVisibility(View.VISIBLE);
        }

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userManager.clearRootDriverToken();
                RootTripTracking.getInstance().getDriverToken(getApplicationContext(), new RootTripTracking.IDriverTokenRequestHandler() {
                    @Override
                    public void onSuccess(String token) {
                        userManager.setRootDriverToken(token);
                        textView.setText(userManager.getRootDriverToken());
                        existingUser.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure() {}
                });
            }
        });

        existingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RootTripTracking.getInstance().setDriverToken(getApplicationContext(), userManager.getRootDriverToken());
            }
        });
    }
}
