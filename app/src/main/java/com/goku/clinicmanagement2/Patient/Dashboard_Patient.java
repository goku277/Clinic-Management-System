package com.goku.clinicmanagement2.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.goku.clinicmanagement2.Doctor.Cardiologist;
import com.goku.clinicmanagement2.Doctor.ChildSpecialist;
import com.goku.clinicmanagement2.Doctor.Chiropractor;
import com.goku.clinicmanagement2.Doctor.Dentist;
import com.goku.clinicmanagement2.Doctor.Dermatologist;
import com.goku.clinicmanagement2.Doctor.EntSpecialist;
import com.goku.clinicmanagement2.Doctor.Gynaecologist;
import com.goku.clinicmanagement2.Doctor.Herbalist;
import com.goku.clinicmanagement2.Doctor.Optometrist;
import com.goku.clinicmanagement2.Doctor.Paramedic;
import com.goku.clinicmanagement2.Doctor.Psychiatrist;
import com.goku.clinicmanagement2.Doctor.Surgeon;
import com.goku.clinicmanagement2.Doctor.Urologist;
import com.goku.clinicmanagement2.Doctor.Veterenian;
import com.goku.clinicmanagement2.R;

public class Dashboard_Patient extends AppCompatActivity implements View.OnClickListener {

    ImageView cardiologist_imageview, psychiatrist_imageview, dentist_imageview,
            surgeon_imageview, ent_specialist_imageview, child_specialist_imageview,
            urologist_imageview, veterinarian_imageview, optometrist_imageview, chiropractor_imageview,
            dermatologist_imageview, gynaecologist_imageview, herbalist_imageview, paramedic_imageview;
    TextView cardiologist_textview, psychiatrist_textview, dentist_textview, dermatologist_textview, herbalist_textview,
            surgeon_textview, ent_specialist_textview, child_specialist_textview, gynaecologist_textview, paramedic_textview,
            urologist_textview, veterinarian_textview, optometrist_textview, chiropractor_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard__patient);

        cardiologist_imageview= (ImageView) findViewById(R.id.cardiologist_imageview_id);
        cardiologist_textview= (TextView) findViewById(R.id.cardiologist_textview_id);

        psychiatrist_imageview= (ImageView) findViewById(R.id.psychiatrist_imageview_id);
        psychiatrist_textview= (TextView) findViewById(R.id.psychiatrist_textview_id);

        dentist_imageview= (ImageView) findViewById(R.id.dentist_imageview_id);
        dentist_textview= (TextView) findViewById(R.id.dentist_textview_id);

        dentist_imageview.setOnClickListener(this);
        dentist_textview.setOnClickListener(this);

        surgeon_imageview= (ImageView) findViewById(R.id.surgeon_imageview_id);
        surgeon_textview= (TextView) findViewById(R.id.surgeon_textview_id);

        surgeon_imageview.setOnClickListener(this);
        surgeon_textview.setOnClickListener(this);

        ent_specialist_imageview= (ImageView) findViewById(R.id.ent_specialist_imageview_id);
        ent_specialist_textview= (TextView) findViewById(R.id.ent_specialist_textview_id);

        ent_specialist_imageview.setOnClickListener(this);
        ent_specialist_textview.setOnClickListener(this);

        child_specialist_imageview= (ImageView) findViewById(R.id.child_specialist_imageview_id);
        child_specialist_textview= (TextView) findViewById(R.id.child_specialist_textview_id);

        child_specialist_imageview.setOnClickListener(this);
        child_specialist_textview.setOnClickListener(this);

        urologist_imageview= (ImageView) findViewById(R.id.urologist_imageview_id);
        urologist_textview= (TextView) findViewById(R.id.urologist_textview_id);

        urologist_imageview.setOnClickListener(this);
        urologist_textview.setOnClickListener(this);

        veterinarian_imageview= (ImageView) findViewById(R.id.veterinarian_imageview_id);
        veterinarian_textview= (TextView) findViewById(R.id.vetarenean_textview_id);

        veterinarian_imageview.setOnClickListener(this);
        veterinarian_textview.setOnClickListener(this);

        optometrist_imageview= (ImageView) findViewById(R.id.optometrist_imageview_id);
        optometrist_textview= (TextView) findViewById(R.id.optometrist_textview_id);

        optometrist_imageview.setOnClickListener(this);
        optometrist_textview.setOnClickListener(this);

        chiropractor_imageview= (ImageView) findViewById(R.id.chiropractor_imageview_id);
        chiropractor_textview= (TextView) findViewById(R.id.chiropractor_textview_id);

        chiropractor_imageview.setOnClickListener(this);
        chiropractor_textview.setOnClickListener(this);

        dermatologist_imageview= (ImageView) findViewById(R.id.dermatologist_imageview_id);
        dermatologist_textview= (TextView) findViewById(R.id.dermatologost_textview_id);

        dermatologist_imageview.setOnClickListener(this);
        dermatologist_textview.setOnClickListener(this);

        gynaecologist_imageview= (ImageView) findViewById(R.id.gynaecologist_imageview_id);
        gynaecologist_textview= (TextView) findViewById(R.id.gynaecologist_textview_id);

        gynaecologist_imageview.setOnClickListener(this);
        gynaecologist_textview.setOnClickListener(this);

        herbalist_imageview= (ImageView) findViewById(R.id.herbalist_imageview_id);
        herbalist_textview= (TextView) findViewById(R.id.herbalist_textview_id);

        herbalist_imageview.setOnClickListener(this);
        herbalist_textview.setOnClickListener(this);

        paramedic_imageview= (ImageView) findViewById(R.id.paramedic_imageview_id);
        paramedic_textview= (TextView) findViewById(R.id.paramedic_textview_id);

        paramedic_imageview.setOnClickListener(this);
        paramedic_textview.setOnClickListener(this);

        cardiologist_textview.setOnClickListener(this);
        cardiologist_imageview.setOnClickListener(this);

        psychiatrist_textview.setOnClickListener(this);
        psychiatrist_imageview.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardiologist_imageview_id:
                startActivity(new Intent(getApplicationContext(), Cardiologist.class));
                break;
            case R.id.cardiologist_textview_id:
                startActivity(new Intent(getApplicationContext(), Cardiologist.class));
                break;
            case R.id.psychiatrist_imageview_id:
                startActivity(new Intent(getApplicationContext(), Psychiatrist.class));
                break;
            case R.id.psychiatrist_textview_id:
                startActivity(new Intent(getApplicationContext(), Psychiatrist.class));
                break;
            case R.id.dentist_imageview_id:
                startActivity(new Intent(getApplicationContext(), Dentist.class));
                break;
            case R.id.dentist_textview_id:
                startActivity(new Intent(getApplicationContext(), Dentist.class));
                break;
            case R.id.surgeon_imageview_id:
                startActivity(new Intent(getApplicationContext(), Surgeon.class));
                break;
            case R.id.surgeon_textview_id:
                startActivity(new Intent(getApplicationContext(), Surgeon.class));
                break;
            case R.id.ent_specialist_imageview_id:
                startActivity(new Intent(getApplicationContext(), EntSpecialist.class));
                break;
            case R.id.ent_specialist_textview_id:
                startActivity(new Intent(getApplicationContext(), EntSpecialist.class));
                break;
            case R.id.child_specialist_imageview_id:
                startActivity(new Intent(getApplicationContext(), ChildSpecialist.class));
                break;
            case R.id.child_specialist_textview_id:
                startActivity(new Intent(getApplicationContext(), ChildSpecialist.class));
                break;
            case R.id.urologist_imageview_id:
                startActivity(new Intent(getApplicationContext(), Urologist.class));
                break;
            case R.id.urologist_textview_id:
                startActivity(new Intent(getApplicationContext(), Urologist.class));
                break;
            case R.id.veterinarian_imageview_id:
                startActivity(new Intent(getApplicationContext(), Veterenian.class));
                break;
            case R.id.vetarenean_textview_id:
                startActivity(new Intent(getApplicationContext(), Veterenian.class));
                break;
            case R.id.optometrist_imageview_id:
                startActivity(new Intent(getApplicationContext(), Optometrist.class));
                break;
            case R.id.optometrist_textview_id:
                startActivity(new Intent(getApplicationContext(), Optometrist.class));
                break;
            case R.id.chiropractor_imageview_id:
                startActivity(new Intent(getApplicationContext(), Chiropractor.class));
                break;
            case R.id.chiropractor_textview_id:
                startActivity(new Intent(getApplicationContext(), Chiropractor.class));
                break;
            case R.id.dermatologist_imageview_id:
                startActivity(new Intent(getApplicationContext(), Dermatologist.class));
                break;
            case R.id.dermatologost_textview_id:
                startActivity(new Intent(getApplicationContext(), Dermatologist.class));
                break;
            case R.id.gynaecologist_imageview_id:
                startActivity(new Intent(getApplicationContext(), Gynaecologist.class));
                break;
            case R.id.gynaecologist_textview_id:
                startActivity(new Intent(getApplicationContext(), Herbalist.class));
                break;
            case R.id.herbalist_imageview_id:
                startActivity(new Intent(getApplicationContext(), Herbalist.class));
                break;
            case R.id.paramedic_imageview_id:
                startActivity(new Intent(getApplicationContext(), Paramedic.class));
                break;
            case R.id.paramedic_textview_id:
                startActivity(new Intent(getApplicationContext(), Paramedic.class));
                break;
        }
    }
}