package com.cwservices.a2zproject.Fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cwservices.a2zproject.Adapters.PastUpdateAdapter;
import com.cwservices.a2zproject.Adapters.SliderAdapter;
import com.cwservices.a2zproject.MapsActivity;
import com.cwservices.a2zproject.Models.ComplaintModel;
import com.cwservices.a2zproject.Models.LocationModel;
import com.cwservices.a2zproject.Models.NotificationModel;
import com.cwservices.a2zproject.Models.PastUpdateModel;
import com.cwservices.a2zproject.Models.SliderData;
import com.cwservices.a2zproject.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.smarteist.autoimageslider.SliderView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {
    String url1 = "";
    String url2 = "";
    String url3 = "";
    BottomSheetDialog bottomSheetDialog;
    CardView complaint,livetracking;
    RadioButton vnotarrive,imissv;
    FirebaseAuth auth;
    FirebaseDatabase database;
    TextView notification;
    Location location1,location2;
    LocationModel model;
    float distanceInMeters;
    int speedIs10MetersPerMinute = 20;
    float estimatedDriveTimeInMinutes;
    TextView estimated;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayout pg = view.findViewById(R.id.ltprogress);
        getimages();

        bottomSheetDialog = new BottomSheetDialog(requireActivity(),R.style.BottomSheetDialog);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.complaint_dialog);
        complaint = bottomSheetDialog.findViewById(R.id.filecomplaint);
        vnotarrive = bottomSheetDialog.findViewById(R.id.radioButton);
        imissv = bottomSheetDialog.findViewById(R.id.radioButton2);
        notification = view.findViewById(R.id.textView6);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        estimated = view.findViewById(R.id.textView8);


        imissv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vnotarrive.setChecked(false);
            }
        });
        vnotarrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imissv.setChecked(false);
            }
        });

        database.getReference()
                .child("notifications")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       NotificationModel notificationModel = snapshot.getValue(NotificationModel.class);
                       notification.setText(notificationModel.getNotification()+"");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        FancyToast.makeText(getContext(),"SOMETHING WENT WRONG",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                    }
                });

        location1 = new Location("");
        location2 = new Location("");
        location2.setLatitude(28.61522041);
        location2.setLongitude(77.09382387);
        database.getReference()
                .child("driverlocation")
                .child("driver1")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        model = snapshot.getValue(LocationModel.class);
                        location1.setLatitude(model.getLatitude());
                        location1.setLongitude(model.getLongitude());
                        location2.setLatitude(28.61522041);
                        location2.setLongitude(77.09382387);
                        distanceInMeters = location1.distanceTo(location2);
                        estimatedDriveTimeInMinutes = distanceInMeters / speedIs10MetersPerMinute;
                        estimated.setText("Estimated Time of Arrival -"+ String.valueOf((int)estimatedDriveTimeInMinutes)+" min");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(vnotarrive.isChecked()){
                   try {
                       String phone = auth.getCurrentUser().getEmail().toString();
                       String userid = phone.substring(0,phone.length()-10);
                       ComplaintModel complaintModel = new ComplaintModel(date,vnotarrive.getText().toString(),"Not Resolved");
                       database.getReference()
                               .child("usersdata")
                               .child(userid)
                               .child("complaints")
                               .setValue(complaintModel);
                       database.getReference()
                               .child("complaints")
                               .child(userid)
                               .setValue(complaintModel);
                       FancyToast.makeText(getContext(),"Your complaint received successfully",Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                       bottomSheetDialog.dismiss();
                   }catch (Exception e){
                       FancyToast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                   }

               }else if(imissv.isChecked()) {
                   try {
                       String phone = auth.getCurrentUser().getEmail().toString();
                       String userid = phone.substring(0,phone.length()-10);
                       ComplaintModel complaintModel = new ComplaintModel(date,imissv.getText().toString(),"Not Resolved");
                       database.getReference()
                               .child("usersdata")
                               .child(userid)
                               .child("complaints")
                               .setValue(complaintModel);
                       FancyToast.makeText(getContext(),"Your complaint received successfully",Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                       bottomSheetDialog.dismiss();
                   }catch (Exception e){
                       FancyToast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                   }
               }
            }
        });

        livetracking = view.findViewById(R.id.livetracking);
        livetracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                requireActivity().startActivity(intent);
            }
        });

        pg.setVisibility(View.GONE);


        RecyclerView recyle_pastupdate = view.findViewById(R.id.recyle_pastupdate);
        TextView no_pastupdate = view.findViewById(R.id.no_pastupdate);
        ArrayList<PastUpdateModel> list = new ArrayList<>();
        list.add(new PastUpdateModel("","No any past updates",""));
        PastUpdateAdapter pastUpdateAdapter = new PastUpdateAdapter(list,getContext());
        recyle_pastupdate.setAdapter(pastUpdateAdapter);
        recyle_pastupdate.setLayoutManager(new LinearLayoutManager(getContext()));





        TextView text_loadgecomplanit = view.findViewById(R.id.text_lodgecomplaint);
        text_loadgecomplanit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getimages();
                ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
                SliderView sliderView = view.findViewById(R.id.slider);
                sliderDataArrayList.add(new SliderData(url1));
                sliderDataArrayList.add(new SliderData(url2));
                sliderDataArrayList.add(new SliderData(url3));
                SliderAdapter adapter = new SliderAdapter(getContext(), sliderDataArrayList);
                sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                sliderView.setSliderAdapter(adapter);
                sliderView.setScrollTimeInSec(3);
                sliderView.setAutoCycle(true);
                sliderView.startAutoCycle();
                if(list.isEmpty()){
                    no_pastupdate.setVisibility(View.VISIBLE);
                    recyle_pastupdate.setVisibility(View.GONE);
                }
            }
        },3000);

        return  view;
    }

    void getimages(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference slider1 = databaseReference.child("sliderimage1");
        slider1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                url1 = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference slider2 = databaseReference.child("sliderimage2");
        slider2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                url2 = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference slider3 = databaseReference.child("sliderimage3");
        slider3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                url3 = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}