package com.example.drivescience;

import android.content.Context;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.test.core.app.ApplicationProvider;

import com.joinroot.roottriptracking.lifecycle.LifecycleEvent;
import com.joinroot.roottriptracking.lifecycle.TripEvent;
import com.joinroot.roottriptracking.lifecycle.models.TripInformation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class TripLifecycleResponderTest {
    private TripLifecycleResponder subject;

    private TextView eventLog;
    private TripInformation exampleTrip;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        eventLog = new TextView(context);

        LogManager log = new LogManager(context, eventLog);
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        UIStateManager uiState = new UIStateManager(
                sharedPreferencesManager,
                new TextView(context),
                new TextView(context),
                new ToggleButton(context),
                new Switch(context),
                new Switch(context)
        );

        subject = new TripLifecycleResponder(log, uiState);

        exampleTrip = new TripInformation("some-identifier");
    }

    @Test
    public void testOnEvent_Activation() {
        LifecycleEvent event = new LifecycleEvent(LifecycleEvent.Name.ACTIVATION);
        subject.onEvent(event);
        assertTrue(eventLog.getText().toString().contains("Activated!"));
    }

    @Test
    public void testOnEvent_Deactivation() {
        LifecycleEvent event = new LifecycleEvent(LifecycleEvent.Name.DEACTIVATION);
        subject.onEvent(event);
        assertTrue(eventLog.getText().toString().contains("Deactivated"));
    }

    @Test
    public void testOnEvent_TripStarted() {
        LifecycleEvent event = new TripEvent(LifecycleEvent.Name.TRIP_STARTED, exampleTrip);
        subject.onEvent(event);
        assertTrue(eventLog.getText().toString().contains("Trip some-identifier started"));
    }

    @Test
    public void testOnEvent_TripCanceled() {
        LifecycleEvent event = new TripEvent(LifecycleEvent.Name.TRIP_CANCELED, exampleTrip);
        subject.onEvent(event);
        assertTrue(eventLog.getText().toString().contains("Trip some-identifier canceled"));
    }

    @Test
    public void testOnEvent_TripEnded() {
        LifecycleEvent event = new TripEvent(LifecycleEvent.Name.TRIP_ENDED, exampleTrip);
        subject.onEvent(event);
        assertTrue(eventLog.getText().toString().contains("Trip some-identifier ended"));
    }
}
