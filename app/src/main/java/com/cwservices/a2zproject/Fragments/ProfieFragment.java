package com.cwservices.a2zproject.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cwservices.a2zproject.ChangeAddress;
import com.cwservices.a2zproject.ChangePassword;
import com.cwservices.a2zproject.Login;
import com.cwservices.a2zproject.MainActivity;
import com.cwservices.a2zproject.Models.UserModel;
import com.cwservices.a2zproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.w3c.dom.Text;

import java.time.Duration;
import java.util.Objects;

public class ProfieFragment extends Fragment {
    FirebaseDatabase database;
    FirebaseAuth auth;
    UserModel userModel;
    ProgressBar pg;
    LinearLayout linearLayout;
    TextView changepassword,changename,savename;
    Dialog dialog;
    EditText name;
    CardView editpfimage;
    ImageView pfimage;
    CardView reqforchange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profie, container, false);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        pg = view.findViewById(R.id.progressBar2);
        linearLayout = view.findViewById(R.id.linearLayout);

        String phone = auth.getCurrentUser().getEmail().toString();
        String userid = phone.substring(0,phone.length()-10);


        database.getReference()
                .child("usersdata")
                .child(userid)
                .child("userinfo")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModel = snapshot.getValue(UserModel.class);
                        pg.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        FancyToast.makeText(getContext(),"SOMETHING WENT WRONG",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                    }
                });

        LinearLayout ltlogout = view.findViewById(R.id.ltlogout);
        ltlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                FancyToast.makeText(getContext(),"Logged out successfully", FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
                requireActivity().finish();

            }
        });

        TextView fullname = view.findViewById(R.id.fullname);
        TextView houseno = view.findViewById(R.id.houseno);
        TextView floorno = view.findViewById(R.id.floorno);
        TextView streetno = view.findViewById(R.id.streetno);
        TextView locality = view.findViewById(R.id.locality);
        TextView wardno = view.findViewById(R.id.wardno);
        TextView routeno = view.findViewById(R.id.routeno);
        TextView pincode = view.findViewById(R.id.pincode);
        TextView mobile = view.findViewById(R.id.mobile);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fullname.setText(userModel.getName());
                houseno.setText(userModel.getHouseno());
                floorno.setText(userModel.getFloorno());
                streetno.setText(userModel.getStreetno());
                locality.setText(userModel.getLocality());
                wardno.setText(userModel.getWardno());
                routeno.setText(userModel.getRouteno());
                pincode.setText(userModel.getPincode());
                mobile.setText(userModel.getMobilenumber());
            }
        },3000);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.change_name_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        savename = dialog.findViewById(R.id.savename);
        name = dialog.findViewById(R.id.editName);

        changepassword = view.findViewById(R.id.change);
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangePassword.class);
                startActivity(intent);
            }
        });
        changename = view.findViewById(R.id.changename);
        changename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        savename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty()){
                    userModel.setName(name.getText().toString());
                    database.getReference()
                            .child("usersdata")
                            .child(userid)
                            .child("userinfo")
                            .setValue(userModel);
                    FancyToast.makeText(getContext(),"Name changed Successfully", FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                    dialog.dismiss();
                    fullname.setText(name.getText().toString());
                }else {
                    Toast.makeText(getContext(), "Name can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editpfimage = view.findViewById(R.id.editpfimage);
        pfimage = view.findViewById(R.id.imageView5);
        editpfimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,45);
            }
        });

        reqforchange = view.findViewById(R.id.requestforchange);
        reqforchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ChangeAddress.class);
                startActivity(intent);
            }
        });


        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if(data.getData() != null){
                pfimage.setImageURI(data.getData());
            }
        }
    }
}