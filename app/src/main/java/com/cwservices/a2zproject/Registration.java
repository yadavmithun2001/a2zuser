package com.cwservices.a2zproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cwservices.a2zproject.Models.ComplaintModel;
import com.cwservices.a2zproject.Models.NotificationModel;
import com.cwservices.a2zproject.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.concurrent.TimeUnit;


public class Registration extends AppCompatActivity {
    TextView txt_login,sendotp;
    CardView register;
    EditText editPhone,edithouseno,editfloorno,editstreetno,editwardno,editrouteno,editpassword,confirmpassword,pinocde;
    EditText et1,et2,et3,et4,et5,et6;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    LinearLayout dialog;
    String verification_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        txt_login = findViewById(R.id.txt_login);
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this,Login.class);
                startActivity(intent);
            }
        });

        dialog = findViewById(R.id.ltprogress);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);
        et6 = findViewById(R.id.et6);
        otpnext();

        register = findViewById(R.id.register);


        editPhone = findViewById(R.id.editTextPhone);

        sendotp = findViewById(R.id.sendotp);
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_otp(editPhone.getText().toString());
            }
        });

        edithouseno = findViewById(R.id.houseno);
        editfloorno = findViewById(R.id.floorno);
        editstreetno = findViewById(R.id.streetno);
        editwardno = findViewById(R.id.wardno);
        editrouteno = findViewById(R.id.editrouteno);
        pinocde = findViewById(R.id.editpincode);
        editpassword = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.cnfpassword);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edithouseno.getText().toString().isEmpty()){
                    FancyToast.makeText(Registration.this,"House No can't be empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(editfloorno.getText().toString().isEmpty()){
                    FancyToast.makeText(Registration.this,"Floor No can't be empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(editstreetno.getText().toString().isEmpty()){
                    FancyToast.makeText(Registration.this,"Street No can't be empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                } else if(editwardno.getText().toString().isEmpty()){
                    FancyToast.makeText(Registration.this,"Ward No can't be empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(editrouteno.getText().toString().isEmpty()){
                    FancyToast.makeText(Registration.this,"Route No can't be empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(pinocde.getText().toString().isEmpty()){
                    FancyToast.makeText(Registration.this,"Pin Code can't be empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(editpassword.getText().toString().isEmpty()){
                    FancyToast.makeText(Registration.this,"Password can't be empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(confirmpassword.getText().toString().isEmpty()){
                    FancyToast.makeText(Registration.this,"Confirm Password can't be empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(!confirmpassword.getText().toString().equals(editpassword.getText().toString())){
                    FancyToast.makeText(Registration.this,"Password not matched",Toast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                }else {
                    verifyotp(verification_id);
                }

            }
        });


    }

    void send_otp(String mobile){
        if(mobile.equals("")){
            FancyToast.makeText(Registration.this,"Phone Number Can't be Empty", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }else {
            if(mobile.length() != 10){
                FancyToast.makeText(Registration.this,"Please Enter Valid Phone Number",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
            }else {
                dialog.setVisibility(View.VISIBLE);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + mobile,
                        30,
                        TimeUnit.SECONDS,
                        Registration.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                FancyToast.makeText(Registration.this,"Code Sent Successfully",Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                FancyToast.makeText(Registration.this,"Error! "+e.getMessage(),Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                                dialog.setVisibility(View.GONE);
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
                            FancyToast.makeText(Registration.this,"Verification Successful", Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                            _login(editPhone.getText().toString(),editpassword.getText().toString());
                        }else {
                            dialog.setVisibility(View.GONE);
                            FancyToast.makeText(Registration.this,"OTP Entered was Wrong",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                        }
                    }
                });
            }else {
                FancyToast.makeText(Registration.this,"Something went wrong",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                dialog.setVisibility(View.GONE);
            }

        }else {
            FancyToast.makeText(Registration.this,"OTP Cannot be Empty",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
            dialog.setVisibility(View.GONE);
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
        auth.createUserWithEmailAndPassword(userid+"@gmail.com",password)
                .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UserModel userModel = new UserModel("demo name",edithouseno.getText().toString(),editfloorno.getText().toString(),
                                    editstreetno.getText().toString(),"Janakpuri",editwardno.getText().toString(),
                                    editrouteno.getText().toString(),pinocde.getText().toString(),
                                    editPhone.getText().toString(),
                                    "false");

                            NotificationModel notificationModel = new NotificationModel("14-04-2022","No notifications");
                            firebaseDatabase.getReference()
                                    .child("usersdata")
                                    .child(editPhone.getText().toString())
                                    .child("notifications")
                                    .setValue(notificationModel);

                            ComplaintModel complaintModel = new ComplaintModel("14-04-2022","No Complaints","None");
                            firebaseDatabase.getReference()
                                    .child("usersdata")
                                    .child(editPhone.getText().toString())
                                    .child("complaints")
                                    .setValue(complaintModel);

                            firebaseDatabase.getReference()
                                    .child("usersdata")
                                    .child(editPhone.getText().toString())
                                    .child("userinfo")
                                    .setValue(userModel)
                                    .addOnCompleteListener(Registration.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            FancyToast.makeText(Registration.this,"User Registered Successfully",Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                                            Intent intent = new Intent(Registration.this,Login.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                        }else {
                            FancyToast.makeText(Registration.this,task.getException().toString(),Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                            dialog.setVisibility(View.GONE);
                        }
                    }
                });
    }

}