package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Score extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Score> scores;
    private OnItemClickListener mItemClickListener;

    public Adapter_Score(ArrayList<Score> scores) {
        this.scores = scores;
    }

    public void updateList(ArrayList<Score> scores) {
        this.scores = scores;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final Score score = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

           genericViewHolder.note_LBL_title.setText(score.toString());
        }
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    private Score getItem(int position) {
        return scores.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView note_LBL_title;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.note_LBL_title = itemView.findViewById(R.id.list_score_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), getItem(getAdapterPosition()));
                }
            });
        }
    }

    public void removeAt(int position) {
        scores.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, scores.size());
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Score score);
    }
}
