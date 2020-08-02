package com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageGame extends AppCompatActivity {

    private StorageReference storageReference;
    private List<Uri> downloadurl;
    private ImageView image;
    private int position;
    private Button prev, next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_game);
        downloadurl = new ArrayList<>();
        image = findViewById(R.id.slideimage);
        position = 0;
        prev = findViewById(R.id.prevbtn);
        next = findViewById(R.id.nextbtn);
        storageReference = FirebaseStorage.getInstance().getReference().child("Slideimages");
        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference item: listResult.getItems())
                {
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadurl.add(uri);
                            if(downloadurl.size() == 1)
                            {
                                Picasso.get()
                                        .load(downloadurl.get(position))
                                        .centerInside()
                                        .fit()
                                        .priority(Picasso.Priority.HIGH)
                                        .placeholder(R.drawable.placeholder)
                                        .into(image);
                            }
                            if(downloadurl.size() > 1 && next.getVisibility() == View.INVISIBLE)
                            {
                                next.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ImageGame.this, "Problem in loading Images..", Toast.LENGTH_LONG).show();
                }
            });
    }

    public void NextImage(View view) {
        position = position + 1;
        if(position == downloadurl.size() - 1)
        {
            next.setVisibility(View.INVISIBLE);
        }
        Picasso.get()
                .load(downloadurl.get(position))
                .centerInside()
                .fit()
                .placeholder(R.drawable.placeholder)
                .priority(Picasso.Priority.HIGH)
                .into(image);
        prev.setVisibility(View.VISIBLE);
    }

    public void PreviousImage(View view) {
        position = position - 1;
        if(position == 0)
        {
            prev.setVisibility(View.INVISIBLE);
        }
        Picasso.get()
                .load(downloadurl.get(position))
                .centerInside()
                .fit()
                .placeholder(R.drawable.placeholder)
                .priority(Picasso.Priority.HIGH)
                .into(image);
        next.setVisibility(View.VISIBLE);
    }
}