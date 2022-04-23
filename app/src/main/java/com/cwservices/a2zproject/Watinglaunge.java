package com.cwservices.a2zproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cwservices.a2zproject.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Watinglaunge extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    UserModel userModel;
    LinearLayout ltprogress;
    TextView message;
    CardView loginwithother;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watinglaunge);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        ltprogress = findViewById(R.id.ltprogress);
        message = findViewById(R.id.textView4);

        try {
            String phone = auth.getCurrentUser().getEmail().toString();
            String userid = phone.substring(0,phone.length()-10);
            database.getReference()
                        .child("usersdata")
                        .child(userid).child("userinfo")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                userModel = snapshot.getValue(UserModel.class);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                message.setText("You don't have access, Please wait while admin will give access to you ");
                                message.setTextColor(getResources().getColor(R.color.rColor));
                                FancyToast.makeText(Watinglaunge.this,"You Don't Have access",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                                ltprogress.setVisibility(View.GONE);
                            }
                        });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(userModel.getAccess().equals("true")){
                                Intent intent = new Intent(Watinglaunge.this,HomePage.class);
                                startActivity(intent);
                                finish();
                            }else {
                                message.setText("You don't have access, Please wait while admin will give access to you ");
                                message.setTextColor(getResources().getColor(R.color.rColor));
                                FancyToast.makeText(Watinglaunge.this,"You Don't Have access",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                                ltprogress.setVisibility(View.GONE);
                            }
                        }catch (Exception e){
                            message.setText("You don't have access, Please wait while admin will give access to you ");
                            message.setTextColor(getResources().getColor(R.color.rColor));
                            FancyToast.makeText(Watinglaunge.this,"You Don't Have access",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                            ltprogress.setVisibility(View.GONE);
                        }


                    }
                },3000);

        }catch (Exception e){
            message.setText("You don't have access, Please wait while admin will give access to you ");
            message.setTextColor(getResources().getColor(R.color.rColor));
            FancyToast.makeText(Watinglaunge.this,"You Don't Have access",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
            ltprogress.setVisibility(View.GONE);
        }

        loginwithother = findViewById(R.id.loginwithohter);
        loginwithother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                FancyToast.makeText(Watinglaunge.this,"Logged out successfully",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                Intent intent = new Intent(Watinglaunge.this,Login.class);
                startActivity(intent);
                finish();

            }
        });

    }
}