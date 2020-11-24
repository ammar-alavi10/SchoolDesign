package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.TestResultModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TestScoreAdapter extends RecyclerView.Adapter {

    List<TestResultModel> testResultModels;

    public TestScoreAdapter(List<TestResultModel> testResultModels) {
        this.testResultModels = testResultModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.test_score_item, parent, false);
        return new ResultViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ResultViewHolder) holder).title.setText(testResultModels.get(position).getTitle());
        ((ResultViewHolder) holder).total.setText("Total : " + testResultModels.get(position).getTotal());
        ((ResultViewHolder) holder).correct.setText("Correct : " + testResultModels.get(position).getCorrect());
        ((ResultViewHolder) holder).wrong.setText("Wrong : " + testResultModels.get(position).getWrong());
        ((ResultViewHolder) holder).unanswered.setText("Unanswered : " + testResultModels.get(position).getUnanswered());
    }

    @Override
    public int getItemCount() {
        return testResultModels.size();
    }

    private class ResultViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView correct;
        TextView wrong;
        TextView total;
        TextView unanswered;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.test_title);
            correct = itemView.findViewById(R.id.test_correct);
            wrong = itemView.findViewById(R.id.test_wrong);
            total = itemView.findViewById(R.id.test_total);
            unanswered = itemView.findViewById(R.id.test_unans);
        }
    }
}
