package com.example.drivescience;

import android.content.Context;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class UIStateManagerTest {
    private TextView activeDriverIdView;
    private TextView driverInputView;
    private ToggleButton clearOrRegisterDriverButton;
    private Switch activateSwitch;
    private Switch reactivateOnStartSwitch;

    private UIStateManager subject;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();

        activeDriverIdView = new TextView(context);
        driverInputView = new TextView(context);
        clearOrRegisterDriverButton = new ToggleButton(context);
        activateSwitch = new Switch(context);
        reactivateOnStartSwitch = new Switch(context);

        activateSwitch.setVisibility(View.INVISIBLE);
        reactivateOnStartSwitch.setVisibility(View.INVISIBLE);

        subject = new UIStateManager(
                new SharedPreferencesManager(context),
                activeDriverIdView,
                driverInputView,
                clearOrRegisterDriverButton,
                activateSwitch,
                reactivateOnStartSwitch
        );
    }

    @Test
    public void testInitializeWithDriverIdAndActiveAndReactivating() {
        subject.initialize("test-id", true, true);

        assertTrue(clearOrRegisterDriverButton.isChecked());
        assertTrue(activateSwitch.getVisibility() == View.VISIBLE);
        assertTrue(activateSwitch.isChecked());
        assertTrue(reactivateOnStartSwitch.getVisibility() == View.VISIBLE);
        assertTrue(reactivateOnStartSwitch.isChecked());
    }

    @Test
    public void testInitializeWithDriverIdAndActiveAndNotReactivating() {
        subject.initialize("test-id", true, false);

        assertTrue(clearOrRegisterDriverButton.isChecked());
        assertTrue(activateSwitch.getVisibility() == View.VISIBLE);
        assertTrue(activateSwitch.isChecked());
        assertTrue(reactivateOnStartSwitch.getVisibility() == View.VISIBLE);
        assertFalse(reactivateOnStartSwitch.isChecked());
    }

    @Test
    public void testInitializeWithDriverIdAndNotActive() {
        subject.initialize("test-id", false, false);

        assertTrue(clearOrRegisterDriverButton.isChecked());
        assertTrue(activateSwitch.getVisibility() == View.VISIBLE);
        assertFalse(activateSwitch.isChecked());
        assertTrue(reactivateOnStartSwitch.getVisibility() == View.INVISIBLE);
        assertFalse(reactivateOnStartSwitch.isChecked());
    }

    @Test
    public void testInitializeWithoutDriverId() {
        subject.initialize("", false, false);

        assertFalse(clearOrRegisterDriverButton.isChecked());
        assertTrue(activateSwitch.getVisibility() == View.INVISIBLE);
        assertFalse(activateSwitch.isChecked());
        assertTrue(reactivateOnStartSwitch.getVisibility() == View.INVISIBLE);
        assertFalse(reactivateOnStartSwitch.isChecked());
    }

    @Test
    public void testSetActiveDriverIdViewWithDriverId() {
        subject.setActiveDriverIdView("test-id");

        assertTrue(activateSwitch.getVisibility() == View.VISIBLE);
    }

    @Test
    public void testSetActiveDriverIdViewWithoutDriverId() {
        subject.setActiveDriverIdView("");

        assertFalse(activateSwitch.isChecked());
        assertTrue(activateSwitch.getVisibility() == View.INVISIBLE);
    }

    @Test
    public void testSetActivatedToTrue() {
        subject.setActivated(true);

        assertTrue(activateSwitch.isChecked());
        assertTrue(reactivateOnStartSwitch.getVisibility() == View.VISIBLE);
    }

    @Test
    public void testSetActivatedToFalse() {
        subject.setActivated(false);

        assertFalse(activateSwitch.isChecked());
        assertTrue(reactivateOnStartSwitch.getVisibility() == View.INVISIBLE);
    }

    @Test
    public void testSetReactivateOnStartToTrue() {
        subject.setReactivateOnStart(true);

        assertTrue(reactivateOnStartSwitch.isChecked());
    }

    @Test
    public void testSetReactivateOnStartToFalse() {
        subject.setReactivateOnStart(false);

        assertFalse(reactivateOnStartSwitch.isChecked());
    }
}
