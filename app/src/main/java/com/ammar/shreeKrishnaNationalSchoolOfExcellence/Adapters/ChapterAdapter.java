package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.Chapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.Subject;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ChapterAdapter extends RecyclerView.Adapter{
    List<Chapter> items;
    public SubjectAdapter.RecyclerViewClickListener listener;

    public ChapterAdapter(List<Chapter> items, SubjectAdapter.RecyclerViewClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.subject_layout, parent, false);
        return new ChapterViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String above = "Chapter : " + items.get(position).getChapter_no();
        ((ChapterViewHolder) holder).text.setText(above);
        ((ChapterViewHolder) holder).textbottom.setText(items.get(position).getChapter_name());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }

    private class ChapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView card;
        TextView text;
        TextView textbottom;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.classcard);
            text = itemView.findViewById(R.id.classname);
            textbottom = itemView.findViewById(R.id.recyclertext2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
