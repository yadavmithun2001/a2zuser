package com.cwservices.a2zproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cwservices.a2zproject.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Objects;

public class ChangeAddress extends AppCompatActivity {
    CardView register;
    EditText edithouseno,editfloorno,editstreetno,editwardno,editrouteno,pinocde;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    LinearLayout dialog;
    String verification_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);

        dialog = findViewById(R.id.ltprogress);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        register = findViewById(R.id.register);
        edithouseno = findViewById(R.id.houseno);
        editfloorno = findViewById(R.id.floorno);
        editstreetno = findViewById(R.id.streetno);
        editwardno = findViewById(R.id.wardno);
        editrouteno = findViewById(R.id.editrouteno);
        pinocde = findViewById(R.id.editpincode);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edithouseno.getText().toString().isEmpty()){
                    FancyToast.makeText(ChangeAddress.this,"House No can't be empty", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(editfloorno.getText().toString().isEmpty()){
                    FancyToast.makeText(ChangeAddress.this,"Floor No can't be empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(editstreetno.getText().toString().isEmpty()){
                    FancyToast.makeText(ChangeAddress.this,"Street No can't be empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                } else if(editwardno.getText().toString().isEmpty()){
                    FancyToast.makeText(ChangeAddress.this,"Ward No can't be empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(editrouteno.getText().toString().isEmpty()){
                    FancyToast.makeText(ChangeAddress.this,"Route No can't be empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(pinocde.getText().toString().isEmpty()){
                    FancyToast.makeText(ChangeAddress.this,"Pin Code can't be empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else {
                    sendrequest();
                }

            }
        });
    }
    void sendrequest(){
        dialog.setVisibility(View.VISIBLE);
        UserModel userModel = new UserModel("demo name",edithouseno.getText().toString(),editfloorno.getText().toString(),
                editstreetno.getText().toString(),"Janakpuri",editwardno.getText().toString(),
                editrouteno.getText().toString(),pinocde.getText().toString(),
                auth.getCurrentUser().getEmail(),
                "false");
        firebaseDatabase.getReference()
                .child("Requestforchange")
                .child(auth.getUid().toString())
                .setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    dialog.setVisibility(View.GONE);
                    FancyToast.makeText(ChangeAddress.this,"Your Request for address change successfully",Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                    Intent intent = new Intent(ChangeAddress.this,HomePage.class);
                    startActivity(intent);
                }else {
                    FancyToast.makeText(ChangeAddress.this,task.getException().getMessage(),Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }
            }
        });
    }
}