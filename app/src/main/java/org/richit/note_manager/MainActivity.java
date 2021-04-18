package org.richit.note_manager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.richit.note_manager.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Calendar;

// http://192.168.0.151:4000/users
// https://restcountries.eu/rest/v2/name/bangladesh

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = this.getClass().getSimpleName();
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    ArrayList<Note> notes = new ArrayList<>();
    private int year, month, day, hour, minute;
    EditText note_title_Et;
    String title;
    String date;
    String time;
    ActivityMainBinding binding;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        initObject();
        getDataFromOnline();
        setInitialRequirement();
        alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE);

        //
        startService(new Intent(MainActivity.this, MyService.class));


    }

    private void setInitialRequirement() {
        binding.setAlarm.setOnClickListener( this );
        binding.cancelAlarm.setOnClickListener( this );
        binding.fab.setOnClickListener( this );
    }

    private void initObject() {
        AndroidNetworking.initialize( getApplicationContext() );
        binding = DataBindingUtil.setContentView( this, R.layout.activity_main );
        noteAdapter = new NoteAdapter( this, notes );
        binding.noteRecyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        binding.noteRecyclerView.setAdapter( noteAdapter );
    }

    private void getDataFromOnline() {
        notes.clear();
        AndroidNetworking
                .get( "https://note-manager-parkingkoi.herokuapp.com/notes/" )
                .setTag( "test" )
                .setPriority( Priority.LOW )
                .build()
                .getAsJSONArray( new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d( TAG, "onResponse: "+ response);
                        for(int n = 0; n < response.length(); n++)
                        {
                            try {
                                JSONObject object = response.getJSONObject(n);
                                Note note = new Gson().fromJson( String.valueOf( object ), Note.class );
                                addData( note );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d( TAG, "onError: " );
                        Toast.makeText( MainActivity.this, "Error", Toast.LENGTH_SHORT ).show();
                    }
                } );
    }

    private void setNote() {
        View view = LayoutInflater.from( MainActivity.this ).inflate( R.layout.dialog_signal, null );
        new AlertDialog
                .Builder( MainActivity.this )
                .setView( view)
                .setTitle( "Take a note" )
                .setCancelable( false )
                .setPositiveButton( "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        note_title_Et = (EditText) view.findViewById( R.id.title_d );
                        title = note_title_Et.getText().toString();
                        final Calendar calendar = Calendar.getInstance();
                        year = calendar.get( Calendar.YEAR );
                        month = calendar.get( Calendar.MONTH );
                        day = calendar.get( Calendar.DATE );
                        new DatePickerDialog( MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Toast.makeText( MainActivity.this, day + "-" + month + "-" + year, Toast.LENGTH_SHORT ).show();
                                date = day + "-" + month + "-" + year;
                                Calendar c = Calendar.getInstance();
                                hour = c.get( Calendar.HOUR_OF_DAY );
                                minute = c.get( Calendar.MINUTE );
                                TimePickerDialog timePickerDialog = new TimePickerDialog( MainActivity.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                                Toast.makeText( MainActivity.this, hour + "-" + minute, Toast.LENGTH_SHORT ).show();
                                                time = hour + "-" + minute;
                                                postOnServer( title, date, time );

                                                // GlobalMethods.sendCustomBroadcastAt( MainActivity.this,  );

                                            }
                                        }, hour, minute, false );
                                timePickerDialog.show();
                            }
                        }, year, month, day ).show();
                    }
                } )
                .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText( MainActivity.this, "Cancelled", Toast.LENGTH_SHORT ).show();
                    }
                } ).show();
    }

    private void postOnServer(String title, String description, String alarm) {
        AndroidNetworking.post("https://note-manager-parkingkoi.herokuapp.com/notes/")
                .addBodyParameter("title", title)
                .addBodyParameter("description", description)
                .addBodyParameter( "alarm", alarm )
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString( new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        getDataFromOnline();
                        Toast.makeText( MainActivity.this, "note added", Toast.LENGTH_SHORT ).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText( MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT ).show();
                    }
                } );
    }

    private void addData(Note note) {
        Log.d( TAG, "addData: "+note );
        notes.add( note );
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setAlarm:
                setAlarm();
                break;
            case R.id.cancelAlarm:
                cancelAlarm();
                break;
            case R.id.fab:
                setNote();
                break;
        }
    }

    private void cancelAlarm() {
        alarmManager.cancel(pendingIntent);
        Toast.makeText(getApplicationContext(), "Alarm Cancelled", Toast.LENGTH_LONG).show();
    }

    private void setAlarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
        }
        Toast.makeText( this, "alarm set", Toast.LENGTH_SHORT ).show();
    }
}