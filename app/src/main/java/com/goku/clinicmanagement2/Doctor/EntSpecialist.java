package com.goku.clinicmanagement2.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.goku.clinicmanagement2.Adapter.HelpAdapter;
import com.goku.clinicmanagement2.CPU.Cardiologist_Formatter;
import com.goku.clinicmanagement2.CPU.Formatter;
import com.goku.clinicmanagement2.CPU.Recogniser;
import com.goku.clinicmanagement2.DoctorModel.FetchCardiologistData;
import com.goku.clinicmanagement2.Patient.Dashboard_Patient;
import com.goku.clinicmanagement2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class EntSpecialist extends AppCompatActivity {

    ArrayList<FetchCardiologistData> fcdList;

    RecyclerView recyclerView;

    HelpAdapter helpAdapter, hdp;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference ref, childRef;

    Formatter cardiologistFormatter;

    Recogniser rs;

    Cardiologist_Formatter cdf;

    String Fees, Mobile, Name, Schedule, ImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ent_specialist);

        cardiologistFormatter= new Formatter();

        fcdList= new ArrayList<>();

        cdf= new Cardiologist_Formatter();

        rs= new Recogniser();

        recyclerView= (RecyclerView) findViewById(R.id.ent_specialist_recyclerview_id);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();

        ref= firebaseDatabase.getReference("Doctor");

       // childRef= ref.child(" ENT Specialist");

        getCardiologistData();
    }

    private void getCardiologistData() {
        ref.addValueEventListener(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getChildrenCount() < 1) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(EntSpecialist.this);
                    alertBuilder.setTitle("Service not yet started or service discontinued");
                    alertBuilder.setMessage("Sorry dear user!\n\nBut no doctors of this profile has been found!");
                    alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alertBuilder.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(EntSpecialist.this, Dashboard_Patient.class));
                            finish();
                        }
                    });
                    AlertDialog a1 = alertBuilder.create();
                    a1.show();
                } else {

                    String key = snapshot.getKey() + "";
                    String value = snapshot.getValue() + "";
                    System.out.println("key is: " + key);
                    System.out.println("value is: " + value);

                    Set<String> getData1 = rs.init(value, "ENT Specialist");


                    System.out.println("getCardiologistData() rs.init: " + getData1);

                    if (getData1.isEmpty()) {
                        AlertDialog.Builder showEmptyStatus = new AlertDialog.Builder(EntSpecialist.this);
                        showEmptyStatus.setTitle("Doctors are not available");
                        showEmptyStatus.setMessage("Sorry dear user but the doctors of this specific profile \n\nis not available right now in this app\n\n");
                        showEmptyStatus.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        showEmptyStatus.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(getApplicationContext(), Dashboard_Patient.class));
                             //   finish();
                            }
                        });

                        AlertDialog a1 = showEmptyStatus.create();
                        a1.show();
                    } else {

                        for (String s1 : getData1) {
                            childRef = ref.child(s1);

                            System.out.println("From the getCardiologistData() s1: getData1 " + s1);

                            childRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    System.out.println("From the getCardiologistData() snapshot:  " + snapshot);

                                    ArrayList<ArrayList<String>> getData = cdf.init(String.valueOf(snapshot.getValue()));

                                    ArrayList<String> scheduleList = getData.get(0);
                                    ArrayList<String> NameList = getData.get(1);
                                    ArrayList<String> MobileList = getData.get(2);
                                    ArrayList<String> FeesList = getData.get(3);
                                    ArrayList<String> UrlImage = getData.get(4);

                                    System.out.println("ScheduleList: " + scheduleList);
                                    System.out.println("NameList: " + NameList);
                                    System.out.println("MobileList: " + MobileList);
                                    System.out.println("FeesList: " + FeesList);
                                    System.out.println("UrlImage: " + UrlImage);


                                    System.out.println("From getCardiologistData() getData: " + getData);

                                    FetchCardiologistData fd = new FetchCardiologistData();

                                    for (String name : NameList) {
                                        fd.setName(name);
                                    }
                                    for (String mobile : MobileList) {
                                        fd.setMobile(mobile);
                                    }
                                    for (String fees : FeesList) {
                                        fd.setFees(fees);
                                    }
                                    for (String schedule : scheduleList) {
                                        fd.setSchedule(schedule);
                                    }
                                    for (String urlImage : UrlImage) {
                                        fd.setImageUrl(urlImage);
                                    }

                                    fcdList.add(fd);

                                    helpAdapter = new HelpAdapter(fcdList, getApplicationContext());

                                    recyclerView.setAdapter(helpAdapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }






         /*   @Override
         /*   public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getChildrenCount() < 1) {
                    AlertDialog.Builder alertBuilder= new AlertDialog.Builder(EntSpecialist.this);
                    alertBuilder.setTitle("Service not yet started or service discontinued");
                    alertBuilder.setMessage("Sorry dear user!\n\nBut no doctors of this profile has been found!");
                    alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alertBuilder.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(EntSpecialist.this, Dashboard_Patient.class));
                            finish();
                        }
                    });
                    AlertDialog a1= alertBuilder.create();
                    a1.show();
                }



              /*  else {


                    String key= snapshot.getKey() + "";
                    String value= snapshot.getValue() + "";
                    System.out.println("key is: " + key);
                    System.out.println("value is: " + value);
                    Set<String> scheduleAndImageUrl= cardiologistFormatter.init(value);
                    for (String s: scheduleAndImageUrl) {
                        String splitter[] = s.split("\\s+|\n");
                        for (String s1 : splitter) {
                            System.out.println("From getCardiologistData(): " + s1);
                        }
                        Fees = splitter[splitter.length - 1];
                        Mobile = splitter[splitter.length - 2];
                        Name = splitter[splitter.length - 3];
                        ImageUrl = splitter[splitter.length - 4];
                        splitter[splitter.length - 1] = splitter[splitter.length - 1].replace(splitter[splitter.length - 1], "");
                        splitter[splitter.length - 2] = splitter[splitter.length - 2].replace(splitter[splitter.length - 2], "");
                        splitter[splitter.length - 3] = splitter[splitter.length - 3].replace(splitter[splitter.length - 3], "");
                        splitter[splitter.length - 4] = splitter[splitter.length - 4].replace(splitter[splitter.length - 4], "");
                        ArrayList<String> list1 = new ArrayList<>();
                        for (String s1 : splitter) {
                            if (!s1.isEmpty()) {
                                System.out.println("s1: " + s1);
                                list1.add(s1);
                            }
                        }
                        String sendJoin[] = new String[list1.size()];
                        for (int i = 0; i < sendJoin.length; i++) {
                            sendJoin[i] = list1.get(i);
                        }
                        String receivedJoin = cardiologistFormatter.slidingWindow(sendJoin);
                        System.out.println("From getCardiologistData() receivedJoin: " + receivedJoin);
                        Schedule = receivedJoin;
                        ImageUrl = ImageUrl.replace("urlImage=", "").trim();
                        System.out.println("Name : " + Name + " Mobile: " + Mobile + " Fees: " + Fees + " Schedule: " + receivedJoin + " ImageUrl: " + ImageUrl);

                        FetchCardiologistData fd = new FetchCardiologistData(ImageUrl, Name, Mobile, Fees, Schedule, "ENT Specialist");
                        fd.setName(Name);
                        fd.setMobile(Mobile);
                        fd.setFees(Fees);
                        fd.setSchedule(Schedule);
                        fd.setImageUrl(ImageUrl);

                        fcdList.add(fd);

                        helpAdapter = new HelpAdapter(fcdList, getApplicationContext());

                        recyclerView.setAdapter(helpAdapter);
                    }

                }  */
            });

         /*   @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }  */
       // });
    }
}