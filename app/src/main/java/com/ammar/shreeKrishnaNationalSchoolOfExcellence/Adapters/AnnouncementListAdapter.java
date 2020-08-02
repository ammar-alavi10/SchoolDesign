package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.AnnouncementModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AnnouncementListAdapter extends RecyclerView.Adapter {

    List<AnnouncementModel> announcements;

    public AnnouncementListAdapter(List<AnnouncementModel> announcements) {
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.announcement_items, parent, false);
        return new AnnouncementsViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AnnouncementsViewHolder) holder).title.setText(announcements.get(position).getTitle());
        ((AnnouncementsViewHolder) holder).date.setText(announcements.get(position).getDate());
        ((AnnouncementsViewHolder) holder).desc.setText(announcements.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    private class AnnouncementsViewHolder extends RecyclerView.ViewHolder{

        TextView title, desc, date;

        public AnnouncementsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card_announcement_title);
            date = itemView.findViewById(R.id.announcement_date);
            desc = itemView.findViewById(R.id.announcement_desc);
        }
    }
}
