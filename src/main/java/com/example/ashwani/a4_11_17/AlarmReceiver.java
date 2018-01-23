package com.example.ashwani.a4_11_17;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

/**
 * Created by ashwani on 22-11-2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    int notifyId =1;

    @Override
    public void onReceive( Context context, Intent intent ) {

        NotificationCompat.Builder notify  = new NotificationCompat.Builder(context);
        notify.setSmallIcon(R.mipmap.ic_launcher_note)
                .setContentTitle("Reminder for TASK")
                .setContentText("TASK not done yet")
                .setDefaults(Notification.DEFAULT_SOUND);
        Intent intent1 = new Intent(context , MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, 0 );

        notify.setContentIntent(pendingIntent);

        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyId, notify.build());
        Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();


    }
}
