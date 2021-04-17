package org.richit.note_manager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    ArrayList<Note> notes = new ArrayList<>();
    private int year, month, day, hour, minute;
    String date, time;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        recyclerView = findViewById( R.id.note_recycler_view );
        noteAdapter = new NoteAdapter( this, notes );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        recyclerView.setAdapter( noteAdapter );

        notes.add( new Note( "I am title", "I am description" ) );
        notes.add( new Note( "I am title", "I am description" ) );
        notes.add( new Note( "I am title", "I am description" ) );
        notes.add( new Note( "I am title", "I am description" ) );
        notes.add( new Note( "I am title", "I am description" ) );
        notes.add( new Note( "I am title", "I am description" ) );
        notes.add( new Note( "I am title", "I am description" ) );

        noteAdapter.notifyDataSetChanged();


        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( MainActivity.this, "Hello world", Toast.LENGTH_SHORT ).show();

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
                                    }
                                }, hour, minute, false);
                        timePickerDialog.show();
                    }
                }, year, month, day ).show();

            }
        } );

    }
}