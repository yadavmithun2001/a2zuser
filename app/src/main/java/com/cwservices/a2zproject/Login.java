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

import com.cwservices.a2zproject.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;


public class Login extends AppCompatActivity {

    TextView txt_register,loginwithmobile,txt_forgotpassword;
    CardView login;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    LinearLayout ltprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            Intent intent = new Intent(Login.this,Watinglaunge.class);
            startActivity(intent);
            finish();
        }

        txt_register = findViewById(R.id.txt_register);
        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Registration.class);
                startActivity(intent);
            }
        });
        loginwithmobile = findViewById(R.id.txt_loginwithbole);
        loginwithmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,LoginWithMobile.class);
                startActivity(intent);
            }
        });
        txt_forgotpassword = findViewById(R.id.txt_forgotpassword);
        txt_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });

        EditText editUserid = findViewById(R.id.editTextUserID);
        EditText editPassword = findViewById(R.id.editTextPassword);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ltprogress = findViewById(R.id.ltprogress);


        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editUserid.getText().toString().isEmpty()){
                    FancyToast.makeText(Login.this,"User can't be Empty", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(editUserid.getText().toString().length() < 10){
                    FancyToast.makeText(Login.this,"Please enter a valid USER ID", FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                }
                else if(editPassword.getText().toString().isEmpty()){
                    FancyToast.makeText(Login.this,"Password can't be Empty", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(editPassword.getText().toString().length() < 6){
                    FancyToast.makeText(Login.this,"Password should be 6 characters length ", FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                }else {
                    _login(editUserid.getText().toString(),editPassword.getText().toString());
                }
            }
        });
    }

    void _login(String userid,String password){
        ltprogress.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(userid+"@gmail.com",password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FancyToast.makeText(Login.this,"Successfully logged in ", FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                            Intent intent = new Intent(Login.this,Watinglaunge.class);
                            ltprogress.setVisibility(View.GONE);
                            startActivity(intent);
                            finish();

                        }else {
                            FancyToast.makeText(Login.this,"Wrong Credentials or user not found", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                            ltprogress.setVisibility(View.GONE);
                        }
                    }
                });
    }

}