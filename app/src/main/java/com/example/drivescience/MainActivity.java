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
    private Button newUser;
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
        textView = findViewById(R.id.authToken);

        RootTripTracking.getInstance().initialize(this, CLIENT_ID);

        String token = RootTripTracking.getInstance().getCurrentAccessToken();
        if (token != null) {
            updateViews();
            RootTripTracking.getInstance().activate(this);
        } else {
            generateAccessToken();
        }

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateAccessToken();
            }
        });
    }

    public void generateAccessToken() {
        RootTripTracking.getInstance().generateAccessToken(new RootTripTracking.IDriverTokenRequestHandler() {
            @Override
            public void onSuccess(String newToken) {
                setAccessTokenAndActivate(newToken);
                updateViews();
            }

            @Override
            public void onFailure() {
                textView.setText("Could not acquire token.");
                textView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setAccessTokenAndActivate(String token) {
        RootTripTracking.getInstance().setAccessToken(token);
        RootTripTracking.getInstance().activate(this);
    }

    public void updateViews() {
        textView.setText(RootTripTracking.getInstance().getCurrentAccessToken());
        textView.setVisibility(View.VISIBLE);
    }
}
