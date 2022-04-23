package com.cwservices.a2zproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shashank.sony.fancytoastlib.FancyToast;


public class ChangePassword extends AppCompatActivity {

    EditText editPassword,editcnfPassword;
    FirebaseAuth auth;
    FirebaseUser user;
    CardView changepassword;
    TextView register;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        editPassword = findViewById(R.id.editTextPassword);
        editcnfPassword = findViewById(R.id.editTextCnfPassword);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        email = user.getEmail();

        changepassword = findViewById(R.id.changepassword);
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editPassword.getText().toString().isEmpty()){
                    FancyToast.makeText(ChangePassword.this,"Password can't be Empty", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(editPassword.getText().toString().isEmpty()){
                    FancyToast.makeText(ChangePassword.this,"Confirm Password can't be Empty", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(!editcnfPassword.getText().toString().equals(editPassword.getText().toString())){
                    FancyToast.makeText(ChangePassword.this,"Password and Confirm Password not matched", FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                }else {
                    AuthCredential authCredential = EmailAuthProvider.getCredential(email,"123456");
                    user.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FancyToast.makeText(ChangePassword.this,"Password Changed Successfully", FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                                Intent intent = new Intent(ChangePassword.this,HomePage.class);
                                startActivity(intent);
                            }else {
                                FancyToast.makeText(ChangePassword.this,task.getException().getMessage(), FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                            }
                        }
                    });
                }

            }
        });





    }
}