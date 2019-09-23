package com.example.drivescience;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class UserTest {

    private User subject;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        subject = new User(context);
    }

    @Test
    public void hasRootDriverToken() {
        assertFalse(subject.hasRootDriverToken());

        subject.setRootDriverToken("a token");
        assertTrue(subject.hasRootDriverToken());
    }
}
