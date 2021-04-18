package org.richit.note_manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

            AndroidNetworking
                    .get( Constants.url )
                    .setTag( "test" )
                    .setPriority( Priority.LOW )
                    .build()
                    .getAsJSONArray( new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for(int n = response.length()-1; n < response.length(); n++)
                            {
                                try {
                                    JSONObject object = response.getJSONObject(n);
                                    Note note = new Gson().fromJson( String.valueOf( object ), Note.class );
                                    Notifier.sendNoti( "Note: "+note.title, note.description+" "+note.alarm, context );
                                } catch (JSONException e) {
                                    Notifier.sendNoti( "Remember Note!!!", "Time is up!", context );
                                }
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            Notifier.sendNoti( "Remember Note!!!", "Time is up!", context );
                        }
                    } );
        }
    }
}