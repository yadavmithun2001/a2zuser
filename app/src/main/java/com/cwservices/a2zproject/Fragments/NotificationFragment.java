package com.cwservices.a2zproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cwservices.a2zproject.Adapters.NotificationAdapter;
import com.cwservices.a2zproject.Models.ComplaintModel;
import com.cwservices.a2zproject.Models.NotificationModel;
import com.cwservices.a2zproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;


import java.util.ArrayList;
import java.util.Objects;


public class NotificationFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase database;
    NotificationModel notificationModel;
    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        RecyclerView recycle_notifications = view.findViewById(R.id.recycle_notifications);
        TextView no_notifications = view.findViewById(R.id.no_notifications);
        ArrayList<NotificationModel> list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        linearLayout = view.findViewById(R.id.ltprogress);

        try {
            String phone = auth.getCurrentUser().getEmail();
            String userid = phone.substring(0,phone.length()-10);
            database.getReference()
                    .child("notifications")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            notificationModel = snapshot.getValue(NotificationModel.class);
                            list.add(notificationModel);
                            linearLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            FancyToast.makeText(getContext(),"SOMETHING WENT WRONG",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                        }
                    });

            NotificationAdapter notificationAdapter = new NotificationAdapter(list,getContext());
            recycle_notifications.setAdapter(notificationAdapter);
            recycle_notifications.setLayoutManager(new LinearLayoutManager(getContext()));

        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(list.isEmpty()){
                    no_notifications.setVisibility(View.VISIBLE);
                    recycle_notifications.setVisibility(View.GONE);
                }
            }
        },3000);




        return  view;
    }
}