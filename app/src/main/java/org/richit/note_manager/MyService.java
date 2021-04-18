package org.richit.note_manager;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public class MyService extends Service {
    private String TAG = this.getClass().getSimpleName();

    public MyService() {
        //
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d( TAG, "onBind: " );
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d( TAG, "onCreate: " );
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart( intent, startId );
        Log.d( TAG, "onStart: " );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d( TAG, "onStartCommand: " );
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d( TAG, "onDestroy: " );
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged( newConfig );
        Log.d( TAG, "onConfigurationChanged: " );
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d( TAG, "onLowMemory: " );
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory( level );
        Log.d( TAG, "onTrimMemory: " );
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d( TAG, "onUnbind: " );
        return super.onUnbind( intent );
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind( intent );
        Log.d( TAG, "onRebind: " );
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved( rootIntent );
        Log.d( TAG, "onTaskRemoved: " );
        try {
            startService( new Intent( this, this.getClass() ) );
        } catch (Exception e) {

        }
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump( fd, writer, args );
        Log.d( TAG, "dump: " );
    }
}
