package org.richit.note_manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    ArrayList<Note> notes = new ArrayList<>();
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( MainActivity.this, "Hello world", Toast.LENGTH_SHORT ).show();
            }
        });

    }
}