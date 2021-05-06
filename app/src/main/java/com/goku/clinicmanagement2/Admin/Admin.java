package com.goku.clinicmanagement2.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.goku.clinicmanagement2.CPU.FetchDoctors;
import com.goku.clinicmanagement2.CPU.FetchPatients;
import com.goku.clinicmanagement2.Credentials.SignIn;
import com.goku.clinicmanagement2.DoctorModel.MemberDoctor;
import com.goku.clinicmanagement2.PatientModel.PatientMember;
import com.goku.clinicmanagement2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Admin extends AppCompatActivity implements View.OnClickListener {

    TextView logout;

    Button doc, pat;

    FirebaseDatabase database1;
    DatabaseReference ref1;

    ArrayList<String> getDoctor= new ArrayList<>();

    ArrayList<String> getPatient= new ArrayList<>();

    String str="", str1="";

    FetchDoctors fd;

    FetchPatients fp;

    Map<String, Set<String>> a1= new LinkedHashMap<>();

    Map<String, Set<String>> a11= new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        fd= new FetchDoctors();

        fp= new FetchPatients();

        database1= FirebaseDatabase.getInstance();

        logout= (TextView) findViewById(R.id.logout_id);
        logout.setOnClickListener(this);

        doc= (Button) findViewById(R.id.doctor_id);
        pat= (Button) findViewById(R.id.patient_id);

        doc.setOnClickListener(this);
        pat.setOnClickListener(this);
    }

    private void fetchPatients() {
        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference().child("Patient");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                   // System.out.println("From fetchPatients() snapshot.getValue(): " + snapshot.getValue());
                    a1= fp.init(snapshot.getValue()+"");
                  //  System.out.println("From fetchPatients() a1 is: " + a1);

                    Intent sendData1= new Intent(Admin.this, ShowAllPatients.class);
                    sendData1.putExtra("mappatientdetails", (Serializable) a1);
                    startActivity(sendData1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchDoctors() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Doctor");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;         // cQualification
                            MemberDoctor addStudentDetailsData= new MemberDoctor((String) userData.get("accurateDetails"), (String) userData.get("UrlImage"), (String) userData.get("referenceId"));
                            getDoctor.add(addStudentDetailsData.getAccurateDetails());
                            getDoctor.add(addStudentDetailsData.getUrlImage());
                            getDoctor.add(addStudentDetailsData.getReferenceId());
                        }catch (ClassCastException cce){
                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                System.out.println("From fetchDoctor mString is: " + mString);
                                str+= mString + " ";
                            }catch (ClassCastException cce2){
                            }
                        }
                    }

                    if (!getDoctor.isEmpty()) {
                        System.out.println("From fetchDoctor getDoctor: " + getDoctor);
                    }
                    else {
                        if (str.contains("null,")) {
                            str= str.replace("null,", "");
                        }
                        if (str.contains("[")) {
                            str= str.replace("[","");
                        }
                        if (str.contains("]")) {
                            str= str.replace("]","");
                        }
                        System.out.println("From fetchDoctors() str: " + str);

                        a11= fd.init(str);

                        System.out.println("From fetchDoctors() a11 is: " + a11);

                        Intent sendData= new Intent(Admin.this, ShowAllDoctors.class);
                        sendData.putExtra("mapdoctordetails", (Serializable) a11);
                        startActivity(sendData);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

  /*  @Override
    protected void onStart() {
        super.onStart();
        fetchDoctors();
        fetchPatients();
    }   */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_id:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
                break;
            case R.id.doctor_id:
                fetchDoctors();
                break;
            case R.id.patient_id:
                fetchPatients();
                break;
        }
    }
}