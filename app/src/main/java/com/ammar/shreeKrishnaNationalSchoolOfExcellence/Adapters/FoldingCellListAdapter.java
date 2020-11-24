package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.NoticeModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FoldingCellListAdapter extends RecyclerView.Adapter<FoldingCellListAdapter.PhotoViewHolder> {

    private List<NoticeModel> models;
    private Context mContext;

    public FoldingCellListAdapter(List<NoticeModel> models, Context mContext) {
        this.models = models;
        this.mContext = mContext;
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
    protected static class PhotoViewHolder extends RecyclerView.ViewHolder {

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
