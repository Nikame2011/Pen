package com.pf.application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {

    TrainingDescription info;

    public TrainingAdapter(TrainingDescription info) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView docName;
//        CheckBox cbSelect;
//        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
//            docName = itemView.findViewById(R.id.tv);
//            cbSelect = itemView.findViewById(R.id.cb);
//            llItem = itemView.findViewById(R.id.ll);
        }
    }
}
