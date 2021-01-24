package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryAdapter extends RecyclerView.Adapter {

    private List<Uri> imageurl;
    GalleryCickListener listener;

    public GalleryAdapter(List<Uri> imageurl, GalleryCickListener listener) {
        this.imageurl = imageurl;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.gallery_item, parent, false);
        return new GalleryViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Picasso.get().load(imageurl.get(position)).into(((GalleryViewHolder) holder).image);
    }

    public interface GalleryCickListener{
        void onClick(View v, int position);
    }

    @Override
    public int getItemCount() {
        return imageurl.size();
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.card_image);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
