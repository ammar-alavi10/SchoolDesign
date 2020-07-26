/*
package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ShowVideo extends AppCompatActivity {

    private  static final int PERMISSION_STORAGE_CODE = 1000;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    String name, url, downloadurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);

        recyclerView = findViewById(R.id.recyclerview_ShowVideoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void firebaseSearch(String searchtext){
        String query = searchtext.toLowerCase();
        Query firebaseQuery = databaseReference.orderByChild("search").startAt(query).endAt(query + "\uf8ff");

        FirebaseRecyclerOptions<VideoModel> options =
                new FirebaseRecyclerOptions.Builder<VideoModel>()
                        .setQuery(firebaseQuery,VideoModel.class)
                        .build();

        FirebaseRecyclerAdapter<VideoModel, RecyclerView.ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<VideoModel, RecyclerView.ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull VideoModel model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserId = user.getUid();
                        final  String postkey = getRef(position).getKey();
                        holder.setExoplayer(getApplication(),model.getName(),model.getVideourl());

                        holder.setOnClicklistener(new RecyclerView.ViewHolder.Clicklistener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                name = getItem(position).getName();
                                url = getItem(position).getVideourl();
                                Intent intent = new Intent(ShowVideoList.this,Fullscreen.class);
                                intent.putExtra("name",name);
                                intent.putExtra("url",url);
                                startActivity(intent);


                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                                name = getItem(position).getName();
                                showDeleteDialog(name);
                            }
                        });

                        holder.commentbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(Showvideo.this,CommentsActivity.class);
                                intent.putExtra("postkey",postkey);
                                startActivity(intent);

                            }
                        });

                        holder.likebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                likechecker = true;

                                likesrefernce.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (likechecker.equals(true)){
                                            if (dataSnapshot.child(postkey).hasChild(currentUserId)){
                                                likesrefernce.child(postkey).child(currentUserId).removeValue();
                                                likechecker = false;
                                            }else {
                                                likesrefernce.child(postkey).child(currentUserId).setValue(true);
                                                likechecker = false;
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item,parent,false);

                        return new ViewHolder(view);

                    }
                };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }





    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<VideoModel> options =
                new FirebaseRecyclerOptions.Builder<VideoModel>()
                        .setQuery(databaseReference,VideoModel.class)
                        .build();

        FirebaseRecyclerAdapter<VideoModel, RecyclerView.ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<VideoModel, RecyclerView.ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position, @NonNull VideoModel model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserId = user.getUid();
                        final  String postkey = getRef(position).getKey();

                        holder.setExoplayer(getApplication(),model.getName(),model.getVideourl());



                        holder.setOnClicklistener(new RecyclerView.ViewHolder.Clicklistener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                name = getItem(position).getName();
                                url = getItem(position).getVideourl();
                                Intent intent = new Intent(ShowVideoList.this,Fullscreen.class);
                                intent.putExtra("nam",name);
                                intent.putExtra("ur",url);
                                startActivity(intent);


                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                                name = getItem(position).getName();
                                showDeleteDialog(name);
                            }
                        });

                        holder.downloadbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                    if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                            PackageManager.PERMISSION_DENIED){
                                        String permission = (Manifest.permission.WRITE_EXTERNAL_STORAGE);

                                        requestPermissions(new String[]{permission},PERMISSION_STORAGE_CODE);
                                    }else {
                                        downloadurl = getItem(position).getVideourl();
                                        startDownloading(downloadurl);
                                    }
                                }else {

                                    startDownloading(downloadurl);
                                }

                            }
                        });


                        holder.setLikesbuttonStatus(postkey);
                        holder.commentbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(ShowVideoList.this,CommentsActivity.class);
                                intent.putExtra("postkey",postkey);
                                startActivity(intent);

                            }
                        });


                        holder.likebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                likechecker = true;

                                likesrefernce.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (likechecker.equals(true)){
                                            if (dataSnapshot.child(postkey).hasChild(currentUserId)){
                                                likesrefernce.child(postkey).child(currentUserId).removeValue();
                                                likechecker = false;
                                            }else {
                                                likesrefernce.child(postkey).child(currentUserId).setValue(true);
                                                likechecker = false;
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });

                    }



                    @NonNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item,parent,false);

                        return new ViewHolder(view);

                    }
                };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void startDownloading(String downloadurl) {

        DownloadManager.Request request  = new DownloadManager.Request(Uri.parse(downloadurl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading file...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());

        DownloadManager manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.username_item:
                Intent intent = new Intent(ShowVideoList.this,Username.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.search_firebase);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    private void showDeleteDialog(final String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowVideoList.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you Sure to Delete this data");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Query query = databaseReference.orderByChild("name").equalTo(name);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            dataSnapshot1.getRef().removeValue();
                        }
                        Toast.makeText(ShowVideoList.this, "Video Deleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        ///
                    }
                });

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case PERMISSION_STORAGE_CODE:{


                if (grantResults.length >0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

                    startDownloading(downloadurl);
                }
                else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                }




            }
        }



    }
}*/
