package com.cwservices.a2zproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.concurrent.TimeUnit;


public class LoginWithMobile extends AppCompatActivity {

    TextView loginwithpassword;
    TextView sendotp;
    LinearLayout dialog;
    EditText edit_mobile;
    String verification_id;
    CardView login;
    EditText et1,et2,et3,et4,et5,et6;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_mobile);

        loginwithpassword = findViewById(R.id.loginpassword_txt);
        loginwithpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginWithMobile.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        dialog = findViewById(R.id.ltprogress);
        edit_mobile = findViewById(R.id.editTextPhone);
        auth = FirebaseAuth.getInstance();



        sendotp = findViewById(R.id.textView3);
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_otp(edit_mobile.getText().toString());
            }
        });

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);
        et6 = findViewById(R.id.et6);
        otpnext();

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyotp(verification_id);
            }
        });




    }
    void send_otp(String mobile){
        if(mobile.equals("")){
            FancyToast.makeText(LoginWithMobile.this,"Phone Number Can't be Empty", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }else {
            if(mobile.length() != 10){
                FancyToast.makeText(LoginWithMobile.this,"Please Enter Valid Phone Number",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
            }else {
                dialog.setVisibility(View.VISIBLE);
                sendotp.setVisibility(View.INVISIBLE);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + mobile,
                        30,
                        TimeUnit.SECONDS,
                        LoginWithMobile.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                FancyToast.makeText(LoginWithMobile.this,"Code Sent Successfully",Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                FancyToast.makeText(LoginWithMobile.this,"Error! "+e.getMessage(),Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                                dialog.setVisibility(View.GONE);
                                sendotp.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verification_id = s;
                                dialog.setVisibility(View.GONE);
                            }
                        }
                );
            }

        }
    }
    void verifyotp(String verification_id){
        if(!et1.getText().toString().isEmpty() && !et2.getText().toString().isEmpty() && !et3.getText().toString().isEmpty() &&
                !et4.getText().toString().isEmpty() && !et5.getText().toString().isEmpty() && !et6.getText().toString().isEmpty()){
            String code = et1.getText().toString() + et2.getText().toString() + et3.getText().toString() + et4.getText().toString()
                    + et5.getText().toString() + et6.getText().toString();
            if(verification_id != null){
                dialog.setVisibility(View.VISIBLE);
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verification_id,code);
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseAuth.getInstance().signOut();
                            FancyToast.makeText(LoginWithMobile.this,"Verification Successful", Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                            _login(edit_mobile.getText().toString(),"123456");
                            Intent i = new Intent(LoginWithMobile.this,MainActivity.class);
                            startActivity(i);
                            finish();

                        }else {
                            dialog.setVisibility(View.INVISIBLE);
                            FancyToast.makeText(LoginWithMobile.this,"OTP Entered was Wrong",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                        }
                    }
                });

            }

        }else {
            FancyToast.makeText(LoginWithMobile.this,"OTP Cannot be Empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }

    }
    public void otpnext(){
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et1.getText().toString().isEmpty()){
                    et2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!et1.getText().toString().isEmpty()){
                    et2.requestFocus();
                }
            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et2.getText().toString().isEmpty()){
                    et3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!et2.getText().toString().isEmpty()){
                    et3.requestFocus();
                }else {
                    et1.requestFocus();
                }
            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et3.getText().toString().isEmpty()){
                    et4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!et3.getText().toString().isEmpty()){
                    et4.requestFocus();
                }else {
                    et2.requestFocus();
                }
            }
        });
        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et4.getText().toString().isEmpty()){
                    et5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!et4.getText().toString().isEmpty()){
                    et5.requestFocus();
                }else {
                    et3.requestFocus();
                }
            }
        });
        et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et5.getText().toString().isEmpty()){
                    et6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!et5.getText().toString().isEmpty()){
                    et6.requestFocus();
                }else {
                    et4.requestFocus();
                }
            }
        });
        et6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et6.getText().toString().isEmpty()){
                    et5.requestFocus();
                }
            }
        });
    }
    void _login(String userid,String password){
        dialog.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(userid+"@gmail.com",password)
                .addOnCompleteListener(LoginWithMobile.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FancyToast.makeText(LoginWithMobile.this,"Successfully logged in ", FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                            Intent intent = new Intent(LoginWithMobile.this,Watinglaunge.class);
                            dialog.setVisibility(View.GONE);
                            startActivity(intent);
                            finish();

                        }else {
                            FancyToast.makeText(LoginWithMobile.this,"Wrong Credentials or user not found", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                            dialog.setVisibility(View.GONE);
                        }
                    }
                });
    }
}