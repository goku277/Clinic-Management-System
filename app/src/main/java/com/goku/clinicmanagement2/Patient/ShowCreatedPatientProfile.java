package com.goku.clinicmanagement2.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.goku.clinicmanagement2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowCreatedPatientProfile extends AppCompatActivity {

    CircleImageView cig;
    TextView name, age, gender, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_created_patient_profile);

        cig= (CircleImageView) findViewById(R.id.show_created_patient_profile_logo);
        name= (TextView) findViewById(R.id.show_created_patient_profile_name);
        age= (TextView) findViewById(R.id.show_created_patient_profile_age);
        gender= (TextView) findViewById(R.id.show_created_patient_profile_gender);
        mobile= (TextView) findViewById(R.id.show_created_patient_profile_moible);

        Intent receiveData= getIntent();

        String mobile1= receiveData.getStringExtra("age");

        name.setText("Name: \t"+receiveData.getStringExtra("name"));
        age.setText("Mobile: \t"+ mobile1);
        gender.setText("Gender: \t"+receiveData.getStringExtra("gender"));
        mobile.setText("Age: \t"+receiveData.getStringExtra("mobile"));

        String imageurl= receiveData.getStringExtra("imageurl");

        System.out.println("mobile1: " + mobile1);

        Glide.with(ShowCreatedPatientProfile.this).load(imageurl).centerCrop().into(cig);
    }
}