package org.richit.note_manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NoteBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals( "android.intent.action.BOOT_COMPLETED")){
            Intent serviceIntent = new Intent(context, NoteService.class);
            context.startService( serviceIntent );
        }else{
            Toast.makeText( context, "Note Manager run", Toast.LENGTH_SHORT ).show();
        }
    }
}
