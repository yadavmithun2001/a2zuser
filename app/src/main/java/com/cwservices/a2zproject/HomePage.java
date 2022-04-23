package com.cwservices.a2zproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cwservices.a2zproject.Fragments.ComplaintFragment;
import com.cwservices.a2zproject.Fragments.HomeFragment;
import com.cwservices.a2zproject.Fragments.NotificationFragment;
import com.cwservices.a2zproject.Fragments.ProfieFragment;


public class HomePage extends AppCompatActivity {

    LinearLayout lthome,ltcomplaint,ltprofile;
    int index ;
    ImageView bottomhome_img,bottomcomplaint_img,bottomprofile_img;
    TextView bottonhome_txt,bottomcomplaint_txt,bottomprofile_txt;

    ImageView notification,homeback;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,new HomeFragment()).commit();


        lthome = findViewById(R.id.lthome);
        ltcomplaint = findViewById(R.id.ltcomplaint);
        ltprofile = findViewById(R.id.ltprofile);

        bottomhome_img = findViewById(R.id.bottomhome_img);
        bottomcomplaint_img = findViewById(R.id.bottomcomplaint_img);
        bottomprofile_img = findViewById(R.id.bottomprofile_img);

        bottonhome_txt = findViewById(R.id.bottomhome_txt);
        bottomcomplaint_txt = findViewById(R.id.bottomcomplaint_txt);
        bottomprofile_txt = findViewById(R.id.bottomprofile_txt);

        lthome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 0;
                setbottomcolor(index);
                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,new HomeFragment()).commit();
            }
        });
        ltcomplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,new ComplaintFragment()).commit();
                index = 1;
                setbottomcolor(index);
            }
        });
        ltprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,new ProfieFragment()).commit();
                index = 2;
                setbottomcolor(index);
            }
        });

        notification = findViewById(R.id.img_notification);
        title = findViewById(R.id.hometitle);
        homeback = findViewById(R.id.homeback);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,new NotificationFragment()).commit();
                index = 3;
                setbottomcolor(3);
                homeback.setVisibility(View.VISIBLE);
            }
        });
        homeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,new HomeFragment()).commit();
                setbottomcolor(0);
            }
        });





    }
    @SuppressLint("SetTextI18n")
    void setbottomcolor(int index){
        if(index == 0){
            bottomcomplaint_txt.setTextColor(getResources().getColor(R.color.white));
            bottonhome_txt.setTextColor(getResources().getColor(R.color.black));
            bottomprofile_txt.setTextColor(getResources().getColor(R.color.white));
            bottomhome_img.setImageDrawable(getDrawable(R.drawable.home1));
            bottomcomplaint_img.setImageDrawable(getDrawable(R.drawable.complaint));
            bottomprofile_img.setImageDrawable(getDrawable(R.drawable.user));
            title.setText("Welcome To A2Z Group");
            homeback.setVisibility(View.GONE);
            notification.setImageDrawable(getDrawable(R.drawable.notification42));
        }
        if(index == 1){
            bottomcomplaint_txt.setTextColor(getResources().getColor(R.color.black));
            bottonhome_txt.setTextColor(getResources().getColor(R.color.white));
            bottomprofile_txt.setTextColor(getResources().getColor(R.color.white));
            bottomhome_img.setImageDrawable(getDrawable(R.drawable.home));
            bottomcomplaint_img.setImageDrawable(getDrawable(R.drawable.complaint1));
            bottomprofile_img.setImageDrawable(getDrawable(R.drawable.user));
            title.setText("Complaint Lodged");
            homeback.setVisibility(View.VISIBLE);
            notification.setImageDrawable(getDrawable(R.drawable.notification42));
        }if(index == 2){
            bottomcomplaint_txt.setTextColor(getResources().getColor(R.color.white));
            bottonhome_txt.setTextColor(getResources().getColor(R.color.white));
            bottomprofile_txt.setTextColor(getResources().getColor(R.color.black));
            bottomhome_img.setImageDrawable(getDrawable(R.drawable.home));
            bottomcomplaint_img.setImageDrawable(getDrawable(R.drawable.complaint));
            bottomprofile_img.setImageDrawable(getDrawable(R.drawable.user1));
            title.setText("      Profile");
            homeback.setVisibility(View.VISIBLE);
            notification.setImageDrawable(getDrawable(R.drawable.notification42));
        }if(index == 3){
            notification.setImageDrawable(getDrawable(R.drawable.notification41));
            bottomcomplaint_txt.setTextColor(getResources().getColor(R.color.white));
            bottonhome_txt.setTextColor(getResources().getColor(R.color.white));
            bottomprofile_txt.setTextColor(getResources().getColor(R.color.white));
            bottomhome_img.setImageDrawable(getDrawable(R.drawable.home));
            bottomcomplaint_img.setImageDrawable(getDrawable(R.drawable.complaint));
            bottomprofile_img.setImageDrawable(getDrawable(R.drawable.user));
            title.setText("      Noitifications");
            homeback.setVisibility(View.VISIBLE);
            homeback.setVisibility(View.GONE);

        }

    }

    @Override
    public void onBackPressed() {
        if(index > 0){
            getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,new HomeFragment()).commit();
            setbottomcolor(0);
            index = 0;
        }else {
            super.onBackPressed();
        }
    }
}