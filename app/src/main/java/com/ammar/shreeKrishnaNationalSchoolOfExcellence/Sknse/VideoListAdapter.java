package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoListAdapter extends RecyclerView.Adapter {

    List<String> videoList;
    List<String> videoType;
    List<String> videoUrl;
    public RecyclerVideoViewClickListener listener;

    public VideoListAdapter(List<String> videoList, List<String> videoType, List<String> videoUrl, RecyclerVideoViewClickListener listener) {
        this.videoList = videoList;
        this.videoType = videoType;
        this.videoUrl = videoUrl;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((VideoViewHolder) holder).videoTitle.setText(videoList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public interface RecyclerVideoViewClickListener{
        void onClick(View v, int position);
    }

    private class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView videoTitle;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.video_title_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
