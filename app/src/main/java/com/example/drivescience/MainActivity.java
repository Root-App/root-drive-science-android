package com.example.drivescience;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button newUser = findViewById(R.id.newUser);
        final Button existingUser = findViewById(R.id.existingUser);
        final TextView textView = findViewById(R.id.authToken);

        final UserManager userManager = new UserManager(getApplicationContext());
        if (userManager.hasRootDriverToken()) {
            existingUser.setVisibility(View.VISIBLE);
            textView.setText(userManager.getRootDriverToken());
            textView.setVisibility(View.VISIBLE);
        }

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userManager.setRootDriverToken(UUID.randomUUID().toString());
                textView.setText(userManager.getRootDriverToken());
                existingUser.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
            }
        });

        existingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(userManager.getRootDriverToken());
            }
        });
    }
}
