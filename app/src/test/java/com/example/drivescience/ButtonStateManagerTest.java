package com.example.drivescience;

import android.content.Context;
import android.widget.Button;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ButtonStateManagerTest {

    private ButtonStateManager subject;

    private Button generateToken;
    private Button startTracking;
    private Button stopTracking;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        generateToken = new Button(context);
        startTracking = new Button(context);
        stopTracking = new Button(context);
        subject = new ButtonStateManager(startTracking, stopTracking);
    }

    @Test
    public void testSetButtonStateCannotStartTracking() {
        subject.setButtonStateCannotStartTracking();
        assertFalse(startTracking.isEnabled());
        assertFalse(stopTracking.isEnabled());
    }

    @Test
    public void testSetButtonStateCanStartTracking() {
        subject.setButtonStateCanStartTracking();
        assertTrue(startTracking.isEnabled());
        assertFalse(stopTracking.isEnabled());
    }

    @Test
    public void testSetButtonStateShouldBeTracking() {
        subject.setButtonStateShouldBeTracking();
        assertFalse(startTracking.isEnabled());
        assertTrue(stopTracking.isEnabled());
    }
}
