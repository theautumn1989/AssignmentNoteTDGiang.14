package com.example.tomato.assignmentnotetdgiang.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.tomato.assignmentnotetdgiang.R;
import com.example.tomato.assignmentnotetdgiang.model.Note;
import com.example.tomato.assignmentnotetdgiang.myInterface.OnCallBack;

import java.util.ArrayList;

public class RecyclerMainAdapter extends RecyclerView.Adapter<RecyclerMainAdapter.ViewHolder> {

    ArrayList<Note> arrNote;
    Context context;
    OnCallBack onCallBack;

    public RecyclerMainAdapter(ArrayList<Note> arrNote, Context context, OnCallBack onCallBack) {
        this.arrNote = arrNote;
        this.context = context;
        this.onCallBack = onCallBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_activity, parent, false);
        if (itemView != null) {
            return new ViewHolder(itemView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvTitle.setText(arrNote.get(position).getTitle());
        holder.tvContent.setText(arrNote.get(position).getContent());
        holder.rlItemBackgourdColor.setBackgroundColor(Color.parseColor(arrNote.get(position).getColor()));
        holder.tvTimeNow.setText(arrNote.get(position).getTimeNow());
        holder.ivStatusAlarm.setVisibility(View.GONE);

        if (arrNote.get(position).getDate() != null && arrNote.get(position).getTime() != null) {
            holder.ivStatusAlarm.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arrNote.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvTitle, tvContent, tvTimeNow;
        RelativeLayout rlItemBackgourdColor;
        ImageView ivStatusAlarm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            rlItemBackgourdColor = itemView.findViewById(R.id.rl_iteam_backgourd_color);
            tvTimeNow = itemView.findViewById(R.id.tv_timenow);
            ivStatusAlarm = itemView.findViewById(R.id.iv_status_alarm);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onCallBack.onItemClicked(getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {

            onCallBack.onItemClicked(getAdapterPosition(), true);
            return false;
        }
    }


}
