package com.example.drivescience;

import android.content.Context;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class TripLifecycleResponderTest {
    private TripLifecycleResponder subject;

    private TextView eventLog;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        eventLog = new TextView(context);
        subject = new TripLifecycleResponder(eventLog);
    }

    @Test
    public void testOnTripStarted() {
        subject.onTripStarted("trip id");
        assertTrue(eventLog.getText().toString().contains("trip id started"));
    }

    @Test
    public void testOnTripEnded() {
        subject.onTripEnded("trip id");
        assertTrue(eventLog.getText().toString().contains("trip id ended"));
    }
}
