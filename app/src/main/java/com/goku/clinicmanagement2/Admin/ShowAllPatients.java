package com.goku.clinicmanagement2.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.goku.clinicmanagement2.Adapter.ShowAllDoctorsAdapter;
import com.goku.clinicmanagement2.Adapter.ShowAllPatientsAdapter;
import com.goku.clinicmanagement2.DoctorModel.PatientDetailsForAdminData;
import com.goku.clinicmanagement2.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ShowAllPatients extends AppCompatActivity {

    Map<String, Set<String>> a1= new LinkedHashMap<>();
    ArrayList<PatientDetailsForAdminData> patientDetailsForAdminDataArrayList= new ArrayList<>();

    RecyclerView recyclerView1;

    ShowAllPatientsAdapter pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_patients);

        recyclerView1= (RecyclerView) findViewById(R.id.recycler_view_id);

        Intent getIntent= getIntent();
        a1= (Map<String, Set<String>>) getIntent.getSerializableExtra("mappatientdetails");

        System.out.println("From ShowAllPatients a1 is : " + a1);

        String name1="", mobile1="", imageurl1="", age1="", gender1="";
        for (Map.Entry<String, Set<String>> e1: a1.entrySet()) {
          //  System.out.println("From ShowAllPatients e1.getValue() is: " + e1.getValue());

            for (String s : e1.getValue()) {
                System.out.println("From ShowAllPatients e1.getValue() is: " + s);
                if (s.contains("name=") && s.contains("age=")) {
                    s= s.replace("name=","Name:\t").replace(s.substring(s.indexOf("age=")),"").replace(",","").trim();
                  //  s= s.substring(s.indexOf("name="),s.indexOf("age=")).replace("name=","Name:\t").trim();
                    name1= s;
                }
                if (s.contains("imageUrl=")) {
                    s= s.replace("imageUrl=", "").trim();
                    imageurl1= s;
                }
                if (s.contains("gender=")) {
                    s= s.replace("gender=", "Gender:\t").trim();
                    gender1= s;
                }
                if (s.contains("age=")) {
                  //  s= s.replace("age=","Mobile:\t").trim();
                    s= s.substring(s.indexOf("age=")).replace("age=","Mobile:\t");
                    age1= s;
                }
                if (s.contains("mobile=")) {
                    s= s.replace("mobile=","Age:\t");
                    mobile1= s;
                }
            }
            fillAllData(name1, imageurl1, gender1, age1, mobile1);
            name1="";
            imageurl1="";
            gender1="";
            age1="";
            mobile1="";

            LinearLayoutManager layoutManager= new LinearLayoutManager(ShowAllPatients.this, LinearLayoutManager.VERTICAL, false);

            recyclerView1.setLayoutManager(layoutManager);

            recyclerView1.setItemAnimator(new DefaultItemAnimator());

            pd= new ShowAllPatientsAdapter(ShowAllPatients.this, patientDetailsForAdminDataArrayList);

            recyclerView1.setAdapter(pd);
        }
    }

    private void fillAllData(String name1, String imageurl1, String gender1, String age1, String mobile1) {
        PatientDetailsForAdminData pd= new PatientDetailsForAdminData(name1, imageurl1, gender1, age1, mobile1);
        patientDetailsForAdminDataArrayList.add(pd);
    }
}