package com.goku.clinicmanagement2.Doctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.RequestBuilder;
import com.goku.clinicmanagement2.CPU.Time_1;
import com.goku.clinicmanagement2.Credentials.SignIn;
import com.goku.clinicmanagement2.CustomDialog.Doctor_profile_Dialog;
import com.goku.clinicmanagement2.CustomDialog.PostpneDialog;
import com.goku.clinicmanagement2.CustomDialog.PostponeDialog;
import com.goku.clinicmanagement2.CustomDialog.ProfileDialog;
import com.goku.clinicmanagement2.CustomDialog.SetProfileDialog;
import com.goku.clinicmanagement2.Database.DoctorDb;
import com.goku.clinicmanagement2.Database.DoctorImagedb;
import com.goku.clinicmanagement2.Database.DoctorSessionDb;
import com.goku.clinicmanagement2.Database.PreviousScheduledb;
import com.goku.clinicmanagement2.Database.Referencedb;
import com.goku.clinicmanagement2.Database.Rescheduledb;
import com.goku.clinicmanagement2.DoctorModel.CancelPostponeMember;
import com.goku.clinicmanagement2.DoctorModel.MemberDoctor;
import com.goku.clinicmanagement2.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Doctor_profile extends AppCompatActivity implements View.OnClickListener, Doctor_profile_Dialog.Doctor_profile_Listener, ProfileDialog.profile_Listener, PostponeDialog.PostponeListener {

    ImageView createprofile, cancel_or_postpone,scheduleTimings,checkCreatedProfile,complaint,update,trackincome,deleteprofile, logout;
    TextView createprofile_text, cancel_or_postpone_text,scheduleTimings_text,checkCreatedProfile_text,complaint_text,update_text,trackincome_text,deleteprofile_text, log;

    String initialTiming= "";
    String dueTiming= "";
    String day_of_week = "";

    String fetchUrl="";

    String url="";

    FirebaseStorage storage;
    StorageReference storageReference;

    String concat="", existingData="", re_schedule="";

    Set<String> removeDuplicate= new LinkedHashSet<>();

    String joinTimings1="";

    CancelPostponeMember cpm;

    int cancelPostponemaxId=0;

    DoctorDb db;
    DoctorSessionDb ds;
    Rescheduledb rdb;
    PreviousScheduledb pdb;
    Referencedb ridb;


    FirebaseDatabase database, db2;
    DatabaseReference ref, childref;

    Set<String> doctorScheduleTiming= new LinkedHashSet<>();

    int maxId=0;

    MemberDoctor md;


    Time_1 t1;

    Uri imageuri;
    RequestBuilder<Drawable> fetchProfile;

    Bitmap bitmap;

    String Name="", Specialization="", Mobile="",Fees="", Name1="", Fees1="", accurateData1="";

    boolean isCreateDoctorProfile, isScheduled, snapShotExisted;

    StringBuilder sb1;

    ArrayList<String> TimeSchedule;

    String patientName="", concat5="";

    boolean isScheduledCancelledByDoctor=false;

    ImageView menu_icon;



    Set<String> timings= new LinkedHashSet<>();
    private int index=0;

    DoctorImagedb dimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        dimg= new DoctorImagedb(Doctor_profile.this);

        pdb= new PreviousScheduledb(Doctor_profile.this);

        db= new DoctorDb(Doctor_profile.this);
        ds= new DoctorSessionDb(Doctor_profile.this);
        rdb= new Rescheduledb(Doctor_profile.this);
        ridb= new Referencedb(Doctor_profile.this);

        storage= FirebaseStorage.getInstance();
        storageReference= storage.getReference();

        t1= new Time_1();

        md= new MemberDoctor();

        //  db.delete();
       //  ds.delete();

       //   rdb.delete();

        SQLiteDatabase db1= db.getReadableDatabase();
        String query="select * from doctor";
        Cursor c1= db1.rawQuery(query,null);

        System.out.println("Doctordb is empty: " + c1.getCount());

        SQLiteDatabase db11= ds.getReadableDatabase();
        String query1="select * from doctorsession";
        Cursor c11= db11.rawQuery(query1,null);

        System.out.println("Doctorsession is empty: " + c11.getCount());

        createprofile= (ImageView) findViewById(R.id.create_profile_img_id);
        createprofile_text= (TextView) findViewById(R.id.create_profile_textview_id);

        cancel_or_postpone= (ImageView) findViewById(R.id.cancel_postpone_schedule_id);
        cancel_or_postpone_text= (TextView) findViewById(R.id.cancel_update_textview_schedule_id);

        scheduleTimings= (ImageView) findViewById(R.id.send_notifications_imageview_id);
        scheduleTimings_text= (TextView) findViewById(R.id.send_notification_textview_id);

        checkCreatedProfile= (ImageView) findViewById(R.id.check_created_profile_id);
        checkCreatedProfile_text= (TextView) findViewById(R.id.check_created_profile_textview_id);

        complaint= (ImageView) findViewById(R.id.doctor_profile_notification_id);
        complaint_text= (TextView) findViewById(R.id.doctor_profile_notification_textview_id);

        update= (ImageView) findViewById(R.id.update_imageview_id);
        update_text= (TextView) findViewById(R.id.update_textview_id);

        trackincome= (ImageView) findViewById(R.id.track_income_imageview_id);
        trackincome_text= (TextView) findViewById(R.id.track_income_textview_id);

        deleteprofile= (ImageView) findViewById(R.id.delete_imageview_id);
        deleteprofile_text= (TextView) findViewById(R.id.delete_textview_id);

        logout= (ImageView) findViewById(R.id.track_income_imageview_id);

        log= (TextView) findViewById(R.id.track_income_textview_id);

        logout.setOnClickListener(this);

        log.setOnClickListener(this);

        createprofile.setOnClickListener(this);
        createprofile_text.setOnClickListener(this);

        cancel_or_postpone.setOnClickListener(this);
        cancel_or_postpone_text.setOnClickListener(this);

        scheduleTimings.setOnClickListener(this);
        scheduleTimings_text.setOnClickListener(this);

        checkCreatedProfile.setOnClickListener(this);
        checkCreatedProfile_text.setOnClickListener(this);

        complaint.setOnClickListener(this);
        complaint_text.setOnClickListener(this);

        update.setOnClickListener(this);
        update_text.setOnClickListener(this);

        trackincome.setOnClickListener(this);
        trackincome_text.setOnClickListener(this);

        deleteprofile.setOnClickListener(this);
        deleteprofile_text.setOnClickListener(this);

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }



   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,Menu.FIRST,Menu.NONE,"Logout");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }   */

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_profile_img_id:
                createDoctorProfile();
                System.out.println("From create_profile_img_id: isCreateDoctorProfile: " + isCreateDoctorProfile + "\t" + "isScheduled: " + isScheduled);
                if (isCreateDoctorProfile && isScheduled) {

                }
                break;
            case R.id.create_profile_textview_id:
                createDoctorProfile();
                System.out.println("From create_profile_textview_id: isCreateDoctorProfile: " + isCreateDoctorProfile + "\t" + "isScheduled: " + isScheduled);
                if (isCreateDoctorProfile && isScheduled) {

                }
                break;

            case R.id.cancel_postpone_schedule_id:
                cancelPostpone();
                break;
            case R.id.cancel_update_textview_schedule_id:
                cancelPostpone();
                break;

            case R.id.send_notifications_imageview_id:
                isScheduled=DatePick();
                System.out.println("From send_notification_imageview: isCreateDoctorProfile: " + isCreateDoctorProfile + "\t" + "isScheduled: " + isScheduled);
                if (isCreateDoctorProfile && isScheduled) {

                }
                break;
            case R.id.send_notification_textview_id:
                isScheduled=DatePick();
                System.out.println("From send_notification_textview_id: isCreateDoctorProfile: " + isCreateDoctorProfile + "\t" + "isScheduled: " + isScheduled);
                if (isCreateDoctorProfile && isScheduled) {

                }
                break;

            case R.id.check_created_profile_id:
                // openAlertDialog();

                SQLiteDatabase db11 = db.getReadableDatabase();
                String query1 = "select * from doctor";
                Cursor c11 = db11.rawQuery(query1, null);

                AlertDialog.Builder profile= new AlertDialog.Builder(Doctor_profile.this);

                profile.setTitle("Check profile");

                String name1= "", specialization1="", fees1="", mobile1="";

                if (c11!=null && c11.getCount() > 0) {
                    if (c11.moveToFirst()) {
                        name1= c11.getString(0);
                        specialization1= c11.getString(1);
                        mobile1= c11.getString(2);
                        fees1= c11.getString(3);
                    }
                }

                profile.setMessage("This is your profile" + "\n" + "Name: " + name1 + "\n\n" +
                        "Specialization: " + specialization1 + "\n\n" +
                        "Mobile: " + mobile1 + "\n\n" +
                        "Fees: " + fees1);

                AlertDialog a1= profile.create();
                a1.show();



                break;
            case R.id.check_created_profile_textview_id:
                //  openAlertDialog();
                SQLiteDatabase db111 = db.getReadableDatabase();
                String query11 = "select * from doctor";
                Cursor c111 = db111.rawQuery(query11, null);

                String name5="", specialization5="", mobile5="", fees5="";

                if (c111!=null && c111.getCount() > 0) {
                    if (c111.moveToFirst()) {
                        name5= c111.getString(0);
                        specialization5= c111.getString(1);
                        mobile5= c111.getString(2);
                        fees5= c111.getString(3);
                    }
                }

                AlertDialog.Builder profile1= new AlertDialog.Builder(Doctor_profile.this);

                profile1.setTitle("Check profile");

                profile1.setMessage("This is your profile" + "\n" + "Name: " + name5 + "\n\n" +
                        "Specialization: " + specialization5 + "\n\n" +
                        "Mobile: " + mobile5 + "\n\n" +
                        "Fees: " + fees5);

                AlertDialog a11= profile1.create();
                a11.show();
                break;

            case R.id.doctor_profile_notification_id:
                fetchNotificationData();
                break;
            case R.id.doctor_profile_notification_textview_id:
                fetchNotificationData();
                break;

            case R.id.update_imageview_id:
                updateDoctorProfile();
                break;
            case R.id.update_textview_id:
                updateDoctorProfile();
                break;

            case R.id.track_income_imageview_id:
                Toast.makeText(Doctor_profile.this, "Clicked on Logout", Toast.LENGTH_SHORT).show();
                  FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
                break;
            case R.id.track_income_textview_id:
                Toast.makeText(Doctor_profile.this, "Clicked on Logout", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
                break;

            case R.id.delete_imageview_id:
                deleteProfile();
                break;
            case R.id.delete_textview_id:
                deleteProfile();
                break;

        }
    }

    private void updateDoctorProfile() {
        deleteProfile();
        createDoctorProfile();
        isScheduled=DatePick();
    }

    @SuppressLint("RestrictedApi")
    private void deleteProfile() {
        SQLiteDatabase db1= db.getWritableDatabase();
        String query = "select * from doctor";
        Cursor c1 = db1.rawQuery(query, null);

        String name="", fees= "", mobile="", specialization="";

        if (c1!=null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                name= c1.getString(0);
                Name1= name;
                fees= c1.getString(3);
                Fees1= fees;
                mobile= c1.getString(2);
                specialization= c1.getString(1);
            }
        }

        String key=name + " " + mobile + " " + fees;

        String key5="KaliLinux 9101058634 100";

        System.out.println("From deleteProfile() key : " + key + "\n Specializaion: " + specialization);

        String key1= specialization;

        database= FirebaseDatabase.getInstance();


        SQLiteDatabase db5= ridb.getWritableDatabase();

        String query1 = "select * from referenceid";
        Cursor c11 = db5.rawQuery(query1, null);

        String getReference="";


        if (c11!=null && c11.getCount() > 0) {
            if (c11.moveToFirst()) {
                getReference= c11.getString(0);
            }
        }

        if (md.getReferenceId()==null) {

            System.out.println("From deleteProfile() md.getReferenceId(): " + getReference);
            ref = database.getInstance().getReference().child("Doctor");
        }

        else {
            System.out.println("From deleteProfile() md.getReferenceId(): " + md.getReferenceId());
            getReference= md.getReferenceId();
            ref = database.getInstance().getReference().child("Doctor");
        }

        String x= " Cardiologist    Aman 8812055712 100" + "Aman 8812055712 100";

        String specialization1[]= {"Cardiologist", "Psychiatrist", "Dentist", "Surgeon", "EntSpecialist", "ChildSpecialist",
                "Urologist", "Veterenian", "Optometrist", "Chiropractor", "Dermatologist", "Gynaecologist", "Herbalist",
                "Paramedic"};

       // String x= "Psychiatrist    Pikolo 8812055712 100";

      //  String key1= name1 + " " + mobile1 + " " + fees1;

        for (String s: specialization1) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctor").child(s+ "    " + key);
            ref.removeValue();
        }

        Toast.makeText(this, "Doctor " + name + " deleted successfully", Toast.LENGTH_SHORT).show();

        //  DatabaseReference dbref= database.getInstance().getReference("Doctor").child(" Cardiologist    " + getReference);

     //   DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctor").child(s+ "    " + key);

    //    System.out.println("dbref.getRepo(): " + dbref.getPath());

     /*   Query query2= dbref.orderByChild("referenceId:").equalTo(getReference);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {

                    System.out.println("ds: " + ds.getValue());
                    ds.getRef().removeValue();
                }
                Toast.makeText(Doctor_profile.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });     */




      //  dbref.removeValue();

        // db= new DoctorDb(Doctor_profile.this);

        db.delete();



        Toast.makeText(this, "Data deleted successfully!", Toast.LENGTH_SHORT).show();


      /*  if (md.getReferenceId()==null) {

            System.out.println("From deleteProfile() md.getReferenceId(): " + getReference);
            ref = database.getInstance().getReference().child("Doctor");
        }

        else {
            System.out.println("From deleteProfile() md.getReferenceId(): " + md.getReferenceId());
            getReference= md.getReferenceId();
            ref = database.getInstance().getReference().child("Doctor");
        }

        System.out.println("ref.get(): " + ref.get());

        Query query2= ref.orderByChild("referenceId:").equalTo(getReference);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {

                    System.out.println("ds: " + ds.getValue());
                    ds.getRef().removeValue();
                }
                Toast.makeText(Doctor_profile.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });     */

        md= new MemberDoctor();

      /*  ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("From deleteProfile() snapshot.getChilderCount(): " + snapshot.getChildrenCount());
                System.out.println("\n\n");
                System.out.println("From deleteProfile() value: " + snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });   */
    }

    private void cancelPostpone() {
        SQLiteDatabase db1= db.getWritableDatabase();

        String query = "select * from doctor";
        Cursor c1 = db1.rawQuery(query, null);


        System.out.println("From cancelPostpone() doctorScheduleTiming is: " + doctorScheduleTiming);


        String name="", fees= "";

        if (c1!=null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                name= c1.getString(0);
                Name1= name;
                fees= c1.getString(3);
                Fees1= fees;
            }
        }

        SQLiteDatabase db= ds.getWritableDatabase();

        String query1 = "select * from doctorsession";
        Cursor c11 = db.rawQuery(query1, null);

        System.out.print("From cancelPostpone() c11.getString(0):");

        if (c11!=null && c11.getCount() > 0) {
            if (c11.moveToFirst()) {
                do {
                    System.out.println(c11.getString(0));
                } while (c11.moveToNext());
            }
        }

        System.out.println("\n\n\n\n");

        database= FirebaseDatabase.getInstance();
        ref= database.getReference("Booking");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String data = String.valueOf(snapshot.getValue());

                System.out.println("From cancelPostpone() data: " + data);

                Set<String> receivedData = init(data, Name1, Fees1);

                System.out.println("From cancelPostpone() receivedData: " + receivedData);

                sb1= new StringBuilder();

                ArrayList<String> sbToList= new ArrayList<>();

                String getData= receivedData + "";

                for (String s: receivedData) {
                    sbToList.add(s);
                }
//
                //  Toast.makeText(Doctor_profile.this, "" + sbToList + "", Toast.LENGTH_SHORT).show();

                TimeSchedule = new ArrayList<>();



                String str1= sb1.toString();

                System.out.println("getData: " + getData);

                String spliGetData[]= getData.split("\n");

                for (String s5: spliGetData) {
                    if (!s5.isEmpty()) {
                        s5= s5.trim();
                        if (s5.contains("Patient Name:")) {
                            patientName= s5.replace("Patient Name:", "");
                        }
                    }
                }

                System.out.println("From cancelPostpone() patientName: " + patientName);



                System.out.println("sbToList: " + sbToList);

                //   System.out.println("TimeSchedule: " + TimeSchedule);

                String str11[]= getData.split("\\[|\n|\\]|,");

                for (String s1: str11) {
                    if (!s1.isEmpty()) {
                        System.out.println("cancelPostpone() s1: " + s1);
                        if (s1.contains("Patient Appointment Time:")) {
                            s1= s1.replace("Patient Appointment Time:","");
                            s1= s1.trim();
                            TimeSchedule.add(s1);
                        }
                    }
                    else {
                        continue;
                    }
                }

                System.out.println("TimeSchedule: " + TimeSchedule);

                System.out.println("\n\n");

                AlertDialog.Builder cancelOrPostpone = new AlertDialog.Builder(Doctor_profile.this);

                cancelOrPostpone.setTitle("Postpone");

                cancelOrPostpone.setMessage("You can postpone or delay your schedule here");

                cancelOrPostpone.setPositiveButton("Postpone", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder showSchedule= new AlertDialog.Builder(Doctor_profile.this);
                        showSchedule.setTitle("Check your previous schedules");
                     /*   if (!doctorScheduleTiming.isEmpty()) {
                            System.out.println("From cancelPostpone() doctorScheduleTiming is: " + doctorScheduleTiming);
                            showSchedule.setMessage("Your schedule timings:" + doctorScheduleTiming + "");
                        }   */
                     //   else {
                            SQLiteDatabase db= rdb.getWritableDatabase();

                            String query = "select * from reschedule";
                            Cursor c1 = db.rawQuery(query, null);
                            Set<String> schedules= new LinkedHashSet<>();
                            if (c1!=null && c1.getCount() > 0) {
                                if (c1.moveToFirst()) {
                                    do {
                                        if (!c1.getString(0).trim().isEmpty()) {
                                            schedules.add(c1.getString(0));
                                        }
                                    } while (c1.moveToNext());
                                }
                            }
                            Set<String> set11= new LinkedHashSet<>();
                            for (String s: schedules) {
                                if (!s.trim().isEmpty()) {
                                    set11.add(s);
                                }
                            }
                            showSchedule.setMessage("Your schedule timings:" + set11 + "");
                      //  }
                        showSchedule.setPositiveButton("Re-schedule", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                fillData();

                                System.out.println("From cancelPostpone() Name: " + Name1 + " Fees1: " + Fees1);
                                System.out.println("\n\n");
                                System.out.println("From cancelPostpone() schedule: " + re_schedule);

                                //  uploadCancelorUpdate();

                            }
                        });

                        showSchedule.setNegativeButton("Cancel Schedule", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // uploadCancelOrPostpone(Name, Fees, schedule, "cancelled");

                                fillData("cancelled");
                            }
                        });

                        AlertDialog a1= showSchedule.create();
                        a1.show();
                    }
                });

                cancelOrPostpone.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog a1= cancelOrPostpone.create();
                a1.show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fillData(String cancelled) {

        SQLiteDatabase db= rdb.getWritableDatabase();

        String query = "select * from reschedule";
        Cursor c1 = db.rawQuery(query, null);

        if (c1!=null && c1.getCount() > 0) {
            openScheduleDialog();
            //  openDateTimeDialog();
            System.out.println("From fillData() Name: " + Name1 + " Fees1: " + Fees1);
            isScheduledCancelledByDoctor= true;
            addToDatabase("Cancelled");
        }

        else {
            fetchNotificationData();
            openScheduleDialog();
            isScheduledCancelledByDoctor= true;
            addToDatabase("Cancelled");
            //    openDateTimeDialog();

            System.out.println("From fillData() Name: " + Name1 + " Fees1: " + Fees1);
        }

        System.out.println("From fillData sb1: " + sb1);
    }


    private void fillData() {

        //  fetchNotificationData();

        SQLiteDatabase db= rdb.getWritableDatabase();

        String query = "select * from reschedule";
        Cursor c1 = db.rawQuery(query, null);

        if (c1!=null && c1.getCount() > 0) {
            openScheduleDialog();
            openDateTimeDialog();
            System.out.println("From fillData() Name: " + Name1 + " Fees1: " + Fees1);
        }

        else {
            fetchNotificationData();
            openScheduleDialog();
            openDateTimeDialog();

            System.out.println("From fillData() Name: " + Name1 + " Fees1: " + Fees1);
        }

        System.out.println("From fillData sb1: " + sb1);

        //  openDateTimeDialog();

    }

    private void openScheduleDialog() {
        PostponeDialog ppd= new PostponeDialog();
        ppd.show(getSupportFragmentManager(),"reschedule");
    }

    private void openDateTimeDialog() {
        final Calendar calendar= Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DATE,date);

                TimePickerDialog.OnTimeSetListener timeSetListener= new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hour);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat sd= new SimpleDateFormat("dd-mm-yyyy HH:mm");

                        String formattedDate= sd.format(calendar.getTime());

                        String splitter[]= formattedDate.split(" ");

                        String day= getEquivalentDay(calendar.get(Calendar.DAY_OF_WEEK));

                        day_of_week= day;

                        String accurateDate= String.valueOf(calendar.get(Calendar.DATE)) + "/" + String.valueOf((calendar.get(Calendar.MONTH)+1) + "/" + String.valueOf(calendar.get(Calendar.YEAR))) + " " +  splitter[1] + "  " + day;

                        initialTiming= formattedDate;

                        System.out.println("From Postpone: Selected Date and Time: " + accurateDate);

                        accurateData1= accurateDate;

                        addToDatabase(accurateDate);

                        timings.add(accurateDate);

                        System.out.println("From Postpone: From 1st timepicker timings: " + timings);

                        System.out.println("\n\n");

                        System.out.println("From Postpone sb1: " + sb1);
                        System.out.println("From Postpone: Name: " + Name1 + " Fees1: " + Fees1);




                        timings.clear();
                    }
                };
                new TimePickerDialog(Doctor_profile.this,timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(Doctor_profile.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();

    }

    private void addToDatabase(String accurateDate) {
        System.out.println("From addToDatabase() accurateDate: " + accurateDate);
        System.out.println("From addToDatabase() schedule: " + re_schedule + " and Patient Name: " + Name1 + " and Patient Fees: " + Fees1);
        Toast.makeText(this, "From addToDatabase() accurateDate: " + accurateDate, Toast.LENGTH_SHORT).show();

        uploadCancelOrPostpone(Name1, Fees1, re_schedule, accurateDate);
    }

    private void fetchNotificationData() {
        SQLiteDatabase db1= db.getWritableDatabase();

        String query = "select * from doctor";
        Cursor c1 = db1.rawQuery(query, null);

        String name="", fees= "";

        if (c1!=null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                name= c1.getString(0);
                Name1= name;
                fees= c1.getString(3);
                Fees1= fees;
            }
        }

        database= FirebaseDatabase.getInstance();
        ref= database.getReference("Booking");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String data= String.valueOf(snapshot.getValue());

                //   System.out.println("data: " + data);

                Set<String> receivedData= init(data,Name1,Fees1);

                System.out.println("From fetchNotificationData: " + receivedData);
                String patientAge="";
                sb1= new StringBuilder();
                for (String s: receivedData) {
                    if (s.contains("Patient Age:")) {
                        s= s.replace(",","");
                        patientAge= s;
                        sb1.append(patientAge);
                    }
                    else {
                        if (!s.contains("Patient Age:"))
                            sb1.append(s);
                    }
                }

                //   System.out.println("From fetchNotificationData: " + sb1);

                String str= receivedData+ "";

                String split[]= str.split(",|\n");

                //   System.out.println("\n\n\n\n");

                ArrayList<String> AppointmentList= new ArrayList<>();

                for (String s: split) {
                    if (!s.isEmpty()) {
                        //  System.out.println("From fetchNotificationData s: " + s);
                        if (s.contains("Patient Appointment Time:")) {
                            s= s.replace("Patient Appointment Time:", "");
                            s= s.trim();
                            AppointmentList.add(s);
                        }
                    }
                    else {
                        continue;
                    }
                }

                //  System.out.println("\n\n\n\n");


                //  System.out.println("From fetchNotificationData AppointmentList: " + AppointmentList);

                for (String s: AppointmentList) {
                    rdb.insertData(s);
                }


                // System.out.println("\n\n\n\n");

                AlertDialog.Builder showNotification= new AlertDialog.Builder(Doctor_profile.this);
                showNotification.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                showNotification.setTitle("Notification bar");
                showNotification.setMessage(sb1);

                AlertDialog a1= showNotification.create();
                a1.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private static Set<String> init(String data, String name, String fees) {
        ArrayList<String> patientTimeAppointment= new ArrayList<>();
        String s[] = data.split("(\\d+|\\w+)-(\\w+|\\d+)-(\\w+|\\d)+-(\\w+|\\d+)-(\\w+|\\d+)|\\{|\\[|\\]|\\}|null|patient_Details=|doctor_Details=");
        ArrayList<String> patientData = new ArrayList<>();
        for (String s1 : s) {
            if (!s1.isEmpty()) {
                s1 = s1.trim();
                String split[]= s1.split(",|^=$|Docto ImageUrl:\\s+https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)|imageUrl=https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)");
                for (String s5: split) {
                    s5= s5.trim();
                    if (!s5.isEmpty()) {
                        patientData.add(s5);
                    }
                    else {
                        continue;
                    }
                }
            }
        }
        ArrayList<String> receiveData= slidingWindow(patientData, name, fees);
        Set<String> patientDetailsList= new LinkedHashSet<>();
        long count=0;
        for (String s1: receiveData) {
            String split[]= s1.split("\\[|,|\\]");
            String patName="", patGender="", patAge="", patMobile="", patAppointmentTime="";
            for (String s2: split) {
                if (!s2.isEmpty()) {
                    s2= s2.trim();
                    if (s2.contains("name=")) {
                        s2= s2.replace("name=","Patient Name: ");
                        patName= s2;
                    }
                    if (s2.contains("gender=")) {
                        s2= s2.replace("gender=", "Patient Gender: ");
                        patGender= s2;
                    }
                    if (s2.contains("age=")) {
                        s2= s2.replace("age=", "Patient Mobile: ");
                        patAge= s2;
                    }
                    if (s2.contains("mobile=")) {
                        s2= s2.replace("mobile=","Patient Age: ");
                        patMobile= s2;
                    }
                    if (s2.contains("Appointment Time:")) {
                        s2= s2.replace("Appointment Time:","Patient Appointment Time: ");
                        patAppointmentTime= s2;
                        patientTimeAppointment.add(s2);
                    }
                }
            }
            count++;
            if (!(patName.isEmpty() && patAge.isEmpty() && patGender.isEmpty() && patAppointmentTime.isEmpty() && patMobile.isEmpty())) {
                patName= patName.trim();
                patAge= patAge.trim();
                patGender= patGender.trim();
                patMobile= patMobile.trim();
                patAppointmentTime= patAppointmentTime.trim();
                patientDetailsList.add(patName + "\n\n" + patAge + "\n\n" + patGender + "\n\n" + patAppointmentTime + "\n\n" + patMobile);
                patientDetailsList.add("\n\n** End of Patient Details List " + count + " **\n\n");
            }
        }

        System.out.println("From init() patientDetailsList: " + patientDetailsList);

        System.out.println("From init() patientTimeAppointment:  " + patientTimeAppointment);
        return patientDetailsList;
    }

    private static ArrayList<String> slidingWindow(ArrayList<String> patientData, String name, String fees) {
        ArrayList<String> fetchList;
        ArrayList<ArrayList<String>> data= new ArrayList<ArrayList<String>>();
        int start=0, end=0;
        while (end <= patientData.size()) {
            fetchList= new ArrayList<>();
            end++;
            if (end < patientData.size() && end-start+1==8) {
                while (end <= patientData.size() && start<=end) {
                    fetchList.add(patientData.get(start));
                    start++;
                }
                start= end+1;
                end++;
            }
            if (!fetchList.isEmpty()) {
                data.add(fetchList);
            }
        }
        String docName= "";
        ArrayList<String> extractListData= new ArrayList<>();
        for (ArrayList<String> a1: data) {
            for (String s: a1) {
                String copyStr=a1+"";
                if (s.contains("DoctorName:")) {
                    docName= s;
                    docName= docName.replace("DoctorName:", "");
                    if (docName.trim().equals(name.trim())) {
                        extractListData.add(copyStr);
                    }
                }
            }
        }
        return extractListData;
    }

    private void openAlertDialog() {
        ProfileDialog pd= new ProfileDialog();
        pd.show(getSupportFragmentManager(),"Profile Dialog");
    }

    private void validateAndUpload(Set<String> timings1) {
        System.out.println("From validateAndUpload(): 1" + timings1);
        String str="";
        for (String s: timings1) {
            str+= s+ " ";
        }
        String s1[]= str.split("\\ +");
        SQLiteDatabase db11= db.getReadableDatabase();
        String query1 = "select * from doctor";
        Cursor c11 = db11.rawQuery(query1, null);
        if (c11!=null && c11.getCount()>0) {
            if (c11.moveToFirst()) {
                Name = c11.getString(0);
                Specialization = c11.getString(1);
                Mobile = c11.getString(2);
                Fees = c11.getString(3);
            }
        }
        for (String s: timings1) {
            if (!joinTimings1.contains(s)) {
                joinTimings1 += s + " ";
            }
        }
        database= FirebaseDatabase.getInstance();

        if (Name.contains(" ")) {
            Name= Name.replace(" ", "");
        }

        SQLiteDatabase db1= ridb.getWritableDatabase();

        String query11 = "select * from referenceid";
        Cursor c111 = db1.rawQuery(query11, null);

        String refId="";

        //   if (c111!=null && c111.getCount() > 0) {
        //      if (c111.moveToFirst()) {
        //          refId= c111.getString(0);
        //           ref= getInstance().getReference().child("Doctor"+"/"+ " " + Specialization + "    " + refId);
        // //      }
        //  }

        //  else {

        String x= "Cardiologist    Rohan 8812055712 100";

        md.setReferenceId(Name + " " + Mobile + " " + Fees);

        System.out.println("From validateAndUpload() md.setReferenceId(Name + \" \" + Mobile + \" \" + Fees): " + md.getReferenceId());

        ref = FirebaseDatabase.getInstance().getReference().child("Doctor" + "/" + Specialization + "    " + md.getReferenceId());
        //   }

        SQLiteDatabase db= ridb.getWritableDatabase();

        String query = "select * from referenceid";
        Cursor c1 = db.rawQuery(query, null);

        if (c1.getCount() < 1) {
            ridb.insertData(md.getReferenceId());
        }

        md= new MemberDoctor();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    snapShotExisted= true;

                    Object obj= snapshot.getValue();

                    String s1= String.valueOf(obj);

                    System.out.println("s1: " + s1);

                    int i=0;

                    String split[]= s1.split("accurateDetails=|[[{},]|\\ +]|null");

                    for (String s: split) {
                        s= s.trim();
                        removeDuplicate.add(s);
                        if (!s.isEmpty()) {
                            System.out.println("s: " + s + " i: " + i);
                            if (i>0) {
                                concat5+= s + " ";
                            }
                            i++;
                        }
                    }

                    if (concat5.contains("]")) {
                        concat5= concat5.replace("]","");
                    }

                    System.out.println(" From validateAndUpload() concat5 2: "+ concat5);

                    recordSchedule(concat5);



                    System.out.println();

                    System.out.println(" From validateAndUpload() 4: " + removeDuplicate);

                    maxId= (int) snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String cacheData="";




        md.setAccurateDetails(joinTimings1);

        String x1= "Psychiatrist    Gildart 8812055712 100";

        md.setReferenceId(Name + " " + Mobile + " " + Fees);

        System.out.println("validateAndUpload() md.getReferenceId(): " + md.getReferenceId());

        SQLiteDatabase db2= dimg.getWritableDatabase();

        String query2 = "select * from doctorimage";
        Cursor c2 = db2.rawQuery(query2, null);

        if (c2!=null && c2.getCount() > 0) {
            if (c2.moveToFirst()) {
                md.setUrlImage(c2.getString(0));
            }
        }

        else {

            if (fetchUrl != null) {
                md.setUrlImage(fetchUrl);
            }
        }
        //  md.setUrlImage("https://firebasestorage.googleapis.com/v0/b/clinicmanagement-8f31a.appspot.com/o/Doctor_Images%2F%20Cardiologist%2FAman?alt=media&token=be90fa01-ffc5-4034-b5ed-20ed02d77bd0");

        ref.child(String.valueOf(maxId+1)).setValue(md);

        Toast.makeText(this, "Schedule created successfully!", Toast.LENGTH_SHORT).show();
    }

    private void recordSchedule(String concat5) {
        SQLiteDatabase db1= db.getWritableDatabase();

        String query = "select * from doctor";
        Cursor c1 = db1.rawQuery(query, null);

        String doctorName="", doctorMobile="";

        if (c1!=null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                doctorName= c1.getString(0);
                doctorMobile= c1.getString(2);
            }
        }

        System.out.println("From recordSchedule() doctorName concat: " + concat5);

        String concatSplit[]= concat5.split("\\d+\\/\\d+\\/\\d+|urlImage=https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*|referenceId=\\.*)");



        System.out.println("\n\n");

        System.out.println("From recordSchedule() doctorName concatSplit: ");

        for (String s: concatSplit) {
            s=s.trim();
            String extractSchedule="";
            System.out.println(s);
            if (!s.isEmpty()) {
                if (isDay(s)) {
                    extractSchedule+= s + " ";
                }
                if (isTime(s)) {
                    extractSchedule+= s + " ";
                }
                System.out.println("From recordSchedule() extractSchedule: " + extractSchedule);
                doctorScheduleTiming.add(extractSchedule);
                rdb.insertData(extractSchedule);
            }
        }

        System.out.println("From recordSchedule() doctorScheduleTiming: " + doctorScheduleTiming);



        uploadDoctorScheduleTiming(doctorScheduleTiming);



        System.out.println("concatSplit ends\n\n\n\n");

        System.out.println("From recordSchedule() doctorName: " + doctorName + " doctorMobile: " + doctorMobile);
    }

    private boolean isTime(String s) {
        String regex="\\d+:\\d+";
        Pattern pattern= Pattern.compile(regex);
        Matcher matcher= pattern.matcher(s);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    private boolean isDay(String day) {
        String days[]= {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String s: days) {
            if (s.equals(day)) {
                return true;
            }
        }
        return false;
    }

    private void uploadDoctorScheduleTiming(Set<String> doctorScheduleTiming) {
        System.out.println("From uploadDoctorScheduleTiming() doctorScheduleTiming: " + doctorScheduleTiming);
    }

    private Set<String> slidingWindow(String concat) {
        Set<String> TimeSet= new LinkedHashSet<>();
        System.out.println("From the slidingWindow() concat is : " + concat);
        String strSplit[]= concat.split("\\ +");
        for (String s: strSplit) {
            System.out.println("From the slidingWindow() strSplit is: " + s);
        }
        ArrayList<String> l1= new ArrayList<>();
        for (String s: strSplit) {
            l1.add(s);
        }
        int start=0, end=0;
        start = end + 1;
        end++;
        String Concat="";
        while (end<= strSplit.length) {
            end++;
            if (end-start+1==4) {
                try {
                    Concat += strSplit[end-1] + " " + strSplit[end - 2];
                } catch (Exception e){}
            }
            TimeSet.add(Concat);
            Concat= "";
        }
        System.out.println("TimeSet is: " + TimeSet);
        return TimeSet;
    }

    private boolean DatePick() {
        AlertDialog.Builder dateChooser= new AlertDialog.Builder(Doctor_profile.this);
        dateChooser.setTitle("Choose Date and Time");
        dateChooser.setMessage("Select your schedule as per your convinience!");
        dateChooser.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                show_Date_Time_Dialog();
                System.out.println();
                System.out.println("From DatePick() timings: " + timings);
                System.out.println("From DatePick: " + concat);
                processData();
            }
        });
        dateChooser.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog a1= dateChooser.create();
        a1.show();
        return true;
    }

    private void processData() {
        //   System.out.println("From processData():");
        //   System.out.println(db.getAllEntries());
        //   System.out.println(ds.getAllEntries());
    }

    boolean shouldTake= false;

    private boolean show_Date_Time_Dialog() {
        final Calendar calendar= Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DATE,date);

                TimePickerDialog.OnTimeSetListener timeSetListener= new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hour);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat sd= new SimpleDateFormat("dd-mm-yyyy HH:mm");

                        String formattedDate= sd.format(calendar.getTime());

                        String splitter[]= formattedDate.split(" ");

                        String day= getEquivalentDay(calendar.get(Calendar.DAY_OF_WEEK));

                        day_of_week= day;

                        String accurateDate= String.valueOf(calendar.get(Calendar.DATE)) + "/" + String.valueOf((calendar.get(Calendar.MONTH)+1) + "/" + String.valueOf(calendar.get(Calendar.YEAR))) + " " +  splitter[1] + "  " + day;

                        initialTiming= formattedDate;

                        System.out.println("Selected Date and Time: " + accurateDate);

                        timings.add(accurateDate);

                        System.out.println("From 1st timepicker timings: " + timings);


                        SQLiteDatabase db11= db.getReadableDatabase();

                        String query1 = "select * from doctor";
                        Cursor c11 = db11.rawQuery(query1, null);

                        if (c11.getCount() > 0) {
                            isCreateDoctorProfile= true;
                        }

                        if (isCreateDoctorProfile) {
                            validateAndUpload(timings);
                        }

                        String accDate[]= accurateDate.split("\\ +");

                        accurateDate="";
                        accurateDate+= accDate[2] + " " + accDate[1];

                        concat+= accurateDate;
                    }
                };
                new TimePickerDialog(Doctor_profile.this,timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(Doctor_profile.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();

        return true;
    }

    private String getEquivalentDay(int i1) {
        String weekDays[]= {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        HashMap<Integer,String> mapEqivalentDay= new HashMap<>();
        int i=1;
        for (String s1: weekDays) {
            mapEqivalentDay.put(i,s1);
            i++;
        }
        return mapEqivalentDay.get(i1);
    }

    private void createDoctorProfile() {
        isCreateDoctorProfile= openDialog();
    }

    private boolean openDialog() {
        Doctor_profile_Dialog dp= new Doctor_profile_Dialog();
        dp.show(getSupportFragmentManager(),"Doctor profile");
        return true;
    }

    @Override
    public void applyFields(String name1, String specialization1, String mobile1, String fees1, Uri imageUri) {
        System.out.println("Successfully created Doctor's profile!");
        System.out.println(name1 + " " + specialization1 + " " + mobile1 + " " + fees1);
        System.out.println("Accurate schedule is: " + day_of_week + " " + initialTiming + " to " + dueTiming);

        if (name1.contains(" ")) {
            name1= name1.replace(" ","");
        }

        Name= name1;
        Specialization= specialization1;
        Mobile= mobile1;
        Fees= fees1;
        imageuri= imageUri;

        // Image uploading to the firebase...

        if (imageuri!=null) {
            final ProgressDialog pd= new ProgressDialog(this);
            pd.setTitle("Uploading...");
            pd.show();

            String x=" Psychiatrist    Goku 8812055712 100";

            final StorageReference reference= storageReference.child("Doctor_Images"+"/" + Specialization + "/" + Name);

            //  StorageReference mReference= storageReference

            reference.putFile(imageuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(Doctor_profile.this, "Profile image uploaded successfully!", Toast.LENGTH_SHORT).show();

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    fetchUrl = String.valueOf(uri);
                                    System.out.println("From Doctor_profile uri is: " + fetchUrl);
                                    if (fetchUrl != null) {
                                        try {
                                            Toast.makeText(Doctor_profile.this, " From onSuccess(): " + uri, Toast.LENGTH_SHORT).show();
                                            md.setUrlImage(fetchUrl);
                                        } catch (Exception e) {
                                        }
                                    }
                                    Toast.makeText(Doctor_profile.this, " Download url is: " + uri, Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress= (100.0 * snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                            pd.setMessage("Uploaded: " + (int) progress + "%");
                            //   Glide.with(Doctor_profile.this)
                            //  .load()
                        }
                    });

            SQLiteDatabase db11= dimg.getWritableDatabase();

            String query1 = "select * from doctorimage";
            Cursor c11 = db11.rawQuery(query1, null);

            if (c11!=null && c11.getCount() > 0) {
                if (c11.moveToFirst()) {
                    md.setUrlImage(c11.getString(0));
                }
            }

            else {

                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        fetchUrl = String.valueOf(uri);
                        System.out.println("From Doctor_profile uri is: " + fetchUrl);
                        if (fetchUrl != null) {
                            try {
                                md.setUrlImage(fetchUrl);
                            } catch (Exception e) {
                            }
                        }
                        Toast.makeText(Doctor_profile.this, " Download url is: " + uri, Toast.LENGTH_LONG).show();
                        //  validateAndUpload();
                    }
                });
            }


        }

        SQLiteDatabase db1= db.getReadableDatabase();

        String query = "select * from doctor";
        Cursor c1 = db1.rawQuery(query, null);

        if (c1.getCount() == 0) {
            db.insertData(Name, Specialization, Mobile, Fees);
            Toast.makeText(this, " Profile created successfully!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, " Multiple profile creation is not allowed here!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void applyProfileFields(String name1, String specialization1, String mobile1, String fees1) {

        SQLiteDatabase db11 = db.getReadableDatabase();
        String query1 = "select * from doctor";
        Cursor c11 = db11.rawQuery(query1, null);


    }

    private void openSetProfileDialog() {
        SetProfileDialog sd= new SetProfileDialog();
        sd.show(getSupportFragmentManager(), "SetProfileDialog");

    }

    @Override
    public void applyScheduleFields(String schedule) {
        Toast.makeText(this, "From Doctor profile schedule is: " + schedule, Toast.LENGTH_SHORT).show();

        pdb.insertData(schedule);

        // System.out.println("From applyScheduleFields() schedule: " + schedule);

        re_schedule= schedule;

        //  System.out.println("From applyScheduleFields() schedule: " + re_schedule + " and Patient Name: " + Name1 + " and Patient Fees: " + Fees1 + " and accurateData(): " + accurateData1);

        //   uploadCancelOrPostpone(re_schedule, Name1, Fees1);

        //  openDateTimeDialog();
    }

    private void uploadCancelOrPostpone(String Name, String Fees, String schedule, String accurateDate) {

        // 510  uploadCancelOrPostpone(Name1, Fees1, re_schedule, accurateDate);
        System.out.println("From uploadCancelOrPostpone() schedule: " + schedule + "  name: " + Name + " fees: " + Fees + " accurateDate: " + accurateDate);

        SQLiteDatabase db1= db.getWritableDatabase();

        String query = "select * from doctor";
        Cursor c1 = db1.rawQuery(query, null);

        String doctorName= "", doctorMobile="";

        if (c1!=null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                doctorName= c1.getString(0);
                doctorMobile= c1.getString(2);
            }
        }

        System.out.println("From uploadCancelOrPostpone() Doctor Name: " + doctorName + " Doctor Mobile: " + doctorMobile);

        if (patientName.contains("["))  {
            patientName= patientName.replace("[", "");
        }

        System.out.println("From uploadCancelOrPostpone() patientName: " + patientName);

        cpm= new CancelPostponeMember();

        String getUUID= UUID.randomUUID().toString();

        patientName= patientName.trim();

        //   if (patientName.contains(",")) {
        //       patientName= patientName.replace(",","");
        //   }

        //   if (patientName.contains("\\s+")) {
        //      patientName= patientName.replace("\\s+","");
        //  }

        ref= FirebaseDatabase.getInstance().getReference().child("Postpone").child(patientName).child(getUUID);

        System.out.println("From uploadCancelOrPostpone() re_schedule: " + re_schedule);

        if (isScheduledCancelledByDoctor) {
            cpm.setAccuratedate("Cancelled");

            SQLiteDatabase db= pdb.getWritableDatabase();

            String query1 = "select * from previousreschedule";
            Cursor c11 = db.rawQuery(query1, null);

            if (c11!=null && c11.getCount() > 0) {
                if (c11.moveToFirst()) {
                    cpm.setPreviousschedule(c11.getString(0));
                }
            }

            pdb.delete();
        }
        else {
            cpm.setAccuratedate(accurateDate);
            cpm.setPreviousschedule(schedule);
        }

        System.out.println("\n\n");

        System.out.println("From uploadCancelOrPostpone() cpm.getAccuratedate(): " + cpm.getAccuratedate());

        System.out.println("From uploadCancelOrPostpone() isScheduledCancelledByDoctor: " + isScheduledCancelledByDoctor);

        isScheduledCancelledByDoctor= false;

        System.out.println("\n\n");


        cpm.setDoctormobile(doctorMobile);
        cpm.setDoctrname(doctorName);
        cpm.setPatientfees(Fees);
        cpm.setPatientname(patientName);
        // cpm.setPreviousschedule(schedule);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cancelPostponemaxId = (int) snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child(String.valueOf(cancelPostponemaxId + 1)).setValue(cpm);
    }
}