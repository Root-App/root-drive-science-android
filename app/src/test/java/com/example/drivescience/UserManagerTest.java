package com.example.drivescience;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class UserManagerTest {

    private UserManager subject;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        subject = new UserManager(context);
    }

    @Test
    public void testRootHasDriverToken() {
        assertFalse(subject.hasRootDriverToken());

        subject.setRootDriverToken("a token");
        assertTrue(subject.hasRootDriverToken());
    }

    @Test
    public void testClearRootDriverToken() {
        subject.setRootDriverToken("original token");
        assertTrue(subject.hasRootDriverToken());

        subject.clearRootDriverToken();
        assertFalse(subject.hasRootDriverToken());

    }
}