package org.richit.note_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    Context context;
    ArrayList<Note> notes = new ArrayList<>();

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from( context ).inflate( R.layout.note_rv, parent, false );
        ViewHolder viewHolder = new ViewHolder( view );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        holder.noteTitleTv.setText( notes.get( position ).getTitle() );
        holder.noteDescriptionTv.setText( notes.get( position ).getDescription() );
        holder.alarmTv.setText( notes.get( position ).getAlarm() );
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitleTv;
        TextView noteDescriptionTv;
        TextView alarmTv;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            noteTitleTv = itemView.findViewById( R.id.noteTitle );
            noteDescriptionTv = itemView.findViewById( R.id.noteDescription );
            alarmTv = itemView.findViewById( R.id.alarm );
        }
    }
}
