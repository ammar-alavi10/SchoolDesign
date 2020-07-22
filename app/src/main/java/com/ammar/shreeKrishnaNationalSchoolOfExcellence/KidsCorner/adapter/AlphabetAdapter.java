package com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.activity.ViewDetailsActivity;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.model.Alphabet;

import java.util.List;

public class AlphabetAdapter extends RecyclerView.Adapter<AlphabetAdapter.MyViewHolder> {

    private Context mContext;
    private List<Alphabet> alphabetList;

    public AlphabetAdapter(Context mContext, List<Alphabet> alphabetList) {
        this.mContext = mContext;
        this.alphabetList = alphabetList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alphabet_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Alphabet alphabet = alphabetList.get(position);

        final Bitmap image = BitmapFactory.decodeResource(mContext.getResources(),
                alphabet.getThumbnail1());
        holder.thumbnail.setImageBitmap(image);

      /*  // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);*/
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ViewDetailsActivity.class);
                intent.putExtra("name", alphabet.getName());
                intent.putExtra("title2", alphabet.getTitle2());
                intent.putExtra("title3", alphabet.getTitle3());
                intent.putExtra("title4", alphabet.getTitle4());
                intent.putExtra("image1", alphabet.getThumbnail1());
                intent.putExtra("image2", alphabet.getThumbnail2());
                intent.putExtra("image3", alphabet.getThumbnail3());
                intent.putExtra("image4", alphabet.getThumbnail4());
                intent.putExtra("lettersound", alphabet.getLettersound());
                intent.putExtra("sound1", alphabet.getSound1());
                intent.putExtra("sound2", alphabet.getSound2());
                intent.putExtra("sound3", alphabet.getSound3());

                mContext.startActivity(intent);
            }
        });


        ScaleAnimation fade_in = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(1000);     // animation duration in milliseconds
        fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
        holder.thumbnail.startAnimation(fade_in);


    }


    @Override
    public int getItemCount() {
        return alphabetList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public TextView textView;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
            textView = view.findViewById(R.id.title_tv);

        }
    }


}

