package org.richit.note_manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;
import java.util.Date;

public class GlobalMethods {

//    public static void registerReceiver(Context context) {
//        IntentFilter receiverFilter = new IntentFilter();
//
//        if (!MyReceiver.isRegistered) {
//            try {
//                context.registerReceiver( MyReceiver.getInstance(), receiverFilter );
//                MyReceiver.isRegistered = true;
//            } catch (Exception e) {
//                //
//            }
//        }
//    }

    public static void sendCustomBroadcastAt(Context context, Date timeToSendBroadcast, String action, int requestCode) {
        Calendar calendar = Calendar.getInstance();
        calendar.set( Calendar.HOUR_OF_DAY, timeToSendBroadcast.getHours() );
        calendar.set( Calendar.MINUTE, timeToSendBroadcast.getMinutes() );
        calendar.set( Calendar.SECOND, 0 );

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add( Calendar.DAY_OF_MONTH, 1 );
        }

        Intent intentAlarm = new Intent( context, MyReceiver.class );
        intentAlarm.setAction( action );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
        PendingIntent pendingIntent = PendingIntent.getBroadcast( context, requestCode, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent );
        } else {
            alarmManager.set( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent );
        }

    }


}
