package com.goku.clinicmanagement2.Patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.goku.clinicmanagement2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class CollapsingToolbar extends AppCompatActivity {

    Toolbar toolbar;

    TextView name, fees, mobile, schedule;

    ImageView placeholderImage;

    FloatingActionButton book;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing_toolbar);

        toolbar = (Toolbar) findViewById(R.id.collapsing_toolbar_toolbar_id);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        name = (TextView) findViewById(R.id.collapsing_toolbar_name_id);

        fees = (TextView) findViewById(R.id.collapsing_toolbar_fees_id);

        mobile = (TextView) findViewById(R.id.collapsing_toolbar_mobile_id);

        schedule = (TextView) findViewById(R.id.collapsing_toolbar_schedule_id);

        placeholderImage = (ImageView) findViewById(R.id.collapsing_toolbar_imageview_id);

        book = (FloatingActionButton) findViewById(R.id.collapsing_toolbar_book_id);

        Intent receiveData = getIntent();

        final String Name = receiveData.getStringExtra("name");
        final String Fees = receiveData.getStringExtra("fees");
        final String Mobile = receiveData.getStringExtra("mobile");
        final String Schedule = receiveData.getStringExtra("schedule");
        final String ImageUrl = receiveData.getStringExtra("imageurl");
        final String Specialization = receiveData.getStringExtra("specialization");

        String schedules[] = Schedule.split("\\  +");

        for (String s : schedules) {
            System.out.println("From CollapsingToolbar schedules: " + s);
        }

        Glide.with(CollapsingToolbar.this).load(ImageUrl).centerCrop().into(placeholderImage);

        name.setText("Name: \t" + Name);
        fees.setText("Fees: \t" + Fees);
        mobile.setText("Mobile: \t" + Mobile);
        schedule.setText("Schedule: \t" + Schedule);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder gotoActivity = new AlertDialog.Builder(CollapsingToolbar.this);
                gotoActivity.setTitle("Book a doctor's appointment");
                gotoActivity.setMessage("Are you sure that you want \nto book doctor " + Name + " ?");
                gotoActivity.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent sendData = new Intent(CollapsingToolbar.this, EventBooking.class);
                        sendData.putExtra("specialization", Specialization);
                        sendData.putExtra("name", Name);
                        sendData.putExtra("fees", Fees);
                        sendData.putExtra("schedule", Schedule);
                        sendData.putExtra("mobile", Mobile);
                        sendData.putExtra("imageurl", ImageUrl);

                        startActivity(sendData);
                    }
                });
                AlertDialog a1 = gotoActivity.create();
                a1.show();
            }
        });

    }
}