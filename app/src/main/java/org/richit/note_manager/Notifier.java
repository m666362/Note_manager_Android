package org.richit.note_manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;


public class Notifier {
    private static final String TAG = "Notifier";
    public static Notification notification = null;
    static NotificationManager notificationManager;
    static Notification.Builder builder;
    static boolean notificationOn = false;

    static String NOTIFICATION_CHANNEL_ID = "Rayhan";

    public static void sendNoti(String title, String message, Context context) {
        if (notificationManager == null)
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        builder = new Notification.Builder(context);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 6684, notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_ID, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(false);
            notificationChannel.setVibrationPattern(new long[]{0L});
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID);
        }

        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setStyle(new Notification.BigTextStyle().bigText(message));
        }
        builder.setContentText(message);
        builder.setOngoing(false);
        builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setVibrate(new long[]{0L});

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Notifier.notification = builder.build();
        } else {
            Notifier.notification = builder.getNotification();
        }
        notificationManager.notify(1000, Notifier.notification);

        notificationOn = true;
    }

    public static void clearNoti(Context context) {
        if (notificationOn) {
            builder.setOngoing(false);
            builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setVibrate(new long[]{0L});

            if (notificationManager == null)
                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1000);

            notificationOn = false;
        }

    }
}
