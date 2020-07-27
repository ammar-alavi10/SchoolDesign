package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.Subject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SubjectAdapter extends RecyclerView.Adapter {

    List<Subject> items;
    int[] images;
    public RecyclerViewClickListener listener;

    public SubjectAdapter(List<Subject> items, int[] images, RecyclerViewClickListener listener) {
        this.items = items;
        this.images = images;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.subject_layout, parent, false);
        return new SubjectViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((SubjectViewHolder) holder).text.setText(items.get(position).getSubject_name());
        String below = "Class : " + items.get(position).getClass_name();
        ((SubjectViewHolder) holder).textbottom.setText(below);
        ((SubjectViewHolder) holder).relativeLayout.setBackgroundResource(images[position % 4]);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }

    private class SubjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView card;
        TextView text;
        TextView textbottom;
        RelativeLayout relativeLayout;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.classcard);
            text = itemView.findViewById(R.id.classname);
            textbottom = itemView.findViewById(R.id.recyclertext2);
            relativeLayout = itemView.findViewById(R.id.classbg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
