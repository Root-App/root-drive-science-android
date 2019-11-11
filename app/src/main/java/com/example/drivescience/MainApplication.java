package com.example.drivescience;

import android.app.Application;

import com.joinroot.roottriptracking.RootTripTracking;
import com.joinroot.roottriptracking.environment.Environment;

import static com.example.drivescience.MainActivity.CLIENT_ID;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RootTripTracking.getInstance().initialize(getApplicationContext(), CLIENT_ID, Environment.LOCAL);
    }
}
