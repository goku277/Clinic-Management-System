package com.goku.clinicmanagement2.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goku.clinicmanagement2.CPU.ShowBookedDetailsData;
import com.goku.clinicmanagement2.Credentials.SignIn;
import com.goku.clinicmanagement2.CustomDialog.PatientProfileDialog;
import com.goku.clinicmanagement2.Database.PatientImageUrldb;
import com.goku.clinicmanagement2.Database.PatientProfiledb;
import com.goku.clinicmanagement2.DoctorModel.BookingMember;
import com.goku.clinicmanagement2.PatientModel.PatientMember;
import com.goku.clinicmanagement2.R;
import com.google.android.gms.tasks.OnCanceledListener;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Patient_Profile extends AppCompatActivity implements View.OnClickListener, PatientProfileDialog.Patient_profile_Listener {

    TextView Dashboard, CreateProfile, Notification, CheckProfile, logout;
    ImageView dashboard_img, createProfile_img, notification_img, checkProfile_img;

    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseDatabase database, db2;
    DatabaseReference ref;
    DatabaseReference childref;

    PatientMember pm;

    PatientImageUrldb pdb;

    PatientProfiledb ppdb;

    String fetchUrl="";

    int maxId=0, childCount=0;

    ArrayList<String> fetchNotification= new ArrayList<>();

    ArrayList<ArrayList<String>> list1= new ArrayList<ArrayList<String>>();

    ShowBookedDetailsData showBookedDetailsData;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__profile);

        pm= new PatientMember();

        pdb= new PatientImageUrldb(Patient_Profile.this);

        ppdb= new PatientProfiledb(Patient_Profile.this);

        showBookedDetailsData= new ShowBookedDetailsData();

        toolbar= (Toolbar) findViewById(R.id.tool_bar_id);

        setSupportActionBar(toolbar);

        //  ppdb.delete();

        storage= FirebaseStorage.getInstance();
        storageReference= storage.getReference();

        Dashboard= (TextView) findViewById(R.id.dashboard_textview_id);
        dashboard_img= (ImageView) findViewById(R.id.dashboard_id);

        CreateProfile= (TextView) findViewById(R.id.create_profile_textview_id);
        createProfile_img= (ImageView) findViewById(R.id.patient_profile_id);

        Notification= (TextView) findViewById(R.id.notification_textview_id);
        notification_img= (ImageView) findViewById(R.id.notification_id);

        CheckProfile= (TextView) findViewById(R.id.check_profile_textview_id);
        checkProfile_img= (ImageView) findViewById(R.id.check_profile_id);

        logout= (TextView) findViewById(R.id.logout_id);

        logout.setOnClickListener(this);

        Dashboard.setOnClickListener(this);
        dashboard_img.setOnClickListener(this);
        CreateProfile.setOnClickListener(this);
        createProfile_img.setOnClickListener(this);
        Notification.setOnClickListener(this);
        notification_img.setOnClickListener(this);
        CheckProfile.setOnClickListener(this);
        checkProfile_img.setOnClickListener(this);

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
    }  */

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.dashboard_id:
                startActivity(new Intent(getApplicationContext(), Dashboard_Patient.class));
                break;
            case R.id.dashboard_textview_id:
                startActivity(new Intent(getApplicationContext(), Dashboard_Patient.class));
                break;
            case R.id.patient_profile_id:
                createPatientProfile();
                break;
            case R.id.create_profile_textview_id:
                createPatientProfile();
                break;
            case R.id.check_profile_id:
                Toast.makeText(this, "Clicked on checkProfileId!", Toast.LENGTH_SHORT).show();
                fetchProfile();
                break;
            case R.id.check_created_profile_textview_id:
                fetchProfile();
                break;
            case R.id.notification_id:
                alertNotification();
                break;
            case R.id.notification_textview_id:
                alertNotification();
                break;

            case R.id.logout_id:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
                break;
        }
    }

    private void alertNotification() {
        AlertDialog.Builder notificationAlert = new AlertDialog.Builder(Patient_Profile.this);
        notificationAlert.setTitle("Choose notification type");
        notificationAlert.setMessage("Check your notifications\t\t\t\t\n\n");
        notificationAlert.setPositiveButton("Check booking details", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showBookedDetails();
            }
        });
        notificationAlert.setNeutralButton("Doctor's notification", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkDoctorsNotification();
            }
        });

        AlertDialog a1= notificationAlert.create();
        a1.show();
    }

    private void checkDoctorsNotification() {

        SQLiteDatabase db= ppdb.getWritableDatabase();

        String query = "select * from patientprofile";
        Cursor c1 = db.rawQuery(query, null);

        String name= "";

        if (c1!=null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                name= c1.getString(0);
            }
        }

        System.out.println("From checkDoctorsNotification() name is: " + name);

        // String x=  Gon;

        String x="Aman";

        database= FirebaseDatabase.getInstance();
        ref= database.getReference("Postpone");
        childref= ref.child(name);

        childref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                AlertDialog.Builder displayDoctorNotification = new AlertDialog.Builder(Patient_Profile.this);

                System.out.println("From checkDoctorsNotification() snapshot.getChildrenCount(): " + snapshot.getChildrenCount());


                if (snapshot.getChildrenCount() > 0) {

                    System.out.println("checkDoctorsNotification() snapshot: " + snapshot);
                    String value = snapshot.getValue() + "";
                    System.out.println("checkDoctorsNotification() value: " + value);

                    String splitValue[] = value.split("(\\d+|\\w+)-(\\w+|\\d+)-(\\w+|\\d)+-(\\w+|\\d+)-(\\w+|\\d+)|\\{|\\[|\\]|\\}|null|,");

                    StringBuilder sb1 = new StringBuilder();

                    for (String s : splitValue) {
                        if (!s.isEmpty()) {
                            System.out.println("checkDoctorsNotification() splitValue: " + s);
                            if (s.contains("doctrname=")) {
                                s = s.replace("doctrname=", "Doctor Name: ");
                                s = s.trim();
                                sb1.append(s);
                                sb1.append("\n\n");
                            }
                            if (s.contains("doctormobile=")) {
                                s = s.replace("doctormobile=", "Doctor Mobile: ");
                                s = s.trim();
                                sb1.append(s);
                                sb1.append("\n\n");
                            }
                            if (s.contains("accuratedate=")) {
                                s = s.replace("accuratedate=", "Booking Status: ");
                                s = s.trim();
                                sb1.append(s);
                                sb1.append("\n\n");
                            }
                            if (s.contains("previousschedule=")) {
                                s = s.replace("previousschedule=", "Previous Schedule: ");
                                s = s.trim();
                                sb1.append(s);
                                sb1.append("\n\n");
                            }
                            if (s.contains("patientname=,")) {
                                s = s.replace("patientname=,", "Patient Name: ");
                                s = s.trim();
                                sb1.append(s);
                                sb1.append("\n\n");
                            }
                            if (s.contains("patientfees=")) {
                                s = s.replace("patientfees=", "Patient Fees: ");
                                s = s.trim();
                                sb1.append(s);
                                sb1.append("\n\n");
                            }
                        }
                    }

                    displayDoctorNotification.setTitle("Check notification");
                    displayDoctorNotification.setIcon(R.drawable.notification_1);
                    displayDoctorNotification.setMessage(sb1);

                    displayDoctorNotification.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                }

                else {
                    displayDoctorNotification.setMessage("No notifications to show\n\n");
                }

                AlertDialog a1 = displayDoctorNotification.create();
                a1.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void showBookedDetails() {
        SQLiteDatabase db= ppdb.getWritableDatabase();

        String query = "select * from patientprofile";
        Cursor c1 = db.rawQuery(query, null);

        String name= "", mobile="";

        if (c1!=null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                name= c1.getString(0);
                mobile= c1.getString(1);
            }
        }

        System.out.println("From showBookedDetails() name is: " + name + "\tmobile is: " + mobile);

        database= FirebaseDatabase.getInstance();
        ref= database.getReference("Booking");

        final String finalName = name;
        final String finalMobile = mobile;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("From showBookedDetails() snapshot: " + snapshot.getValue());

                ArrayList<String> aList=  new ArrayList<>();

                if (snapshot.exists()) {
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();

                    for (String key : dataMap.keySet()) {

                        Object data = dataMap.get(key);

                        try {
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;         // cQualification

                            BookingMember bookingMember = new BookingMember((String) userData.get("Doctor_Details"), (String) userData.get("Patient_Details"));

                            fetchNotification.add(bookingMember.getDoctor_Details());
                            fetchNotification.add(bookingMember.getPatient_Details());

                        } catch (ClassCastException cce) {
                            try {

                                System.out.println("Data not fetched due to: " + cce.getMessage());

                                String mString = String.valueOf(dataMap.get(key));

                                aList.add(mString);

                                list1.add(aList);

                                System.out.println("From Tpo mString is: " + mString);


                            } catch (ClassCastException cce2) {

                            }
                        }
                    }

                    if (fetchNotification.size()==0) {
                        System.out.println("From showBookedDetails() list1: " + list1);

                        String val1= list1 +"";

                        if (val1.contains("[")) {
                            val1= val1.replace("[","");
                        }
                        if (val1.contains("]")) {
                            val1= val1.replace("]","");
                        }

                        if (val1.contains("{")) {
                            val1= val1.replace("{","");
                        }
                        if (val1.contains("}")) {
                            val1= val1.replace("}","");
                        }
                        if (val1.contains("null")) {
                            val1= val1.replace("null","").replace(",","").trim();
                        }

                        System.out.println("From showBookedDetails() val1 is: " + val1);

                        Set<Set<String>> a1= showBookedDetailsData.init(val1, finalName, finalMobile);

                        System.out.println("From From showBookedDetails() a1 is: " + a1);

                        StringBuilder sb1= new StringBuilder();

                        long count=1;

                        for (Set<String> s: a1) {
                            for (String s1: s) {
                                sb1.append(s1);
                                sb1.append("\n");
                            }
                            sb1.append("\n");
                            sb1.append("***  End of Notification: " + count + "  ***");
                            sb1.append("\n\n");
                            count++;
                        }

                        AlertDialog.Builder showNotification= new AlertDialog.Builder(Patient_Profile.this);
                        showNotification.setTitle("Check Booking Details");
                        showNotification.setIcon(R.drawable.doctor_profile_1);
                        showNotification.setMessage(sb1+"");
                        showNotification.setCancelable(false);
                        showNotification.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog a11= showNotification.create();
                        a11.show();
                    }
                    else {
                        System.out.println("From showBookedDetails() fetchNotification: " + fetchNotification);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private static ArrayList<String> init(String data, String name, String age) {
        String s[]= data.split("(\\d+|\\w+)-(\\w+|\\d+)-(\\w+|\\d+)-(\\w+|\\d+)-(\\w+|\\d+)|\\}|\\[|\\]|\\}|null|patient_Details=|doctor_Details=");
        ArrayList<String> doctorData= new ArrayList<>();
        String name1="", age1="";
        for (String s5: s) {
            String split[]= s5.split(",");
            for (String s11: split) {
                String doc="", fees="", schec="";
                if (!s11.isEmpty()) {
                    System.out.println("s11: " + s11);
                    if (s11.contains("name=")) {
                        name1 = s11;
                        name1 = name1.replace("name=", "");
                        name1= name1.trim();
                        System.out.println("name1: " + name1);
                    }
                    if (s11.contains("age=")) {
                        age1 = s11;
                        age1 = age1.replace("age=", "");
                        age1= age1.trim();
                        System.out.println("age1: " + age1);
                    }
                    if (name.equals(name1) && age.equals(age1)) {
                        System.out.println(name + " == " + name1 + "\n"+age + " == " + age1);
                        if (s11.contains("DoctorName:")) {
                            doc= s11;
                        }
                        if (s11.contains("Doctor fees:")) {
                            fees= s11;
                        }
                        if (s11.contains("Doctor Fees: \tDoctor Schedule:")) {
                            s11= s11.replace("Doctor Fees:", "");
                            schec= s11;
                        }
                        if (!(doc.isEmpty() && fees.isEmpty() && schec.isEmpty())) {
                            doctorData.add(doc + " " + fees + " " + schec);
                        }
                    }
                }
                else {
                    continue;
                }
            }
        }
        return doctorData;
    }

    private void fetchProfile() {
        String name, mobile, age;
        SQLiteDatabase db1= ppdb.getWritableDatabase();
        String query1 = "select * from patientprofile";
        Cursor c1 = db1.rawQuery(query1, null);
        if (c1!=null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                System.out.println("c1.getCount(): " + c1.getCount());
                System.out.println("Name: "+c1.getString(0) + " Mobile: " + c1.getString(1) + " Age: " + c1.getString(2));
                name= c1.getString(0);
                mobile= c1.getString(1);
                age= c1.getString(2);
                fetchQuerry(name, mobile, age);
            }
        }
    }

    private void fetchQuerry(String name, String mobile, String age) {
        String id1= name+ " " + mobile;

        ref= database.getInstance().getReference().child("Patient"+"/"+ id1 +"/" + name + " " + age);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value= String.valueOf(snapshot.getValue());
                System.out.println("From fetchQuery() value is: "+ value);

                showData(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Need to work from here on tomorrow morning........ From the showData()........


    private void showData(String value) {
        String split[]= value.split("\\[|\\]|null|\\{|\\}|,"), name1 = "", age1="", gender1="", mobile1="", imageurl1="";
        for (String s: split) {
            s=s.trim();
            if (!s.isEmpty()) {
                if (s.contains("name=")) {
                    s= s.replace("name=", "");
                    name1= s;
                }
                else if (s.contains("imageUrl=")) {
                    s= s.replace("imageUrl=", "");
                    imageurl1=s;
                }
                else if (s.contains("gender=")) {
                    s= s.replace("gender=","");
                    gender1= s;
                }
                else if (s.contains("mobile=")) {
                    s= s.replace("mobile=", "");
                    mobile1= s;
                }
                else if (s.contains("age=")) {
                    s= s.replace("age=", "");
                    age1= s;
                }
                System.out.println("s: " + s);
            } else {
                continue;
            }
        }
        Intent sendData= new Intent(Patient_Profile.this, ShowCreatedPatientProfile.class);
        sendData.putExtra("name", name1);
        sendData.putExtra("age", age1);
        sendData.putExtra("gender", gender1);
        sendData.putExtra("mobile", mobile1);
        sendData.putExtra("imageurl", imageurl1);

        startActivity(sendData);
    }

    private void createPatientProfile() {
        openDialog();
    }

    private void openDialog() {
        PatientProfileDialog dp= new PatientProfileDialog();
        dp.show(getSupportFragmentManager(),"Patient profile");
    }

    @Override
    public void applyFields(final String name1, final String age, final String mobile1, Uri imageUri, final String checkedItem) {
        Toast.makeText(this, " " + age + " " + mobile1 + " " + name1 + " " + checkedItem, Toast.LENGTH_SHORT).show();

        System.out.println(imageUri!=null);

        final ProgressDialog pd= new ProgressDialog(Patient_Profile.this);

        pd.setTitle("Uploading...");
        pd.show();

        final StorageReference ref
                = storageReference
                .child(
                        "Patient_Images/"
                                + name1 + " " + mobile1);

        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(Patient_Profile.this, "downloadUrl is: " + uri, Toast.LENGTH_SHORT).show();
                        uploadTextData(name1,age, mobile1,String.valueOf(uri), checkedItem);
                    }
                });
                pd.dismiss();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress
                        = (100.0
                        * snapshot.getBytesTransferred()
                        / snapshot.getTotalByteCount());
                pd.setMessage(
                        "Uploaded "
                                + (int)progress + "%");
            }
        });
    }

    private void uploadTextData(String name1, String age, String mobile1, String url, String gender) {
        database= FirebaseDatabase.getInstance();

        String id1= name1 + " " + age;

        System.out.println("");



        ref= database.getInstance().getReference().child("Patient"+"/"+ id1 +"/" + name1 + " " + mobile1);

        pm= new PatientMember();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxId= (int) snapshot.getChildrenCount();
                System.out.println("From uploadTextData(): " + snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pm.setImageUrl(url);
        pm.setMobile(mobile1);
        pm.setAge(age);
        pm.setName(name1);
        pm.setGender(gender);

        if (maxId < 1) {
            ref.child(String.valueOf(maxId + 1)).setValue(pm);

            ppdb.insertData(name1,age,mobile1);

            Toast.makeText(this, "Data uploaded successfully!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Multiple profile uploads are not allowed here!", Toast.LENGTH_SHORT).show();
        }
    }
}