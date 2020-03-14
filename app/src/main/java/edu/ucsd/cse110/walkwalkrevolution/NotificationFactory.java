package edu.ucsd.cse110.walkwalkrevolution;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;


// manage notifications
public class NotificationFactory {
    NotificationCompat.Builder builder;
    String channelId;

    NotificationFactory(Context context, String channelId) {
        this.channelId = channelId;
        createNotificationChannel(channelId, context);
    }

    public void createNotification(Context context, int icon, CharSequence title, String text, int notificationId) {
        // Create intent for notification tap-action
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        builder = new NotificationCompat.Builder(context, channelId)
                //.setSmallIcon(icon) off for now
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set Intent on tap
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel(String channelId, Context context) {
        // Create notification channel in only API 26+ because they support notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "WWR Notification";
            String description = "WWR Notification Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
