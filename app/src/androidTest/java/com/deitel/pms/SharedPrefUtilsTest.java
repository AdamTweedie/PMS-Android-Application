package com.deitel.pms;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.MasterKey;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

public class SharedPrefUtilsTest {

    Context appContext = InstrumentationRegistry
            .getInstrumentation().getTargetContext();
    SharedPrefUtils utils = new SharedPrefUtils();
    @Test
    public void getEncryptedPreferences() {
        assertThat(utils.getEncryptedPreferences
                        (utils.getMasterKey(appContext), appContext),
                instanceOf(SharedPreferences.class));
    }
    @Test
    public void getMasterKey() {
        assertThat(utils.getMasterKey(appContext),
                instanceOf(MasterKey.class));
    }
}
