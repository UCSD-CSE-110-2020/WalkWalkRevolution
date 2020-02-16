package edu.ucsd.cse110.walkwalkrevolution;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class StepCountUpdateService extends IntentService {

    public static final String BROADCAST_ACTION = "edu.ucsd.cse110";

    public StepCountUpdateService() {
        super("StepCountUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent broadcastIntent = new Intent(BROADCAST_ACTION);
        synchronized (this) {
            while (true) {
                try {
                    sendBroadcast(broadcastIntent);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
