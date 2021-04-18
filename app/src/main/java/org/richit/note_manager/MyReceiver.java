package org.richit.note_manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

    // public static boolean isRegistered = false;
    private static MyReceiver myBroadcastReceiver;

    public static MyReceiver getInstance() {
        if (myBroadcastReceiver == null)
            myBroadcastReceiver = new MyReceiver();
        return myBroadcastReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals( Constants.SEND_NOTIFICATION )) {

            Notifier.sendNoti( "Remember Note!!!", "Time is up!", context );
        }
    }
}