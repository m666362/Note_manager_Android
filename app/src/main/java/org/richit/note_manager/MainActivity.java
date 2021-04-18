package org.richit.note_manager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
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

public class MainActivity extends AppCompatActivity {

    String TAG = this.getClass().getSimpleName();
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    ArrayList<Note> notes = new ArrayList<>();
    private int year, month, day, hour, minute;
    String title, description, date, time;
    ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
//        setContentView( R.layout.activity_main );

        initObject();
        getDataFromOnline();

//        recyclerView = findViewById( R.id.note_recycler_view );
//        noteAdapter = new NoteAdapter( this, notes );
//        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
//        recyclerView.setAdapter( noteAdapter );
//        FloatingActionButton fab = findViewById( R.id.fab );

        binding.fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog
                        .Builder( MainActivity.this )
                        .setView( LayoutInflater.from( MainActivity.this ).inflate( R.layout.dialog_signal, null ) )
                        .setTitle( "Take a note" )
                        .setCancelable( false )
                        .setPositiveButton( "Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
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
                                                        postOnServer( "note", date, time );
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
        } );
        noteAdapter.notifyDataSetChanged();

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

}