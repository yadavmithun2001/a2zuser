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

import com.cwservices.a2zproject.Adapters.ComplaintAdapter;
import com.cwservices.a2zproject.Models.ComplaintModel;
import com.cwservices.a2zproject.Models.UserModel;
import com.cwservices.a2zproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;


public class ComplaintFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase database;
    ComplaintModel complaintModel;
    LinearLayout linearLayout;
    ComplaintAdapter complaintAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complaint, container, false);
        RecyclerView recycle_complaint = view.findViewById(R.id.recycle_complaint);
        TextView nocomplaints = view.findViewById(R.id.nocomplaints);
        ArrayList<ComplaintModel> list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        linearLayout = view.findViewById(R.id.ltprogress);

        try {
            String phone = auth.getCurrentUser().getEmail().toString();
            String userid = phone.substring(0,phone.length()-10);

            database.getReference()
                    .child("usersdata")
                    .child(userid)
                    .child("complaints")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            complaintModel = snapshot.getValue(ComplaintModel.class);
                            list.add(complaintModel);
                            linearLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            FancyToast.makeText(getContext(),"SOMETHING WENT WRONG",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                        }
                    });

            complaintAdapter = new ComplaintAdapter(list,getContext());
            recycle_complaint.setAdapter(complaintAdapter);
            recycle_complaint.setLayoutManager(new LinearLayoutManager(getContext()));
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(list.isEmpty()){
                    nocomplaints.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }
            }
        },3000);

        return view;
    }
}