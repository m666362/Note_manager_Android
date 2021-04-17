package org.richit.note_manager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.richit.note_manager.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

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

        binding = DataBindingUtil.setContentView( this, R.layout.activity_main );

        noteAdapter = new NoteAdapter( this, notes );
        binding.noteRecyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        binding.noteRecyclerView.setAdapter( noteAdapter );

//        recyclerView = findViewById( R.id.note_recycler_view );
//        noteAdapter = new NoteAdapter( this, notes );
//        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
//        recyclerView.setAdapter( noteAdapter );

        AndroidNetworking.initialize( getApplicationContext() );

        notes.add( new Note( "I am title", "I am description" ) );
        notes.add( new Note( "I am title", "I am description" ) );

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
                                        Toast.makeText( MainActivity.this, day+"-"+month+"-"+year, Toast.LENGTH_SHORT ).show();
                                        date = day+"-"+month+"-"+year;
                                        Calendar c = Calendar.getInstance();
                                        hour = c.get(Calendar.HOUR_OF_DAY);
                                        minute = c.get(Calendar.MINUTE);
                                        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                                                new TimePickerDialog.OnTimeSetListener() {
                                                    @Override
                                                    public void onTimeSet(TimePicker view, int hour, int minute) {
                                                        Toast.makeText( MainActivity.this, hour+"-"+minute, Toast.LENGTH_SHORT ).show();
                                                        time = hour+"-"+minute;
                                                        notes.add( new Note( date, time ) );
                                                        noteAdapter.notifyDataSetChanged();
                                                    }
                                                }, hour, minute, false);
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

        binding.api.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking
                        .get( "https://192.168.0.151:4000/users" )
                        .setTag( "test" )
                        .setPriority( Priority.LOW)
                        .build()
                        .getAsJSONArray( new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Toast.makeText( MainActivity.this, response.toString(), Toast.LENGTH_SHORT ).show();
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText( MainActivity.this, "Error", Toast.LENGTH_SHORT ).show();
                            }
                        } );
            }
        } );

    }

}