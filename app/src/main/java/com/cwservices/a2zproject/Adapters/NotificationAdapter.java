package com.cwservices.a2zproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.cwservices.a2zproject.Models.NotificationModel;
import com.cwservices.a2zproject.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewholder> {
    ArrayList<NotificationModel> list = new ArrayList<>();
    Context context;

    public NotificationAdapter(ArrayList<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_norification,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        NotificationModel notificationModel = list.get(position);
        holder.notifiication_date.setText(notificationModel.getDate());
        holder.notification_name.setText(notificationModel.getNotification());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView notifiication_date,notification_name;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            notifiication_date = itemView.findViewById(R.id.notification_date);
            notification_name = itemView.findViewById(R.id.notification_name);
        }
    }
}
