package com.example.drivescience;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class SharedPreferencesManagerTest {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferencesManager subject;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        subject = new SharedPreferencesManager(context);
        sharedPreferences = context.getSharedPreferences("Root-demo-preferences", Context.MODE_PRIVATE);
    }

    @After
    public void tearDown() {
        sharedPreferences.edit().clear().commit();
    }

    @Test
    public void testSetActiveDriverIdPreference() {
        assertEquals(subject.getActiveDriverIdPreference(), "");

        String driverId = "test-id";
        subject.setActiveDriverIdPreference(driverId);
        assertEquals(sharedPreferences.getString("activeDriverId", null), driverId);
    }

    @Test
    public void testGetActiveDriverIdPreference() {
        String driverId = "test-id";
        sharedPreferences.edit().putString("activeDriverId", driverId).commit();
        assertEquals(subject.getActiveDriverIdPreference(), driverId);
    }
}
