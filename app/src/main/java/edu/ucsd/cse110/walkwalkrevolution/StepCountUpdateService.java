package edu.ucsd.cse110.walkwalkrevolution;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class StepCountUpdateService extends Service {

    public static final String BROADCAST_ACTION = "edu.ucsd.cse110";

    private final IBinder binder = new LocalBinder();
    private boolean isRunning;

    public StepCountUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class LocalBinder extends Binder {
        public StepCountUpdateService getService() {
            return StepCountUpdateService.this;
        }
    }

    // Exposed method to be able to stop service when service is bound
    // Service cannot be restarted, a new service has to be created
    public void stop() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread thread = new Thread(new MyThread(startId));
        thread.start();
        isRunning = true;
        return super.onStartCommand(intent, flags, startId);
    }

    final class MyThread implements Runnable {
        int startId;
        public MyThread(int startId) {
            this.startId = startId;
        }

        @Override
        public void run() {
            Intent broadcastIntent = new Intent(BROADCAST_ACTION);
            synchronized (this) {
                while (isRunning) {
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
}
