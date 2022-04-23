package com.cwservices.a2zproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cwservices.a2zproject.Models.PastUpdateModel;
import com.cwservices.a2zproject.R;

import java.util.ArrayList;

public class PastUpdateAdapter extends RecyclerView.Adapter<PastUpdateAdapter.viewholder> {
    ArrayList<PastUpdateModel> list = new ArrayList<>();
    Context context;

    public PastUpdateAdapter(ArrayList<PastUpdateModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public ArrayList<PastUpdateModel> getList() {
        return list;
    }

    public void setList(ArrayList<PastUpdateModel> list) {
        this.list = list;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_pastupdate,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        PastUpdateModel pastUpdateModel = list.get(position);
        holder.pastupdate_date.setText(pastUpdateModel.getDate());
        holder.pastupdate_status.setText(pastUpdateModel.getStatus());
        holder.time_status.setText(pastUpdateModel.getTime_status());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView pastupdate_date,pastupdate_status,time_status;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            pastupdate_date = itemView.findViewById(R.id.pastupdate_date);
            pastupdate_status = itemView.findViewById(R.id.pastupate_status);
            time_status = itemView.findViewById(R.id.time_status);
        }
    }
}
