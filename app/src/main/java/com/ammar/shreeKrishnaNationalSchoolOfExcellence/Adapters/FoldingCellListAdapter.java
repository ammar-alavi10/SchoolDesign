package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.NoticeModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse.ShowNotesList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class FoldingCellListAdapter extends RecyclerView.Adapter<FoldingCellListAdapter.PhotoViewHolder> {

    private List<NoticeModel> models;
    private Context mContext;
    private String fun;

    public FoldingCellListAdapter(List<NoticeModel> models, Context mContext, String fun) {
        this.models = models;
        this.mContext = mContext;
        this.fun = fun;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_title_layout, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoViewHolder holder, final int position) {

        final NoticeModel model = models.get(position);
        holder.bind(model);
        if(models.get(position).getFileUrl() == null)
        {
            holder.download.setVisibility(View.GONE);
        }
        else{
            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadFile(mContext, models.get(position).getTitle(), ".pdf",
                            "SKNSE/Downloads", models.get(position).getFileUrl());
                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean expanded = model.expanded;
                // Change the state
                model.expanded = !expanded;
                // Notify the adapter that item has changed
                notifyItemChanged(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                SharedPreferences preferences = mContext.getSharedPreferences("com.ammar.shreeKrishnaNationalSchoolOfExcellence", Context.MODE_PRIVATE);
                int category = preferences.getInt("category", -1);
                if(category == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Confirm Delete !");
                    builder.setMessage("You are about to delete this Note. Do you really want to proceed ?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection(fun).whereEqualTo("title", models.get(position).getTitle()).
                                    get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        if (task.getResult() != null) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                String id = document.getId();
                                                final String url = document.getString("fileUrl");
                                                db.collection(fun).document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            if(url != null)
                                                            {
                                                                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                                                                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful())
                                                                        {
                                                                            Toast.makeText(mContext, "Note Deleted", Toast.LENGTH_SHORT).show();
                                                                            remove(holder.getAdapterPosition());
                                                                        }
                                                                        else {
                                                                            Toast.makeText(mContext, "Error in deleting notice", Toast.LENGTH_SHORT).show();
                                                                            Log.d("Notice", task.getException().toString());
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                            else{
                                                                Toast.makeText(mContext, "Note Deleted", Toast.LENGTH_SHORT).show();
                                                                remove(holder.getAdapterPosition());
                                                            }
                                                        }

                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();
                }
                return true;
            }
        });
    }

    private void remove(int adapterPosition) {
        models.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(adapterPosition, models.size());

    }

    public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {


        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        return downloadmanager.enqueue(request);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    // View lookup cache
    protected class PhotoViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView date;
        TextView content;
        TextView download;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_notice);
            date = itemView.findViewById(R.id.date_tv);
            content = itemView.findViewById(R.id.notice_content);
            download = itemView.findViewById(R.id.content_request_btn);
        }

        private void bind(NoticeModel noticeModel) {
            Log.d("Notice", noticeModel.getDate());
            // Get the state
            boolean expanded = noticeModel.expanded;
            // Set the visibility based on state
            content.setVisibility(expanded ? View.VISIBLE : View.GONE);
            download.setVisibility(expanded ? View.VISIBLE : View.GONE);

            title.setText(noticeModel.getTitle());
            date.setText(noticeModel.getDate());
            content.setText(noticeModel.getNotice());
            if(noticeModel.getFileUrl() != null)
            {
                download.setText("Click to Download");
            }
            else {
                download.setVisibility(View.GONE);
            }
        }

    }
}
