package com.goku.clinicmanagement2.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.goku.clinicmanagement2.Adapter.ShowAllDoctorsAdapter;
import com.goku.clinicmanagement2.DoctorModel.DoctorDetailsForAdminData;
import com.goku.clinicmanagement2.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ShowAllDoctors extends AppCompatActivity {

    Map<String, Set<String>> a11= new LinkedHashMap<>();

    Set<String> fetchDoctorDetails= new LinkedHashSet<>();

    ArrayList<DoctorDetailsForAdminData> doctorDetailsForAdminData= new ArrayList<>();

    RecyclerView recyclerView1;

    ShowAllDoctorsAdapter sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_doctors);
        recyclerView1= (RecyclerView) findViewById(R.id.recycler_view_id);
        Intent getData = getIntent();
        a11 = (Map<String, Set<String>>) getData.getSerializableExtra("mapdoctordetails");
        System.out.println("From ShowAllDoctors a11 is: " + a11);
        String name="", mobile="", fees="", urlimage="", schedule="";
        for (Map.Entry<String, Set<String>> e1 : a11.entrySet()) {
            for (String s : e1.getValue()) {
                System.out.println("From ShowAllDoctors e1.getValue() is: " + s);
                if (s.contains("referenceId=")) {
                    s= s.replace("referenceId=","");
                    String str[]= s.split("\\s+");
                    fees= str[str.length-1];
                    mobile= str[str.length-2];
                    for (int i=0;i<str.length-2;i++) {
                        name+= str[i] + " ";
                    }
                }
                if (s.contains("urlImage=")) {
                    s= s.replace("urlImage=", "").trim();
                    urlimage= s;
                }
                if (s.contains("Schedule=")) {
                    s= s.replace("Schedule=", "").trim();
                    schedule= s;
                }
            }
            fillAllData("Name:\t"+name, "Fees:\t"+ fees, "Mobile:\t"+ mobile, urlimage, "Schedules:\t" + schedule);
            name="";
            fees="";
            mobile="";

            LinearLayoutManager layoutManager= new LinearLayoutManager(ShowAllDoctors.this, LinearLayoutManager.VERTICAL, false);

            recyclerView1.setLayoutManager(layoutManager);

            recyclerView1.setItemAnimator(new DefaultItemAnimator());

            sd= new ShowAllDoctorsAdapter(ShowAllDoctors.this, doctorDetailsForAdminData);

            recyclerView1.setAdapter(sd);

          //  displayCorrespondingJobAdapter= new DisplayCorrespondingJobAdapter(DisplayCorrespondingJob.this, displayCorrespondingJobData1);

            // jobAdvertisementAdapter  = new JobAdvertisementAdapter(DisplayCorrespondingJob.this, jobAdvertisementdata, receiveData);

         //   recyclerView1.setAdapter(displayCorrespondingJobAdapter);
        }
    }

    private void fillAllData(String name, String fees, String mobile, String urlimage, String schedule) {
        DoctorDetailsForAdminData dm= new DoctorDetailsForAdminData(name, fees, urlimage, mobile, schedule);
        doctorDetailsForAdminData.add(dm);
    }
}