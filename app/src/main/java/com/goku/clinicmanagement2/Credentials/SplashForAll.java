package com.goku.clinicmanagement2.Credentials;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.goku.clinicmanagement2.Doctor.Doctor_profile;
import com.goku.clinicmanagement2.Patient.Patient_Profile;
import com.goku.clinicmanagement2.R;

public class SplashForAll extends AppCompatActivity {

    ImageView bus;
    SwitchCompat switch1, switch11;
    TextView res;
    String UserStatus="";

    UserStatus us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_for_all);

        us= new UserStatus(SplashForAll.this);

        grantAllPermisions();

        bus= (ImageView) findViewById(R.id.vehicle_id);
        switch1= (SwitchCompat) findViewById(R.id.doctor_or_patient_id);
        switch11= (SwitchCompat) findViewById(R.id.patient_or_doctor_id);
        res= (TextView) findViewById(R.id.doctor_or_patient_textview_id);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    res.setText("User is a Doctor!");
                    UserStatus = "User is a Doctor!";
                } else {
                    res.setText("User is a Patient!");
                    UserStatus = "User is a Patient!";
                }

                us.insertData(UserStatus);

                startActivity(new Intent(getApplicationContext(), Doctor_profile.class));
                finish();
            }
        });

        switch11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    res.setText("User is a Patient!");
                    UserStatus = "User is a Patient!";
                } else {
                    res.setText("User is a Doctor!");
                    UserStatus = "User is a Doctor!";
                }

                us.insertData(UserStatus);

                startActivity(new Intent(getApplicationContext(), Patient_Profile.class));
                finish();
            }
        });

        moveAnimation();

    }

    // });






    private void grantAllPermisions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(SplashForAll.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        }
    }

    public void moveAnimation() {
        Animation anim= new TranslateAnimation(0,500,0,0);
        anim.setDuration(4000);
        anim.setFillAfter(true);
        bus.startAnimation(anim);
        bus.setVisibility(View.INVISIBLE);
    }
}