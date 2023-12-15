package com;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import com.openclassrooms.realestatemanager.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class InternetAvailabilityTest {
    public Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    @Test
    public void testIsInternetAvailable() throws Exception  {
        setAirplaneMode(false);
        Thread.sleep(2000);

        boolean isInternetAvailable = Utils.isInternetAvailable(context);

        assertTrue(isInternetAvailable);
    }
    @Test
    public void testIsInternetNotAvailable() throws Exception  {
        setAirplaneMode(true);
        Thread.sleep(2000);

        boolean isInternetAvailable = Utils.isInternetAvailable(context);


        assertFalse(isInternetAvailable);
    }

    private void setAirplaneMode(boolean enable)
    {
        if ((enable ? 1 : 0) == Settings.System.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0))
        {
            return;
        }
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openQuickSettings();
        // Find the text of your language
        BySelector description = By.desc("Airplane mode");
        // Need to wait for the button, as the opening of quick settings is animated.
        device.wait(Until.hasObject(description), 500);
        device.findObject(description).click();
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

}

