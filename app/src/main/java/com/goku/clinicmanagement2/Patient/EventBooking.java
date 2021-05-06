package com.goku.clinicmanagement2.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.goku.clinicmanagement2.Database.PatientProfiledb;
import com.goku.clinicmanagement2.DoctorModel.BookingMember;
import com.goku.clinicmanagement2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class EventBooking extends AppCompatActivity {

    TextView name, fees, mobile;
    Spinner schedule;

    Button bookappointment;

    String  patientDetails="";

    String doctorSchedule[]= new String[1];

    FirebaseDatabase database, db2;
    DatabaseReference ref, childref;

    boolean wrongKey= false;

    PatientProfiledb ppdb;

    BookingMember bmk;
    private int maxId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_booking);

        ppdb= new PatientProfiledb(EventBooking.this);

        bmk = new BookingMember();

        name= (TextView) findViewById(R.id.doctor_name_id);
        fees= (TextView) findViewById(R.id.doctor_fees_id);
        mobile= (TextView) findViewById(R.id.doctor_mobile_id);

        schedule= (Spinner) findViewById(R.id.doctor_spinner_id);

        bookappointment= (Button) findViewById(R.id.doctor_booking_btn_id);

        Intent receiveData= getIntent();
        final String DoctorName= receiveData.getStringExtra("name");
        name.setText("Doctor Name: \t"+ DoctorName);
        String DoctorSpecialization= receiveData.getStringExtra("specialization");
        final String DoctorFees= receiveData.getStringExtra("fees");
        fees.setText("Fees: \t"+ DoctorFees);
        final String DoctorMobile= receiveData.getStringExtra("mobile");
        mobile.setText("Mobile: \t"+ DoctorMobile);
        String DoctorSchedule= receiveData.getStringExtra("schedule");
        System.out.println("From EventBooking Doctor Schedule: " + DoctorSchedule);
        final String DoctorImageUrl= receiveData.getStringExtra("imageurl");

        String filteredDoctorSchedule= DoctorSchedule.replace("Doctor Schedule:", "");

        String split[]= filteredDoctorSchedule.split("\n");

        String schedules[]= DoctorSchedule.split("\\  +");

        for (String s: schedules) {
            System.out.println("From EventBooking schedules: " + s);
        }


        final String Schedule[]= new String[schedules.length+1];
        Schedule[0]= "Book appointment";

        int i=1;

        for (String s: schedules) {
            System.out.println("s: " + s);
            Schedule[i]= s;
            i++;
        }

        ArrayAdapter aa = new ArrayAdapter(EventBooking.this, android.R.layout.simple_spinner_item, Schedule);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        schedule.setAdapter(aa);

        schedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(EventBooking.this, Schedule[position] , Toast.LENGTH_LONG).show();
                doctorSchedule[0]= Schedule[position];

                if (doctorSchedule[0].equals("Book appointment")) {
                    wrongKey= true;
                }

                else if (!doctorSchedule[0].equals("Book appointment")) {
                    wrongKey= false;
                }

                System.out.println("schedule doctorSchedule is: " + doctorSchedule[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bookappointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wrongKey) {
                    AlertDialog.Builder wrongInput= new AlertDialog.Builder(EventBooking.this);
                    wrongInput.setTitle("Wrong Input");
                    wrongInput.setMessage("Please choose a valid timing!");
                    wrongInput.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog a1= wrongInput.create();
                    a1.show();
                }
                else {
                    System.out.println("bookappointment doctorSchedule is: " + doctorSchedule[0]);
                    getPatientProfileDetails(DoctorName, DoctorMobile, DoctorFees, doctorSchedule[0], DoctorImageUrl);
                }
            }
        });
        database= FirebaseDatabase.getInstance();
    }

    private void getPatientProfileDetails(final String DoctorName, final String DoctorMobile, final String DoctorFees, final String DoctorSchedule, final String DoctorImageUrl) {
        String name="";
        String mobile="";
        String age="";
        // final String[] concat = { "" };
        SQLiteDatabase db1= ppdb.getWritableDatabase();
        String query1 = "select * from patientprofile";
        Cursor c1 = db1.rawQuery(query1, null);
        if (c1!=null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                System.out.println("c1.getCount(): " + c1.getCount());
                System.out.println(c1.getString(0) + " " + c1.getString(1));
                name= c1.getString(0);
                mobile= c1.getString(1);
                age= c1.getString(2);
            }
        }

        System.out.println("From getPatientProfileDetails() name: " + name + " mobile: " + mobile + " age: " + age);

        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference().child("Patient").child(name + " " + mobile).child(name + " " + age);
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value= String.valueOf(snapshot.getValue());
                patientDetails= value;
                String concat="";
                System.out.println("PatientDetails: " + patientDetails);
                System.out.println("From getPatientProfileDetails(): "+ value);

                String extractData[]= value.split("\\[|\\]|\\{|\\}|null|,");

                for (String s: extractData) {

                    s=s.trim();
                    if (!s.isEmpty()) {
                        System.out.println("extractData: " + s);
                        concat += s + " , ";
                    } else {
                        continue;
                    }
                }
                System.out.println("concat is: " + concat);

                bmk.setPatient_Details(concat);

                System.out.println("bmk.getPatientDetails(): " + bmk.getPatient_Details());

                String appointmentTime= "Appointment Time: \t"+ DoctorSchedule;

                // concat+=appointmentTime;

                System.out.println("appointmentTime is: " + concat);

                System.out.println("concat after appointmentTime is: " + appointmentTime);

                System.out.println("DoctorSchedule1 : " + doctorSchedule[0]);

                upload(concat+ appointmentTime, DoctorName, DoctorMobile, DoctorFees, DoctorSchedule, DoctorImageUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println("DoctorSchedule: " + DoctorSchedule);

        System.out.println("Doctor Schedule ends");

    }

    private void upload(String concat, String doctorName, String doctorMobile, String doctorFees, String doctorSchedule, String doctorImageUrl) {
        String allDoctorDetails="DoctorName: \t" + doctorName + " , " + "Doctor fees: \t" + doctorFees + " , "
                + "Doctor Fees: \t" + "Doctor Schedule: \t" + doctorSchedule + " , " + "Docto ImageUrl: \t" + doctorImageUrl;

        System.out.println("allDoctorDetails: " + allDoctorDetails);
        System.out.println("concat: " + concat);

        System.out.println("From upload() concat is: " + concat);

        bmk.setDoctor_Details(allDoctorDetails);
        bmk.setPatient_Details(concat);

        database= FirebaseDatabase.getInstance();

        String id1= UUID.randomUUID().toString();

        ref= database.getInstance().getReference().child("Booking").child(id1);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxId= (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child(String.valueOf(maxId + 1)).setValue(bmk);
    }
}