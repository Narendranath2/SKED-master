package com.roostergames.www.sked;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class subjectadapter extends RecyclerView.Adapter<subjectadapter.subjectHolder> {
    ArrayList<subjectitem> msubjectlist;
    public static class subjectHolder extends  RecyclerView.ViewHolder{
        public TextView subjectName;
        public TextView subjectTime;
        public subjectHolder(View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectname);
            subjectTime = itemView.findViewById(R.id.subjecttime);
        }
    }
    public subjectadapter(ArrayList<subjectitem> subjectlist)
    {
        msubjectlist = subjectlist;

    }
    @NonNull
    @Override
    public subjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjectitem,parent,false);
        subjectHolder sh = new subjectHolder(v);
        return sh;
    }

    @Override
    public void onBindViewHolder(@NonNull subjectHolder holder, int position) {
        subjectitem currentItem = msubjectlist.get(position);
        holder.subjectName.setText(currentItem.getSubjectName());
        holder.subjectTime.setText(currentItem.getSubjectTime());
    }

    @Override
    public int getItemCount() {
        return msubjectlist.size();
    }
}
