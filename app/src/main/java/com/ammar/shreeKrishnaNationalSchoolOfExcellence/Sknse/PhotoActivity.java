package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.GalleryAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhotoActivity extends AppCompatActivity {

    private Button childBtn, schoolBtn;
    FirebaseStorage firebaseStorage;
    List<Uri> imageurls;
    RecyclerView recyclerView;
    private String class_name, roll_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageurls = new ArrayList<>();

        childBtn = findViewById(R.id.child_button);
        schoolBtn = findViewById(R.id.school_button);

        SharedPreferences preferences = getSharedPreferences("com.ammar.shreeKrishnaNationalSchoolOfExcellence", MODE_PRIVATE);
        int category = preferences.getInt("category", -1);

        firebaseStorage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.recyclerview_PhotosList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        if(FirebaseAuth.getInstance().getCurrentUser() == null || category != 1)
        {
            childBtn.setVisibility(View.GONE);
            schoolBtn.setVisibility(View.GONE);
            ShowSchoolPhotos();
        }
    }

    public void ChildPhotos(View view) {
        childBtn.setVisibility(View.GONE);
        schoolBtn.setVisibility(View.GONE);

        FirebaseFirestore.getInstance().collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null)
                    {
                        class_name = documentSnapshot.getString("class");
                        roll_no = documentSnapshot.getString("rollno");
                        ShowChildPhotos();
                    }
                }
            }
        });
    }

    private void ShowChildPhotos() {
        StorageReference storageReference = firebaseStorage.getReference("ChildGallery/" + class_name + roll_no);
        storageReference.listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                if(task.isSuccessful())
                {
                    ListResult listResult = task.getResult();
                    if (listResult != null)
                    {
                        for(StorageReference item : listResult.getItems())
                        {
                            Uri url = item.getDownloadUrl().getResult();
                            imageurls.add(url);
                        }
                        InitRecycler();
                    }
                }
            }
        });
    }

    public void SchoolPhotos(View view) {
        childBtn.setVisibility(View.GONE);
        schoolBtn.setVisibility(View.GONE);
        ShowSchoolPhotos();
    }

    private void ShowSchoolPhotos() {
        StorageReference storageReference = firebaseStorage.getReference("SchoolGallery");
        storageReference.listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                if(task.isSuccessful())
                {
                    ListResult listResult = task.getResult();
                    if (listResult != null)
                    {
                        for(StorageReference item : listResult.getItems())
                        {
                            Uri url = item.getDownloadUrl().getResult();
                            imageurls.add(url);
                        }
                        InitRecycler();
                    }
                }
            }
        });
    }

    public void ShowPopUp(Uri imageUrl) {
        Picasso.setSingletonInstance(new Picasso.Builder(this).build());
        final ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.initiatePopupWithPicasso(imageUrl);
        imagePopup.viewPopup();
    }

    private void InitRecycler() {
        GalleryAdapter.GalleryCickListener listener = new GalleryAdapter.GalleryCickListener() {
            @Override
            public void onClick(View v, int position) {
                ShowPopUp(imageurls.get(position));
            }
        };
        recyclerView.setAdapter(new GalleryAdapter(imageurls, listener));
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(PhotoActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}