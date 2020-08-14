package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.TestModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TestListAdapter extends RecyclerView.Adapter {

    List<TestModel> testModels;
    public TestListRecyclerListener listener;

    public TestListAdapter(List<TestModel> testModels, TestListRecyclerListener listener) {
        this.testModels = testModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.test_list_item, parent, false);
        return new TestViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TestViewHolder) holder).testTitle.setText(testModels.get(position).getTestTitle().toUpperCase());
        String time = testModels.get(position).getTestTime() + " minutes";
        ((TestViewHolder) holder).testTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return testModels.size();
    }

    public interface TestListRecyclerListener{
        void onClick(View v, int position);
        void onLongClick(View v, int position);
    }

    private class TestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView testTitle;
        TextView testTime;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            testTitle = itemView.findViewById(R.id.test_title);
            testTime = itemView.findViewById(R.id.test_time);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onLongClick(view, getAdapterPosition());
            return true;
        }
    }
}
