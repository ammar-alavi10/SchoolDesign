package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesListAdapter extends RecyclerView.Adapter {

    List<String> notesList;
    List<String> notesUrl;
    public RecyclerNotesViewClickListener listener;

    public NotesListAdapter(List<String> notesList, List<String> notesUrl, RecyclerNotesViewClickListener listener) {
        this.notesList = notesList;
        this.notesUrl = notesUrl;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.notes_list_item, parent, false);
        return new NotesViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((NotesViewHolder) holder).notesTitle.setText(notesList.get(position));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public interface RecyclerNotesViewClickListener{
        void onClick(View v, int position);
        void onLongClick(View v, int position);
    }

    private class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView notesTitle;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            notesTitle = itemView.findViewById(R.id.video_title_tv);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onLongClick(view, getAdapterPosition());
            return true;
        }
    }
}
