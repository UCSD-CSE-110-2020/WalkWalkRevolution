package edu.ucsd.cse110.walkwalkrevolution;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ServiceTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class StepCountUpdateServiceTest {

    @Rule
    public final ServiceTestRule serviceRule = new ServiceTestRule();

    private Context context;

    private boolean broadcastReceived;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            broadcastReceived = true;
        }
    };

    @Before
    public void setup() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Register broadcast receiver from step count updater
        context.registerReceiver(broadcastReceiver, new IntentFilter(StepCountUpdateService.BROADCAST_ACTION));
    }

    @Test
    public void testBroadcastFromService() throws TimeoutException, InterruptedException {
        // Start service
        Intent serviceIntent = new Intent(context, StepCountUpdateService.class);
        serviceRule.startService(serviceIntent);

        // Check if broadcast has been received (broadcast sent every second)
        Thread.sleep(1000);
        assertThat(broadcastReceived, is(true));
    }

    @Test
    public void testBindService() throws TimeoutException {
        // Start service
        Intent serviceIntent = new Intent(context, StepCountUpdateService.class);
        serviceRule.startService(serviceIntent);

        // Bind service
        IBinder binder = serviceRule.bindService(serviceIntent);
        assertThat(binder, is(notNullValue()));
        StepCountUpdateService service = ((StepCountUpdateService.LocalBinder) binder).getService();
        assertThat(service, is(notNullValue()));

        // Check service state
        assertThat(service.isRunning(), is(true));
        service.stop();
        assertThat(service.isRunning(), is(false));
    }
}
